package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/// [Version 7](https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-7)
/// ```text
///  0                   1                   2                   3
///  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                           unix_ts_ms                          |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |          unix_ts_ms           |  ver  |       rand_a          |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |var|                        rand_b                             |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                            rand_b                             |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
///```
///
/// - unix_ts_ms: 48-bit big-endian unsigned number of the Unix Epoch timestamp in milliseconds as per Section 6.1. Occupies bits 0 through 47 (octets 0-5).
/// - ver: The 4-bit version field as defined by Section 4.2, set to 0b0111 (7). Occupies bits 48 through 51 of octet 6.
/// - rand_a: 12 bits of pseudorandom data to provide uniqueness as per Section 6.9 and/or optional constructs to guarantee additional monotonicity as per Section 6.2. Occupies bits 52 through 63 (octets 6-7).
/// - var: The 2-bit variant field as defined by Section 4.1, set to 0b10. Occupies bits 64 and 65 of octet 8.
/// - rand_b: The final 62 bits of pseudorandom data to provide uniqueness as per Section 6.9 and/or an optional counter to guarantee additional monotonicity as per Section 6.2. Occupies bits 66 through 127 (octets 8-15).
///
/// @author honhimW
/// @see Version#SORT_RANDOM
/// @since 2025-12-09
public class V7 extends AbstractGenerator {

    public V7() {
        this(Context.builder().clock(new ClockSequenceV7()).build());
    }

    public V7(Context context) {
        super(context);
        if (context.clockSequence instanceof ClockSequenceV7) {
            ClockSequenceV7 clockSequence = (ClockSequenceV7) context.clockSequence;
            clockSequence.random = context.random;
        }
    }

    @Override
    public UUID next() {
        Timestamp now = Timestamp.now(_ctx.clockSequence);
        return of(now);
    }

    /// Generate UUIDv7 with timestamp
    ///
    /// @param ts UUIDv7 timestamp
    /// @return UUIDv7
    public UUID of(Timestamp ts) {
        long counter = ts.counter;
        int counterBits = ts.usableCounterBits;

        if (counterBits > Long.SIZE) {
            throw new IllegalArgumentException("counterBits must not greater than Long.SIZE");
        }
        long millis = ts.asEpochMillis();
        Random random = _ctx.random.get();
        long high = random.nextLong();
        int low = random.nextInt();

        if (counterBits > 12) {
            long mask = -1L << (counterBits - 12);
            counter = (counter & ~mask) | ((counter & mask) << 2);
            counterBits += 2;
        }
        // highest 4-bits is version
        high &= -1L >>> (counterBits + 4);
        high |= counter << ((64 - 4) - counterBits);
        return UuidBuilder.fromUnixTimestampMillis(millis, high, low).build();
    }

    /// Resolve and decode UUIDv7 timestamp by current settings.
    ///
    /// @param uuid UUIDv7 encoded uuid
    /// @return timestamp
    public Timestamp resolveTimestamp(UUID uuid) {
        if (Version.SORT_RANDOM.match(uuid)) {
            Bytes bb = new Bytes(UUIDs.toBytes(uuid));
            long millis = (Byte.toUnsignedLong(bb.get(0)) & 0x0F) << 40;
            millis |= (Byte.toUnsignedLong(bb.get(1))) << 32;
            millis |= (Byte.toUnsignedLong(bb.get(2))) << 24;
            millis |= (Byte.toUnsignedLong(bb.get(3))) << 16;
            millis |= (Byte.toUnsignedLong(bb.get(4))) << 8;
            millis |= Byte.toUnsignedLong(bb.get(5));

            long seconds = millis / 1000;
            int nanos = (int) (millis % 1000) * 1_000_000;
            long counter;
            int usableCounterBits = _ctx.clockSequence.usableBits();
            if (_ctx.clockSequence instanceof ClockSequenceV7) {
                ClockSequenceV7 clockSequence = (ClockSequenceV7) _ctx.clockSequence;
                int bits = clockSequence.precision.bits;
                if (bits != 0) {
                    int i = bb.getInt(6); // addition precision always less than 32
                    int addition;
                    if (bits > 12) {
                        int mask = (-1 << 20) >>> 4;
                        addition = ((i & mask) >>> 2 | (i & (-1 >>> 18))) >>> (32 - (bits + 2 + 4));
                    } else {
                        int mask = (-1 << (32 - bits)) >>> 4;
                        addition = (i & mask) >>> (32 - (bits + 4));
                    }
                    if (bits >= 20) {
                        nanos += addition;
                    } else {
                        nanos += (int) (addition * 999_999L >>> bits);
                    }
                }
            }
            counter = (Byte.toUnsignedLong(bb.get(6)) & 0xF) << 38;
            counter |= (Byte.toUnsignedLong(bb.get(7))) << 30;
            counter |= (Byte.toUnsignedLong(bb.get(8)) & 0x3F) << 24;
            counter |= (Byte.toUnsignedLong(bb.get(9))) << 16;
            counter |= (Byte.toUnsignedLong(bb.get(10))) << 8;
            counter |= (Byte.toUnsignedLong(bb.get(11)));

            return new Timestamp(seconds, nanos, counter, usableCounterBits);
        }

        throw new IllegalArgumentException(String.format("UUID version %d is not supported by current Type.", uuid.version()));
    }

    /// UUIDv7 ClockSequence with reseeding timestamp.
    public static class ClockSequenceV7 implements ClockSequence {
        private static final long RESEED_MASK = -1L >>> 23;
        private static final long MAX_COUNTER = -1L >>> 22;

        private final ReseedingTimestamp timestamp;
        private Precision precision;
        private volatile long counter;
        private Supplier<Random> random;

        public ClockSequenceV7() {
            this.timestamp = new ReseedingTimestamp();
            this.precision = new Precision(usableBits(), 0);
            this.random = ThreadLocalRandom::current;
        }

        @Override
        public long generateSequence(long seconds, int nanos) {
            boolean shouldReseed = this.timestamp.advance(seconds, nanos);
            long _counter;
            if (shouldReseed) {
                _counter = this.precision.apply(random.get().nextLong() & RESEED_MASK, this.timestamp);
            } else {
                _counter = this.precision.apply(this.counter, this.timestamp) + 1;
                if (_counter > MAX_COUNTER) {
                    _counter = this.precision.apply(random.get().nextLong() & RESEED_MASK, this.timestamp);
                }
            }
            this.counter = _counter;
            return this.counter;
        }

        @Override
        public int usableBits() {
            return 42;
        }

        @Override
        public Instant now() {
            if (precision.bits == 0) {
                // millisecond
                long millis = System.currentTimeMillis();
                long seconds = millis / 1000;
                int nanos = (int) (millis % 1000) * 1_000_000;
                return Instant.ofEpochSecond(seconds, nanos);
            } else {
                // nanosecond
                return Instant.now();
            }
        }

        /// Add 12-bits timestamp precision
        ///
        /// @return self
        public ClockSequenceV7 withAdditionalPrecision() {
            return this.withAdditionalPrecision(12);
        }

        /**
         * Add custom bits length timestamp precision
         *
         * @param bits range [1, 20]
         * @return self
         */
        public ClockSequenceV7 withAdditionalPrecision(int bits) {
            if (bits < 1 || bits > 20) {
                throw new IllegalArgumentException("Timestamp addition precision out of range [0..20] is meaningless.");
            }
            this.precision = new Precision(usableBits(), bits);
            return this;
        }

    }

    private static class ReseedingTimestamp {
        private volatile long lastSeed;
        private long seconds;
        private int nanos;

        private ReseedingTimestamp() {
            this.lastSeed = 0;
            this.seconds = 0;
            this.nanos = 0;
        }

        private ReseedingTimestamp(long lastSeed, long seconds, int nanos) {
            this.lastSeed = lastSeed;
            this.seconds = seconds;
            this.nanos = nanos;
        }

        private boolean advance(long seconds, int nanos) {
            long millis = (seconds * 1000) + (nanos / 1_000_000);
            if (millis > this.lastSeed) {
                synchronized (this) {
                    if (millis > this.lastSeed) {
                        this.lastSeed = millis;
                        this.seconds = seconds;
                        this.nanos = nanos;
                        return true;
                    } else {
                        this.nanos = Math.max(this.nanos, nanos);
                    }
                }
            }
            return false;
        }

    }

    /// bits length <= 20
    private static class Precision {
        final int bits;
        final int k;
        final long r;
        final long mask;
        final long shift;

        Precision(int usableBits, int bits) {
            this.bits = bits;
            this.k = 32;
            this.r = (((1L << bits) << k) - 1) / 999_999L;
            this.mask = -1L >>> (Long.SIZE - usableBits + bits);
            this.shift = usableBits - bits;
        }

        long apply(long value, ReseedingTimestamp timestamp) {
            if (bits == 0) {
                return value & this.mask;
            }
            if (bits >= 20) {
                long addition = timestamp.nanos % 1_000_000;
                return value & this.mask | (addition << this.shift);
            }
            long addition = ((timestamp.nanos % 1_000_000) * r) >>> this.k;
            return value & this.mask | (addition << this.shift);
        }
    }

}

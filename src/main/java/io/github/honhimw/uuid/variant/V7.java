package io.github.honhimw.uuid.variant;

import io.github.honhimw.uuid.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * @author honhimW
 * @since 2025-12-09
 */

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
            long counter = 0;
            int usableCounterBits = _ctx.clockSequence.usableBits();
            if (_ctx.clockSequence instanceof ClockSequenceV7) {
                ClockSequenceV7 clockSequence = (ClockSequenceV7) _ctx.clockSequence;
                clockSequence.usableBits();
                int bits = clockSequence.precision.bits;
                if (bits == 12) {
                    int addition = (Byte.toUnsignedInt(bb.get(6)) & 0xF) << 8;
                    addition |= (Byte.toUnsignedInt(bb.get(7)));
                    nanos += addition * clockSequence.precision.factor;
                } else {
                    counter = (Byte.toUnsignedLong(bb.get(6)) & 0xF) << 38;
                    counter |= (Byte.toUnsignedLong(bb.get(7))) << 30;
                }
            } else {
                counter = (Byte.toUnsignedLong(bb.get(6)) & 0xF) << 38;
                counter |= (Byte.toUnsignedLong(bb.get(7))) << 30;
            }
            counter |= (Byte.toUnsignedLong(bb.get(8)) & 0x3F) << 24;
            counter |= (Byte.toUnsignedLong(bb.get(9))) << 16;
            counter |= (Byte.toUnsignedLong(bb.get(10))) << 8;
            counter |= (Byte.toUnsignedLong(bb.get(11)));

            return new Timestamp(seconds, nanos, counter, usableCounterBits);
        }

        throw new IllegalArgumentException(String.format("UUID version %d is not supported by current Type.", uuid.version()));
    }

    public static class ClockSequenceV7 implements ClockSequence {
        private static final long RESEED_MASK = Long.MAX_VALUE >> 22;
        private static final long MAX_COUNTER = Long.MAX_VALUE >> 21;

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

        public ClockSequenceV7 withAdditionalPrecision() {
            this.precision = new Precision(usableBits(), 12);
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

    private static class Precision {
        private final int bits;
        private final int factor;
        private final long mask;
        private final long shift;

        private Precision(int usableBits, int bits) {
            this.bits = bits;
            this.factor = (int) (999_999 / (bits >= 1 ? (2L << (bits - 1)) : 1)) + 1;
            this.mask = -1 >>> (Long.SIZE - usableBits + bits);
            this.shift = usableBits - bits;
        }

        private long apply(long value, ReseedingTimestamp timestamp) {
            if (this.bits == 0) {
                return value;
            }
            long addition = timestamp.nanos % 1_000_000 / this.factor;
            return value & this.mask | (addition << this.shift);
        }

    }

}

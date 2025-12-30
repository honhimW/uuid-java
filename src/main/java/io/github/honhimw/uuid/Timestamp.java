package io.github.honhimw.uuid;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/// Time based UUID timestamp context.
///
/// @author honhimW
/// @since 2025-12-08
public class Timestamp implements Comparable<Timestamp>, Serializable {

    /// unix epoch second
    public final long seconds;

    /// unix nano seconds
    public final int nanos;

    /// clock sequence
    public final long counter;

    /// counter bit length in uuid
    public final int usableCounterBits;

    public Timestamp(long seconds, int nanos, long counter, int usableCounterBits) {
        this.seconds = seconds;
        this.nanos = nanos;
        this.counter = counter;
        this.usableCounterBits = usableCounterBits;
    }

    public long asEpochMillis() {
        long millis = seconds * 1_000L;
        return millis + Maths.ns2ms(nanos);
    }

    public Instant asInstant() {
        return Instant.ofEpochSecond(seconds, nanos);
    }

    public long asGregorian() {
        return unix2Gregorian(seconds, nanos);
    }

    public static Timestamp now(ClockSequence context) {
        return of(context, context.now());
    }

    public static Optional<Timestamp> of(UUID uuid) {
        return Uuid.fromUUID(uuid).timestamp();
    }

    public static Timestamp of(ClockSequence context, Instant instant) {
        long epochSecond = instant.getEpochSecond();
        int nano = instant.getNano();
        long counter = context.generateSequence(epochSecond, nano);
        int usableBits = context.usableBits();
        return new Timestamp(
            epochSecond,
            nano,
            counter,
            usableBits
        );
    }

    public static Timestamp fromGregorian(long ticks, long counter) {
        Instant instant = gregorian2unix(ticks);
        return new Timestamp(instant.getEpochSecond(), instant.getNano(), counter, 14);
    }

    /// Coordinated Universal Time (UTC) as a count of 100-nanosecond intervals since 00:00:00.00, 15 October 1582
    public static final long TICKS_OFFSET = 0x01B2_1DD2_1381_4000L;
    public static final long SECOND_MULTIPLIER = 10_000_000L;
    public static final long NANO_MULTIPLIER = 100L;

    /// Unix timestamp to gregorian calendar tickets
    ///
    /// @param seconds unix seconds
    /// @param nanos   unix nanoseconds
    /// @return gregorian calendar tickets
    public static long unix2Gregorian(long seconds, int nanos) {
        long ticks = TICKS_OFFSET;
        ticks += seconds * SECOND_MULTIPLIER;
        ticks += nanos / NANO_MULTIPLIER;
        return ticks;
    }

    /// Gregorian calendar tickets to unix timestamp
    ///
    /// @param ticks gregorian calendar tickets
    /// @return gregorian calendar tickets
    public static Instant gregorian2unix(long ticks) {
        ticks -= TICKS_OFFSET;
        long seconds = ticks / SECOND_MULTIPLIER;
        int nanos = (int) ((ticks % SECOND_MULTIPLIER) * NANO_MULTIPLIER);
        return Instant.ofEpochSecond(seconds, nanos);
    }

    @Override
    public int compareTo(Timestamp other) {
        int cmp = Long.compare(this.seconds, other.seconds);
        if (cmp != 0) {
            return cmp;
        }
        cmp = Integer.compare(this.nanos, other.nanos);
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(this.counter, other.counter);
    }

    @Override
    public String toString() {
        return "Timestamp{" +
               "seconds=" + seconds +
               ", nanos=" + nanos +
               ", counter=" + counter +
               ", usableCounterBits=" + usableCounterBits +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Timestamp timestamp = (Timestamp) o;
        return seconds == timestamp.seconds && nanos == timestamp.nanos && counter == timestamp.counter && usableCounterBits == timestamp.usableCounterBits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seconds, nanos, counter, usableCounterBits);
    }
}

package io.github.honhimw.uuid;

import java.time.Instant;

/// UUID clock-sequence generating context.
///
/// @author honhimW
/// @since 2025-12-08
public interface ClockSequence {

    /// Generate sequence by unix-timestamp
    ///
    /// @param seconds unix second
    /// @param nanos   unix nanosecond
    /// @return clock sequence
    long generateSequence(long seconds, int nanos);

    /// Clock sequence bit-length in uuid.
    ///
    /// @return bit length
    int usableBits();

    /// Get current timestamp.
    ///
    /// In most cases, millisecond is enough. [System#currentTimeMillis()] is much more faster than [Instant#now()]
    ///
    /// @return unix timestamp
    default Instant now() {
        return Instant.now();
    }

}

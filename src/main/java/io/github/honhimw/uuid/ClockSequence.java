package io.github.honhimw.uuid;

import java.time.Instant;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public interface ClockSequence {

    long generateSequence(long seconds, int nanos);

    int usableBits();

    default Instant now() {
        return Instant.now();
    }

}

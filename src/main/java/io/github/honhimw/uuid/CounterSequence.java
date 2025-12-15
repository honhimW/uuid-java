package io.github.honhimw.uuid;

import java.util.concurrent.atomic.AtomicLong;

/// Clock sequence generator via counter.
///
/// @author honhimW
/// @since 2025-12-09
public class CounterSequence implements ClockSequence {

    /// Shared counter clock-sequence
    public static final CounterSequence SHARED = new CounterSequence(0);

    private final AtomicLong count;

    private final long mask;

    /// Construct with initial-value
    /// @param count initial-value
    public CounterSequence(int count) {
        this.count = new AtomicLong(count);
        this.mask = ~(-1L << usableBits());
    }

    @Override
    public long generateSequence(long seconds, int nanos) {
        return count.getAndIncrement() & mask;
    }

    @Override
    public int usableBits() {
        return 14;
    }
}

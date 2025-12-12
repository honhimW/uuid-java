package io.github.honhimw.uuid;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author honhimW
 * @since 2025-12-09
 */
public class CounterSequence implements ClockSequence {

    public static final CounterSequence SHARED = new CounterSequence(0);

    private final AtomicLong count;

    private final long mask;

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

package io.github.honhimw.uuid;

import java.util.Random;
import java.util.function.Supplier;

/// Clock sequence generator via random value.
///
/// @author honhimW
/// @since 2025-12-09
public class RandomSequence implements ClockSequence {

    private final Supplier<Random> random;

    private final long mask;

    /// Construct clock sequence with random provider
    ///
    /// @param random random provider
    public RandomSequence(Supplier<Random> random) {
        this.random = random;
        this.mask = ~(-1L << usableBits());
    }

    @Override
    public long generateSequence(long seconds, int nanos) {
        return random.get().nextLong() & mask;
    }

    @Override
    public int usableBits() {
        return 14;
    }

}

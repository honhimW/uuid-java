package bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/// @author honhimW
/// @since 2025-12-10
@State(Scope.Benchmark)
public abstract class AbstractBench {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
    @Fork(1)
    public void run() throws Exception {

    }

}

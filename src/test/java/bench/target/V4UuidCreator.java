package bench.target;

import bench.AbstractBench;
import com.github.f4b6a3.uuid.UuidCreator;

/// @author honhimW
/// @since 2025-12-10
public class V4UuidCreator extends AbstractBench {
    @Override
    public void run() throws Exception {
        UuidCreator.getRandomBasedFast();
    }
}

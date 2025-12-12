package bench.target;

import bench.AbstractBench;
import bench.Fixtures;
import com.github.f4b6a3.uuid.UuidCreator;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V5UuidCreator extends AbstractBench {
    @Override
    public void run() throws Exception {
        UuidCreator.getNameBasedSha1(Fixtures.NAME);
    }
}

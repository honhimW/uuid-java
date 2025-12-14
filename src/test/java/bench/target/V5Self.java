package bench.target;

import bench.AbstractBench;
import bench.Fixtures;
import io.github.honhimw.uuid.UUIDs;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V5Self extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDs.FAST.V5.of(Fixtures.NAME);
    }

}

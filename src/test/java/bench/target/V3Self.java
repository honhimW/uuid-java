package bench.target;

import bench.AbstractBench;
import bench.Fixtures;
import io.github.honhimw.uuid.UUIDs;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V3Self extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDs.FAST.v3().of(Fixtures.NAME);
    }

}

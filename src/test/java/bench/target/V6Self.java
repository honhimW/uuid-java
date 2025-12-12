package bench.target;

import bench.AbstractBench;
import io.github.honhimw.uuid.UUIDs;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V6Self extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDs.FAST.v6().next();
    }

}

package bench.target;

import bench.AbstractBench;
import io.github.honhimw.uuid.UUIDs;
import io.github.honhimw.uuid.variant.V7;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V7Self extends AbstractBench {

    static final V7 V7 = UUIDs.FAST.v7();

    @Override
    public void run() throws Exception {
        V7.next();
    }

}

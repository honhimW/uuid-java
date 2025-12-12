package bench.target;

import bench.AbstractBench;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-11
 */

public class V4Jdk extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUID.randomUUID();
    }
}

package bench.target;

import bench.AbstractBench;
import io.github.robsonkades.uuidv7.UUIDv7;

/**
 *
 * @author honhimW
 * @since 2025-12-10
 */

public class V7Fastest extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDv7.randomUUID();
    }
}

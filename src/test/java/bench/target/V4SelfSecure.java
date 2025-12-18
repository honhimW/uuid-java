package bench.target;

import bench.AbstractBench;
import io.github.honhimw.uuid.UUIDs;

/// @author honhimW
/// @since 2025-12-10
public class V4SelfSecure extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDs.SECURE.V4.next();
    }

}

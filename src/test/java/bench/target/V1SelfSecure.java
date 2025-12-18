package bench.target;

import bench.AbstractBench;
import io.github.honhimw.uuid.UUIDs;

/// @author honhimW
/// @since 2025-12-10
public class V1SelfSecure extends AbstractBench {

    @Override
    public void run() throws Exception {
        UUIDs.SECURE.V1.next();
    }

}

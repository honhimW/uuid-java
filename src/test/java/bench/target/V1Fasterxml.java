package bench.target;

import bench.AbstractBench;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V1Fasterxml extends AbstractBench {

    static final NoArgGenerator GENERATOR = Generators.timeBasedGenerator();

    @Override
    public void run() throws Exception {
        GENERATOR.generate();
    }
}

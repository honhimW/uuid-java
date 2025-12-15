package bench.target;

import bench.AbstractBench;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

import java.util.concurrent.ThreadLocalRandom;

/// @author honhimW
/// @since 2025-12-10
public class V4Fasterxml extends AbstractBench {

    static final NoArgGenerator GENERATOR = Generators.randomBasedGenerator(ThreadLocalRandom.current());

    @Override
    public void run() throws Exception {
        GENERATOR.generate();
    }
}

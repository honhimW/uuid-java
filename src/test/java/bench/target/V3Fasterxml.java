package bench.target;

import bench.AbstractBench;
import bench.Fixtures;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.StringArgGenerator;
import io.github.honhimw.uuid.UUIDs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author honhimW
 * @since 2025-12-10
 */

public class V3Fasterxml extends AbstractBench {

    static final StringArgGenerator GENERATOR;

    static {
        try {
            GENERATOR = Generators.nameBasedGenerator(UUIDs.NAMESPACE_DNS, MessageDigest.getInstance("MD5"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() throws Exception {
        GENERATOR.generate(Fixtures.NAME);
    }
}

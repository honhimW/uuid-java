package io.github.honhimw.uuid;

import java.util.UUID;

/**
 * UUID generator.
 *
 * @author honhimW
 * @since 2025-12-08
 */

public interface Generator {

    /**
     * No-args UUID generation.
     * <p>
     * For time-based generator, using current timestamp.
     * <p>
     * For name-based generator, using random bytes.
     * @return random uuid
     */
    UUID next();

}

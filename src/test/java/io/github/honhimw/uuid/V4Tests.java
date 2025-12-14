package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public class V4Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V4.next();
        Assertions.assertEquals(4, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

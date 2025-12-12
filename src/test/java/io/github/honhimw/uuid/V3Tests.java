package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V3;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public class V3Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.v3().next();
        Assertions.assertEquals(3, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        V3 dns = V3.dns();
        UUID uuid = dns.of("foo".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(3, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

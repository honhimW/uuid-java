package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V5;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/// @author honhimW
/// @since 2025-12-08
public class V5Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V5.next();
        Assertions.assertEquals(5, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        V5 dns = V5.dns();
        UUID uuid = dns.of("foo".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(5, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

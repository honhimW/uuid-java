package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V3;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/// @author honhimW
/// @since 2025-12-08
public class V3Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V3.next();
        Assertions.assertEquals(3, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        V3 dns = V3.dns();
        byte[] name = "foo".getBytes(StandardCharsets.UTF_8);
        UUID uuid = dns.of(name);
        Assertions.assertEquals(3, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void withNamespace() {
        byte[] name = "foo".getBytes(StandardCharsets.UTF_8);
        V3 v3 = new V3(UUIDs.NAMESPACE_X500);
        UUID uuid = v3.of(name);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(UUIDs.toBytes(UUIDs.NAMESPACE_X500));
        byte[] digest = md.digest(name);
        UUID md5UUID = UuidBuilder.fromBytes(digest).version(Version.MD5).variant(Variant.RFC4122).build();
        Assertions.assertEquals(md5UUID, uuid);
    }

    @Test
    @SneakyThrows
    void noNamespace() {
        byte[] name = "foo".getBytes(StandardCharsets.UTF_8);
        V3 v3 = new V3(null);
        UUID uuid = v3.of(name);
        byte[] digest = MessageDigest.getInstance("MD5").digest(name);
        UUID md5UUID = UuidBuilder.fromBytes(digest).version(Version.MD5).variant(Variant.RFC4122).build();
        Assertions.assertEquals(md5UUID, uuid);
    }

}

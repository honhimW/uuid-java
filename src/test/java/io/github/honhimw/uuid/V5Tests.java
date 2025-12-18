package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V3;
import io.github.honhimw.uuid.gen.V5;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

    @Test
    @SneakyThrows
    void withNamespace() {
        byte[] name = "foo".getBytes(StandardCharsets.UTF_8);
        V5 v5 = new V5(UUIDs.NAMESPACE_X500);
        UUID uuid = v5.of(name);
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(UUIDs.toBytes(UUIDs.NAMESPACE_X500));
        byte[] digest = md.digest(name);
        UUID md5UUID = UuidBuilder.fromBytes(Bytes.copyOf(digest, 16)).version(Version.SHA1).variant(Variant.RFC4122).build();
        Assertions.assertEquals(md5UUID, uuid);
    }

    @Test
    @SneakyThrows
    void noNamespace() {
        byte[] name = "foo".getBytes(StandardCharsets.UTF_8);
        V5 v5 = new V5(null);
        UUID uuid = v5.of(name);
        byte[] digest = MessageDigest.getInstance("SHA1").digest(name);
        UUID md5UUID = UuidBuilder.fromBytes(Bytes.copyOf(digest, 16)).version(Version.SHA1).variant(Variant.RFC4122).build();
        Assertions.assertEquals(md5UUID, uuid);
    }

}

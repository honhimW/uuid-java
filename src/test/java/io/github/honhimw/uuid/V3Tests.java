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

    /// [Example of a UUIDv3 Value](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv3-value)
    /// ```text
    /// Namespace (DNS):  6ba7b810-9dad-11d1-80b4-00c04fd430c8
    /// Name:             www.example.com
    /// ------------------------------------------------------
    /// MD5:              5df418813aed051548a72f4a814cf09e
    ///
    /// -------------------------------------------
    /// field     bits value
    /// -------------------------------------------
    /// md5_high  48   0x5df418813aed
    /// ver        4   0x3
    /// md5_mid   12   0x515
    /// var        2   0b10
    /// md5_low   62   0b00, 0x8a72f4a814cf09e
    /// -------------------------------------------
    /// total     128
    /// -------------------------------------------
    /// final: 5df41881-3aed-3515-88a7-2f4a814cf09e
    ///
    /// MD5 hex and dash:      5df41881-3aed-0515-48a7-2f4a814cf09e
    /// Ver and Var Overwrite: xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx
    /// Final:                 5df41881-3aed-3515-88a7-2f4a814cf09e
    /// ```
    @Test
    @SneakyThrows
    void example() {
        UUID uuid = V3.dns().of("www.example.com");
        Assertions.assertEquals("5df41881-3aed-3515-88a7-2f4a814cf09e", uuid.toString());
        Assertions.assertEquals(3, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

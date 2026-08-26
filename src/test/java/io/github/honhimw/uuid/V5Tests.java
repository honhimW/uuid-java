package io.github.honhimw.uuid;

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

    /// [Example of a UUIDv5 Value](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv5-value)
    /// ```text
    /// Namespace (DNS):  6ba7b810-9dad-11d1-80b4-00c04fd430c8
    /// Name:             www.example.com
    /// ----------------------------------------------------------
    /// SHA-1:            2ed6657de927468b55e12665a8aea6a22dee3e35
    ///
    /// -------------------------------------------
    /// field      bits value
    /// -------------------------------------------
    /// sha1_high  48   0x2ed6657de927
    /// ver         4   0x5
    /// sha1_mid   12   0x68b
    /// var         2   0b10
    /// sha1_low   62   0b01, 0x5e12665a8aea6a2
    /// -------------------------------------------
    /// total      128
    /// -------------------------------------------
    /// final: 2ed6657d-e927-568b-95e1-2665a8aea6a2
    ///
    /// SHA-1 hex and dash:    2ed6657d-e927-468b-55e1-2665a8aea6a2-2dee3e35
    /// Ver and Var Overwrite: xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx
    /// Final:                 2ed6657d-e927-568b-95e1-2665a8aea6a2
    /// Discarded:                                                 -2dee3e35
    /// ```
    @Test
    @SneakyThrows
    void example() {
        UUID uuid = V5.dns().of("www.example.com");
        Assertions.assertEquals("2ed6657d-e927-568b-95e1-2665a8aea6a2", uuid.toString());
        Assertions.assertEquals(5, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

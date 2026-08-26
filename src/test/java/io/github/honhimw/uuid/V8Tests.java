package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.AbstractGenerator;
import io.github.honhimw.uuid.gen.AbstractNameBasedGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.util.UUID;

/// @author honhimW
/// @since 2026-08-24

public class V8Tests {

    /// [Example of a UUIDv8 Value (Time-Based)](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv8-value-t)
    /// ```text
    /// -------------------------------------------
    /// field     bits value
    /// -------------------------------------------
    /// custom_a  48   0x2489E9AD2EE2
    /// ver        4   0x8
    /// custom_b  12   0xE00
    /// var        2   0b10
    /// custom_c  62   0b00, 0xEC932D5F69181C0
    /// -------------------------------------------
    /// total     128
    /// -------------------------------------------
    /// final: 2489E9AD-2EE2-8E00-8EC9-32D5F69181C0
    /// ```
    @Test
    @SneakyThrows
    void timeBased() {
        Bytes bytes = new Bytes(16);
        bytes
            // 60-bits timestamp
            .putLong(0x2489E9AD2EE2E00L << 4)
            // random data
            .putLong(0xEC932D5F69181C0L)
            // skipping version 4 bits
            .partialShiftRight(48, 12, 4)
        ;
        UuidBuilder builder = UuidBuilder.fromBytes(bytes.unwrap());
        UUID uuid = builder.version(Version.CUSTOM).variant(Variant.RFC4122).build();
        Assertions.assertEquals("2489E9AD-2EE2-8E00-8EC9-32D5F69181C0", uuid.toString().toUpperCase());
        Assertions.assertEquals(8, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    /// [Example of a UUIDv8 Value (Name-Based)](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv8-value-n)
    /// ```text
    /// Namespace (DNS):       6ba7b810-9dad-11d1-80b4-00c04fd430c8
    /// Name:                  www.example.com
    /// ----------------------------------------------------------------
    /// SHA-256:
    /// 5c146b143c524afd938a375d0df1fbf6fe12a66b645f72f6158759387e51f3c8
    ///
    /// -------------------------------------------
    /// field     bits value
    /// -------------------------------------------
    /// custom_a  48   0x5c146b143c52
    /// ver        4   0x8
    /// custom_b  12   0xafd
    /// var        2   0b10
    /// custom_c  62   0b00, 0x38a375d0df1fbf6
    /// -------------------------------------------
    /// total     128
    /// -------------------------------------------
    /// final: 5c146b14-3c52-8afd-938a-375d0df1fbf6
    /// ```
    @Test
    @SneakyThrows
    void nameBased() {
        Generator.NameBased generator = new AbstractNameBasedGenerator(UUIDs.NAMESPACE_DNS) {
            @Override
            protected String algorithm() {
                return "SHA256";
            }

            @Override
            public UUID of(byte[] name) {
                MessageDigest md = getDigester();
                byte[] digest = md.digest(name);
                digest = Bytes.copyOf(digest, 16);
                return UuidBuilder.fromBytes(digest)
                    .variant(Variant.RFC4122)
                    .version(Version.CUSTOM)
                    .build();
            }
        };
        UUID uuid = generator.of("www.example.com");
        Assertions.assertEquals("5c146b14-3c52-8afd-938a-375d0df1fbf6", uuid.toString());
        Assertions.assertEquals(8, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

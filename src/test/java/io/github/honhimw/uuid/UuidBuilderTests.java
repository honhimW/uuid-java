package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/// @author honhimW
/// @since 2026-09-04

public class UuidBuilderTests {

    @Test
    @SneakyThrows
    void empty() {
        UuidBuilder empty = UuidBuilder.empty();
        UUID uuid = empty.build();
        Assertions.assertEquals(UUIDs.NIL, uuid);
    }

    @Test
    @SneakyThrows
    void version() {
        UuidBuilder empty = UuidBuilder.empty();
        UUID uuid = empty.version(Version.CUSTOM).build();
        Assertions.assertEquals(Version.CUSTOM.value(), uuid.version());
    }

    @Test
    @SneakyThrows
    void variant() {
        UuidBuilder empty = UuidBuilder.empty();
        UUID uuid = empty.variant(Variant.FUTURE).build();
        Assertions.assertEquals(Variant.FUTURE.value(), uuid.variant());
    }

    @Test
    @SneakyThrows
    void fromPair() {
        UUID v4 = UUID.randomUUID();
        UUID uuid = UuidBuilder.fromPair(v4.getMostSignificantBits(), v4.getLeastSignificantBits()).build();
        Assertions.assertEquals(v4, uuid);
    }

}

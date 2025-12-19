package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/// @author honhimW
/// @since 2025-12-19
public class VersionTests {

    @Test
    @SneakyThrows
    void testOf() {
        UuidBuilder builder = UuidBuilder.fromPair(UUIDs.NIL.getMostSignificantBits(), UUIDs.NIL.getLeastSignificantBits());
        Assertions.assertEquals(Version.NIL, Version.of(builder.build()));
        {
            UUID uuid = builder.version(Version.MAC).build();
            Assertions.assertEquals(Version.MAC, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.DCE).build();
            Assertions.assertEquals(Version.DCE, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.MD5).build();
            Assertions.assertEquals(Version.MD5, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.RANDOM).build();
            Assertions.assertEquals(Version.RANDOM, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.SHA1).build();
            Assertions.assertEquals(Version.SHA1, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.SORT_MAC).build();
            Assertions.assertEquals(Version.SORT_MAC, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.SORT_RANDOM).build();
            Assertions.assertEquals(Version.SORT_RANDOM, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.CUSTOM).build();
            Assertions.assertEquals(Version.CUSTOM, Version.of(uuid));
        }
        {
            UUID uuid = builder.version(Version.MAX).build();
            Assertions.assertEquals(Version.MAX, Version.of(uuid));
        }
    }

    @Test
    @SneakyThrows
    void testParseFromString() {
        for (int i = 0; i < 0x10; i++) {
            UUID uuid = UUID.fromString("00000000-0000-%x000-0000-000000000000".formatted(i));
            Uuid _uuid = Uuid.fromUUID(uuid);
            switch (i) {
                case 0 -> {
                    Assertions.assertEquals(Version.NIL, Version.of(uuid));
                    Assertions.assertEquals(Version.NIL, _uuid.version());
                }
                case 1 -> {
                    Assertions.assertEquals(Version.MAC, Version.of(uuid));
                    Assertions.assertEquals(Version.MAC, _uuid.version());
                }
                case 2 -> {
                    Assertions.assertEquals(Version.DCE, Version.of(uuid));
                    Assertions.assertEquals(Version.DCE, _uuid.version());
                }
                case 3 -> {
                    Assertions.assertEquals(Version.MD5, Version.of(uuid));
                    Assertions.assertEquals(Version.MD5, _uuid.version());
                }
                case 4 -> {
                    Assertions.assertEquals(Version.RANDOM, Version.of(uuid));
                    Assertions.assertEquals(Version.RANDOM, _uuid.version());
                }
                case 5 -> {
                    Assertions.assertEquals(Version.SHA1, Version.of(uuid));
                    Assertions.assertEquals(Version.SHA1, _uuid.version());
                }
                case 6 -> {
                    Assertions.assertEquals(Version.SORT_MAC, Version.of(uuid));
                    Assertions.assertEquals(Version.SORT_MAC, _uuid.version());
                }
                case 7 -> {
                    Assertions.assertEquals(Version.SORT_RANDOM, Version.of(uuid));
                    Assertions.assertEquals(Version.SORT_RANDOM, _uuid.version());
                }
                case 8 -> {
                    Assertions.assertEquals(Version.CUSTOM, Version.of(uuid));
                    Assertions.assertEquals(Version.CUSTOM, _uuid.version());
                }
                case 0xF -> {
                    Assertions.assertEquals(Version.MAX, Version.of(uuid));
                    Assertions.assertEquals(Version.MAX, _uuid.version());
                }
            }
        }
    }

}

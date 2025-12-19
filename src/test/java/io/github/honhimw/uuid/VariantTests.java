package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/// @author honhimW
/// @since 2025-12-19
public class VariantTests {

    @Test
    @SneakyThrows
    void testOf() {
        UuidBuilder builder = UuidBuilder.fromPair(UUIDs.NIL.getMostSignificantBits(), UUIDs.NIL.getLeastSignificantBits());
        {
            UUID uuid = builder.variant(Variant.NCS).build();
            Assertions.assertEquals(Variant.NCS, Variant.of(uuid));
        }
        {
            UUID uuid = builder.variant(Variant.RFC4122).build();
            Assertions.assertEquals(Variant.RFC4122, Variant.of(uuid));
        }
        {
            UUID uuid = builder.variant(Variant.MICROSOFT).build();
            Assertions.assertEquals(Variant.MICROSOFT, Variant.of(uuid));
        }
        {
            UUID uuid = builder.variant(Variant.FUTURE).build();
            Assertions.assertEquals(Variant.FUTURE, Variant.of(uuid));
        }
    }

    @Test
    @SneakyThrows
    void testParseFromString() {
        for (int i = 0; i < 0x10; i++) {
            UUID uuid = UUID.fromString("00000000-0000-0000-%x000-000000000000".formatted(i));
            Uuid _uuid = Uuid.fromUUID(uuid);
            if (i <= 7) {
                Assertions.assertEquals(Variant.NCS, Variant.of(uuid));
                Assertions.assertEquals(Variant.NCS, _uuid.variant());
            } else if (i <= 0xB) {
                Assertions.assertEquals(Variant.RFC4122, Variant.of(uuid));
                Assertions.assertEquals(Variant.RFC4122, _uuid.variant());
            } else if (i <= 0xD) {
                Assertions.assertEquals(Variant.MICROSOFT, Variant.of(uuid));
                Assertions.assertEquals(Variant.MICROSOFT, _uuid.variant());
            } else {
                Assertions.assertEquals(Variant.FUTURE, Variant.of(uuid));
                Assertions.assertEquals(Variant.FUTURE, _uuid.variant());
            }
        }
    }

}

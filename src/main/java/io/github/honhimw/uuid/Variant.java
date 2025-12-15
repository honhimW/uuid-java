package io.github.honhimw.uuid;

import java.util.UUID;

/// UUID variant.
/// 64-bits
/// @author honhimW
/// @since 2025-12-09
public enum Variant {

    NCS(0b0),
    RFC4122(0b10),
    MICROSOFT(0b110),
    FUTURE(0b111),
    ;

    private final int value;

    Variant(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Variant of(UUID uuid) {
        return of(uuid.variant());
    }

    public static Variant of(int variant) {
        switch (variant) {
            case 0b0:
                return NCS;
            case 0b10:
                return RFC4122;
            case 0b110:
                return MICROSOFT;
            case 0b111:
                return FUTURE;
            default:
                throw new IllegalArgumentException("Unknown variant value: " + variant);
        }
    }

    /// Resolve UUID variant by sing byte.
    /// @param b the 9th byte in UUID
    /// @return UUID variant
    public static Variant of(byte b) {
        if ((b & 0x80) == 0x00) {
            return Variant.NCS;
        } else if ((b & 0xC0) == 0x80) {
            return Variant.RFC4122;
        } else if ((b & 0xE0) == 0xC0) {
            return Variant.MICROSOFT;
        } else {
            return Variant.FUTURE;
        }
    }

}

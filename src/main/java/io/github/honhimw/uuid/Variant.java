package io.github.honhimw.uuid;

import java.util.UUID;

/// UUID variant
/// [Variant Field](https://www.rfc-editor.org/rfc/rfc9562.html#name-variant-field)
///
/// 64-(+n) bits
///
/// @author honhimW
/// @since 2025-12-09
public enum Variant {

    /// xxxxxxxx-xxxx-xxxx-(1-7)xxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Variant |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | x    | x    | x    | 0-7     |
    NCS(0b0),

    /// xxxxxxxx-xxxx-xxxx-(8,9,A,B)xxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Variant |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 1    | 0    | x    | x    | 8-9,A-B |
    RFC4122(0b10),

    /// xxxxxxxx-xxxx-xxxx-(C,D)xxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Variant |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 1    | 1    | 0    | x    | C-D     |
    MICROSOFT(0b110),

    /// xxxxxxxx-xxxx-xxxx-(E,F)xxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Variant |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 1    | 1    | 1    | x    | E-F     |
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
    ///
    /// Same as follows:
    /// ```java
    /// int i = (b & 0xF0) >>> 4;
    /// return switch (i) {
    ///     case 0..7 -> NCS;
    ///     case 8..0xB -> RFC4122;
    ///     case 0xC..0xD -> MICROSOFT;
    ///     default -> FUTURE;
    /// }
    /// ```
    ///
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

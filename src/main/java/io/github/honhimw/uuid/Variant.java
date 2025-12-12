package io.github.honhimw.uuid;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-09
 */

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

}

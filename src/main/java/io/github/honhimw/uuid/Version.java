package io.github.honhimw.uuid;

import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-09
 */

public enum Version {

    NIL(0),
    MAC(1),
    DCE(2),
    MD5(3),
    RANDOM(4),
    SHA1(5),
    SORT_MAC(6),
    SORT_RANDOM(7),
    CUSTOM(8),
    MAX(0xFF),
    ;

    private final int value;

    Version(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Version of(UUID uuid) {
        return of(uuid.version());
    }

    public static Version of(int version) {
        switch (version) {
            case 0:
                return NIL;
            case 1:
                return MAC;
            case 2:
                return DCE;
            case 3:
                return MD5;
            case 4:
                return RANDOM;
            case 5:
                return SHA1;
            case 6:
                return SORT_MAC;
            case 7:
                return SORT_RANDOM;
            case 8:
                return CUSTOM;
            case 0xF:
                return MAX;
            default:
                throw new IllegalArgumentException("Unknown version: " + version);
        }
    }

    public boolean match(UUID uuid) {
        return uuid.version() == this.value;
    }

}

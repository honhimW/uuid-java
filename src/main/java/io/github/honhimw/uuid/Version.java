package io.github.honhimw.uuid;

import java.util.UUID;

/// UUID version
/// [Version Field](https://www.rfc-editor.org/rfc/rfc9562.html#name-version-field)
///
/// 48-51 bits
///
/// @author honhimW
/// @since 2025-12-09
public enum Version {

    /// xxxxxxxx-xxxx-0xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 0    | 0    | 0    | 0       |
    NIL(0),

    /// xxxxxxxx-xxxx-1xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 0    | 0    | 1    | 1       |
    MAC(1),

    /// xxxxxxxx-xxxx-2xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 0    | 1    | 0    | 2       |
    DCE(2),

    /// xxxxxxxx-xxxx-3xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 0    | 1    | 1    | 3       |
    MD5(3),

    /// xxxxxxxx-xxxx-4xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 1    | 0    | 0    | 4       |
    RANDOM(4),

    /// xxxxxxxx-xxxx-5xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 1    | 0    | 1    | 5       |
    SHA1(5),

    /// xxxxxxxx-xxxx-6xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 1    | 1    | 0    | 6       |
    SORT_MAC(6),

    /// xxxxxxxx-xxxx-7xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 0    | 1    | 1    | 1    | 7       |
    SORT_RANDOM(7),

    /// xxxxxxxx-xxxx-8xxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 1    | 0    | 0    | 0    | 8       |
    CUSTOM(8),

    /// xxxxxxxx-xxxx-Fxxx-xxxx-xxxxxxxxxxxx
    ///
    /// | MSB0 | MSB1 | MSB2 | MSB3 | Version |
    /// |:----:|:----:|:----:|:----:|:------- |
    /// | 1    | 1    | 1    | 1    | 15       |
    MAX(0xF),
    ;

    private final int value;

    Version(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public boolean match(UUID uuid) {
        return uuid.version() == this.value;
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

    public static Version of(byte b) {
        return of(Byte.toUnsignedInt(b) >>> 4);
    }

}

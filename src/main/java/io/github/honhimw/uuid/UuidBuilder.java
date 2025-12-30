package io.github.honhimw.uuid;

import java.util.UUID;

/// UUID builder for modifying version an variant.
///
/// @author honhimW
/// @since 2025-12-08
public class UuidBuilder {

    private final Bytes bytes;

    private UuidBuilder() {
        this.bytes = new Bytes(new byte[16]);
    }

    /// set variant field
    ///
    /// @param variant uuid variant
    /// @return self
    public UuidBuilder variant(int variant) {
        return variant(Variant.of(variant));
    }

    /// set variant field
    ///
    /// @param variant uuid variant
    /// @return self
    public UuidBuilder variant(Variant variant) {
        byte b = this.bytes.get(8);
        switch (variant) {
            case NCS:
                b &= 0x7F;
                break;
            case RFC4122:
                b &= 0x3F;
                b |= (byte) 0x80;
                break;
            case MICROSOFT:
                b &= 0x1F;
                b |= (byte) 0xC0;
                break;
            case FUTURE:
                b |= (byte) 0xE0;
                break;
        }
        this.bytes.put(8, b);
        return this;
    }

    /// set version field
    ///
    /// @param version uuid version
    /// @return self
    public UuidBuilder version(Version version) {
        return version(version.value());
    }

    /// set version field
    ///
    /// @param version uuid version
    /// @return self
    public UuidBuilder version(int version) {
        byte b = this.bytes.get(6);
        b &= 0x0F;
        b |= (byte) (version << 4);
        this.bytes.put(6, b);
        return this;
    }

    public UUID build() {
        return new UUID(this.bytes.getLong(0), this.bytes.getLong(Long.BYTES));
    }

    /// create an empty uuid builder with [UUIDs#NIL] as default.
    ///
    /// @return new empty builder
    public static UuidBuilder empty() {
        return new UuidBuilder();
    }

    public static UuidBuilder fromPair(long high, long low) {
        UuidBuilder builder = empty();
        builder.bytes.putLong(high);
        builder.bytes.putLong(low);
        return builder;
    }

    public static UuidBuilder fromBytes(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("UUIDs only supports 16 bytes");
        }
        UuidBuilder builder = empty();
        builder.bytes.put(bytes);
        return builder;
    }

    public static UuidBuilder fromGregorian(Timestamp ts, NodeId nodeId) {
        long ticks = ts.asGregorian();
        int timeLow = (int) (ticks & (-1L >>> 32));
        short timeMid = (short) ((ticks >>> 32) & (-1 >>> 16));
        short timeHighAndVersion = (short) (((ticks >>> 48) & (-1 >>> 20)) | (1 << 12));

        UuidBuilder builder = empty();
        builder.bytes
            .putInt(timeLow)
            .putShort(timeMid)
            .putShort(timeHighAndVersion)
            .put((byte) (((ts.counter & 0x3F00) >>> 8) | 0x80))
            .put((byte) (ts.counter & 0xFF))
            .put(nodeId.unwrap());
        return builder;
    }

    public static UuidBuilder fromMd5Bytes(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("The length of the md5-bytes array must be 16.");
        }
        UuidBuilder builder = empty();
        builder.bytes.put(bytes);
        builder
            .variant(Variant.RFC4122)
            .version(Version.MD5);
        return builder;
    }

    public static UuidBuilder fromSha1Bytes(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("The length of the sha1-bytes array must be 16.");
        }
        UuidBuilder builder = empty();
        builder.bytes.put(bytes);
        builder
            .variant(Variant.RFC4122)
            .version(Version.SHA1);
        return builder;
    }

    public static UuidBuilder fromSortedGregorian(Timestamp ts, NodeId nodeId) {
        long ticks = ts.asGregorian();
        int timeLow = (int) ((ticks >>> 28) & (-1L >>> 32));
        short timeMid = (short) ((ticks >>> 12) & (-1 >>> 16));
        short timeHighAndVersion = (short) ((ticks & (-1 >>> 20)) | (0b110 << 12));

        UuidBuilder builder = empty();
        builder.bytes
            .putInt(timeLow)
            .putShort(timeMid)
            .putShort(timeHighAndVersion)
            .put((byte) (((ts.counter & 0x3F00) >>> 8) | 0x80))
            .put((byte) (ts.counter & 0xFF))
            .put(nodeId.unwrap());
        return builder;
    }

    public static UuidBuilder fromUnixTimestampMillis(long millis, long randH, int randL) {
        int millisHigh = (int) ((millis >>> 16) & 0xFFFF_FFFFL);
        short millisLow = (short) (millis & 0xFFFF);
        long randHighWithVersionAndVariant = randH & 0x0FFF_3FFF_FFFF_FFFFL | 0x7000_8000_0000_0000L;
        UuidBuilder builder = empty();
        builder.bytes
            .putInt(millisHigh)
            .putShort(millisLow)
            .putLong(randHighWithVersionAndVariant)
            .putShort((short) randL)
        ;
        return builder;
    }

    public static UuidBuilder fromCustom(long h, long l) {
        UuidBuilder builder = empty();
        builder.bytes.putLong(h).putLong(l);
        builder
            .variant(Variant.RFC4122)
            .version(Version.CUSTOM);
        return builder;
    }

    public static UuidBuilder fromUnixTimestampMillis(long millis, byte[] bytes) {
        int millisHigh = (int) ((millis >>> 16) & 0xFFFF_FFFFL);
        short millisLow = (short) (millis & 0xFFFF);

        short withVersion = (short) ((bytes[1] & 0xFF) | (((bytes[0] & 0xFF) << 8) & 0x0FFF) | 0x7000);

        UuidBuilder builder = empty();
        builder.bytes
            .putInt(millisHigh)
            .putShort(millisLow)
            .putShort(withVersion)
            .put((byte) ((bytes[2] & 0x3F) | 0x80))
            .put(bytes, 3, bytes.length - 3)
        ;
        return builder;
    }

}

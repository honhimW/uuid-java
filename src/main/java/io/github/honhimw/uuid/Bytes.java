package io.github.honhimw.uuid;

import java.io.Serializable;

/// Byte array operation helper.
/// Without bounds check(for higher performance).
///
/// @author honhimW
/// @see java.nio.ByteBuffer something like that, but without checking.
/// @since 2025-12-10
public class Bytes implements Serializable, Comparable<Bytes> {

    private final byte[] bytes;

    private int pos;

    /// Construct new Bytes with given capacity
    /// @param capacity array length
    public Bytes(int capacity) {
        this(new byte[capacity]);
    }

    /// Construct new Bytes by given byte array
    /// @param bytes empty byte array
    public Bytes(byte[] bytes) {
        this.bytes = bytes;
        this.pos = 0;
    }

    /// Append int to current position.
    ///
    /// @param value int value
    /// @return self
    public Bytes putInt(int value) {
        this.pos = putInt(this.bytes, this.pos, value);
        return this;
    }

    /// Append long to current position.
    ///
    /// @param value long value
    /// @return self
    public Bytes putLong(long value) {
        this.pos = putLong(this.bytes, this.pos, value);
        return this;
    }

    /// Append short to current position.
    ///
    /// @param value short value
    /// @return self
    public Bytes putShort(short value) {
        this.pos = putShort(this.bytes, this.pos, value);
        return this;
    }

    /// Append byte to current position.
    ///
    /// @param value byte value
    /// @return self
    public Bytes put(byte value) {
        this.pos = put(this.bytes, this.pos, value);
        return this;
    }

    /// Append byte to target position.
    ///
    /// @param i     index
    /// @param value int value
    /// @return self
    public Bytes put(int i, byte value) {
        this.bytes[i] = value;
        return this;
    }

    /// Append byte-array to current position.
    ///
    /// @param value byte array
    /// @return self
    public Bytes put(byte[] value) {
        this.pos = put(this.bytes, this.pos, value);
        return this;
    }

    /// Append byte-array to current position.
    ///
    /// @param value  byte array
    /// @param offset value offset
    /// @param length value length after offset
    /// @return self
    public Bytes put(byte[] value, int offset, int length) {
        this.pos = put(this.bytes, this.pos, value, offset, length);
        return this;
    }

    /// Get result
    ///
    /// @return result
    public byte[] unwrap() {
        return this.bytes;
    }

    /// Get one byte by index
    ///
    /// @param i index
    /// @return single byte
    public byte get(int i) {
        return this.bytes[i];
    }

    /// Copy byte-array from offset with length
    ///
    /// @param offset start position
    /// @param length read length
    /// @return result
    public byte[] get(int offset, int length) {
        byte[] _bytes = new byte[length];
        System.arraycopy(this.bytes, offset, _bytes, 0, length);
        return _bytes;
    }

    /// Read one int value from target index.
    ///
    /// @param i index
    /// @return int value
    public int getInt(int i) {
        int v = 0;
        v |= Byte.toUnsignedInt(bytes[i]) << 24;
        v |= Byte.toUnsignedInt(bytes[i + 1]) << 16;
        v |= Byte.toUnsignedInt(bytes[i + 2]) << 8;
        v |= Byte.toUnsignedInt(bytes[i + 3]);
        return v;
    }

    /// Read one long value from target index.
    ///
    /// @param i index
    /// @return long value
    public long getLong(int i) {
        long v = 0;
        v |= Byte.toUnsignedLong(bytes[i]) << 56;
        v |= Byte.toUnsignedLong(bytes[i + 1]) << 48;
        v |= Byte.toUnsignedLong(bytes[i + 2]) << 40;
        v |= Byte.toUnsignedLong(bytes[i + 3]) << 32;
        v |= Byte.toUnsignedLong(bytes[i + 4]) << 24;
        v |= Byte.toUnsignedLong(bytes[i + 5]) << 16;
        v |= Byte.toUnsignedLong(bytes[i + 6]) << 8;
        v |= Byte.toUnsignedLong(bytes[i + 7]);
        return v;
    }

    /// Read short int value from target index.
    ///
    /// @param i index
    /// @return short value
    public short getShort(int i) {
        int v = 0;
        v |= Byte.toUnsignedInt(bytes[i]) << 8;
        v |= Byte.toUnsignedInt(bytes[i + 1]);
        return (short) v;
    }

    /// Get current position
    ///
    /// @return position
    public int position() {
        return this.pos;
    }

    /// Set current index.
    ///
    /// @param index target index
    /// @return self
    public Bytes position(int index) {
        this.pos = index;
        return this;
    }

    @Override
    public int compareTo(Bytes o) {
        int cmp = Integer.compare(this.bytes.length, o.bytes.length);
        if (cmp != 0) {
            return cmp;
        }
        int len = Math.min(this.bytes.length, o.bytes.length);
        for (int j = 0; j < len; j++) {
            cmp = Byte.compare(this.bytes[j], o.bytes[j]);
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("pos=").append(this.pos);
        sb.append(", byte-len=").append(this.bytes.length);
        sb.append(", bit-len=").append(this.bytes.length * 8);
        sb.append(", bytes=");
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /// Add int value into byte-array with index.
    ///
    /// @param dist  target
    /// @param index position
    /// @param value value
    /// @return next index
    public static int putInt(byte[] dist, int index, int value) {
        dist[index] = (byte) (value >>> 24);
        dist[index + 1] = (byte) (value >>> 16);
        dist[index + 2] = (byte) (value >>> 8);
        dist[index + 3] = (byte) value;
        return index + 4;
    }

    /// Add short value into byte-array with index.
    ///
    /// @param dist  target
    /// @param index position
    /// @param value value
    /// @return next index
    public static int putShort(byte[] dist, int index, short value) {
        dist[index] = (byte) (value >>> 8);
        dist[index + 1] = (byte) value;
        return index + 2;
    }

    /// Add long value into byte-array with index.
    ///
    /// @param dist  target
    /// @param index position
    /// @param value value
    /// @return next index
    public static int putLong(byte[] dist, int index, long value) {
        dist[index] = (byte) (value >>> 56);
        dist[index + 1] = (byte) (value >>> 48);
        dist[index + 2] = (byte) (value >>> 40);
        dist[index + 3] = (byte) (value >>> 32);
        dist[index + 4] = (byte) (value >>> 24);
        dist[index + 5] = (byte) (value >>> 16);
        dist[index + 6] = (byte) (value >>> 8);
        dist[index + 7] = (byte) value;
        return index + 8;
    }

    /// Add byte value into byte-array with index.
    ///
    /// @param dist  target
    /// @param index position
    /// @param value value
    /// @return next index
    public static int put(byte[] dist, int index, byte value) {
        dist[index] = value;
        return index + 1;
    }

    /// Add byte-array into byte-array with index.
    ///
    /// @param dist  target
    /// @param index position
    /// @param value value
    /// @return next index
    public static int put(byte[] dist, int index, byte[] value) {
        for (int i = 0; i < value.length; i++, index++) {
            dist[index] = value[i];
        }
        return index;
    }

    /// Add int value into byte-array with index.
    ///
    /// @param dist   target
    /// @param index  position
    /// @param value  source
    /// @param offset source offset
    /// @param length source length
    /// @return next index
    public static int put(byte[] dist, int index, byte[] value, int offset, int length) {
        for (int i = 0; i < length; i++, index++) {
            dist[index] = value[i + offset];
        }
        return index;
    }

    /// Copy byte-array from 0 into new array.
    ///
    /// @param src    source
    /// @param length read length
    /// @return new array
    public static byte[] copyOf(byte[] src, int length) {
        byte[] dst = new byte[length];
        System.arraycopy(src, 0, dst, 0, length);
        return dst;
    }

}

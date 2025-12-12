package io.github.honhimw.uuid;

import java.io.Serializable;

/**
 * Byte array operation helper.
 * Without bounds check.
 *
 * @author honhimW
 * @see java.nio.ByteBuffer something like that, but without checking.
 * @since 2025-12-10
 */

public class Bytes implements Serializable, Comparable<Bytes> {

    private final byte[] bytes;

    private int pos;

    public Bytes(int capacity) {
        this(new byte[capacity]);
    }

    public Bytes(byte[] bytes) {
        this.bytes = bytes;
        this.pos = 0;
    }

    public Bytes putInt(int value) {
        this.pos = putInt(this.bytes, this.pos, value);
        return this;
    }

    public Bytes putLong(long value) {
        this.pos = putLong(this.bytes, this.pos, value);
        return this;
    }

    public Bytes putShort(short value) {
        this.pos = putShort(this.bytes, this.pos, value);
        return this;
    }

    public Bytes put(byte value) {
        this.pos = put(this.bytes, this.pos, value);
        return this;
    }

    public Bytes put(int i, byte value) {
        this.bytes[i] = value;
        return this;
    }

    public Bytes put(byte[] value) {
        this.pos = put(this.bytes, this.pos, value);
        return this;
    }

    public Bytes put(byte[] value, int offset, int length) {
        this.pos = put(this.bytes, this.pos, value, offset, length);
        return this;
    }

    public byte[] unwrap() {
        return this.bytes;
    }

    public byte get(int i) {
        return this.bytes[i];
    }

    public byte[] get(int offset, int length) {
        byte[] _bytes = new byte[length];
        System.arraycopy(this.bytes, offset, _bytes, 0, length);
        return _bytes;
    }

    public int getInt(int i) {
        int v = 0;
        v |= Byte.toUnsignedInt(bytes[i]) << 24;
        v |= Byte.toUnsignedInt(bytes[i + 1]) << 16;
        v |= Byte.toUnsignedInt(bytes[i + 2]) << 8;
        v |= Byte.toUnsignedInt(bytes[i + 3]);
        return v;
    }

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

    public short getShort(int i) {
        int v = 0;
        v |= Byte.toUnsignedInt(bytes[i]) << 8;
        v |= Byte.toUnsignedInt(bytes[i + 1]);
        return (short) v;
    }

    public int position() {
        return this.pos;
    }

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

    public static int putInt(byte[] dist, int index, int value) {
        dist[index] = (byte) (value >> 24);
        dist[index + 1] = (byte) (value >> 16);
        dist[index + 2] = (byte) (value >> 8);
        dist[index + 3] = (byte) value;
        return index + 4;
    }

    public static int putShort(byte[] dist, int index, short value) {
        dist[index] = (byte) (value >> 8);
        dist[index + 1] = (byte) value;
        return index + 2;
    }

    public static int putLong(byte[] dist, int index, long value) {
        dist[index] = (byte) (value >> 56);
        dist[index + 1] = (byte) (value >> 48);
        dist[index + 2] = (byte) (value >> 40);
        dist[index + 3] = (byte) (value >> 32);
        dist[index + 4] = (byte) (value >> 24);
        dist[index + 5] = (byte) (value >> 16);
        dist[index + 6] = (byte) (value >> 8);
        dist[index + 7] = (byte) value;
        return index + 8;
    }

    public static int put(byte[] dist, int index, byte value) {
        dist[index] = value;
        return index + 1;
    }

    public static int put(byte[] dist, int index, byte[] value) {
        for (byte b : value) {
            dist[index] = b;
            index += 1;
        }
        return index;
    }

    public static int put(byte[] dist, int index, byte[] value, int offset, int length) {
        for (int i = 0; i < length; i++) {
            dist[index] = value[i + offset];
            index += 1;
        }
        return index;
    }

    public static byte[] copyOf(byte[] src, int length) {
        byte[] dst = new byte[length];
        System.arraycopy(src, 0, dst, 0, length);
        return dst;
    }

}

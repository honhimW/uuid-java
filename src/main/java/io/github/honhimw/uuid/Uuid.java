package io.github.honhimw.uuid;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/// Feature full UUID. Compatible with all versions.
/// [UUID#timestamp()], [UUID#clockSequence()] ()}, [UUID#node()] are only support UUIDv1.
///
/// @author honhimW
/// @see UUID
/// @since 2025-12-09
public class Uuid implements Serializable, Comparable<Uuid> {

    /// Create new Uuid from JDK UUID
    ///
    /// @param uuid uuid
    /// @return Uuid
    public static Uuid fromUUID(UUID uuid) {
        return fromPair(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    /// Create new Uuid from byte array
    ///
    /// @param bytes 16-bytes array
    /// @return Uuid
    public static Uuid fromBytes(byte[] bytes) {
        Bytes bb = new Bytes(16);
        bb.put(bytes);
        return new Uuid(bb);
    }

    /// Create new Uuid from long values pair
    ///
    /// @param m high bytes
    /// @param l low bytes
    /// @return Uuid
    public static Uuid fromPair(long m, long l) {
        Bytes bb = new Bytes(16);
        bb.putLong(m);
        bb.putLong(l);
        return new Uuid(bb);
    }

    /// Create new Uuid from uuid string value
    ///
    /// @param s formatted uuid string
    /// @return Uuid
    public static Uuid fromString(String s) {
        return fromUUID(UUID.fromString(s));
    }

    private final Bytes bb;

    private Uuid(Bytes bb) {
        this.bb = bb;
    }

    /// The variant number of current Uuid
    ///
    /// @return Variant enum
    public Variant variant() {
        return Variant.of(bb.get(8));
    }

    /// The version number of current Uuid
    ///
    /// @return Version enum
    public Version version() {
        return Version.of(bb.get(6));
    }

    /// The timestamp of current Uuid
    ///
    /// Only [Version#MAC] and [Version#SORT_MAC] and [Version#SORT_RANDOM] is available
    ///
    /// @return optional timestamp of current Uuid
    public Optional<Timestamp> timestamp() {
        Timestamp timestamp = null;
        switch (version()) {
            case MAC: {
                long ticks = (Byte.toUnsignedLong(bb.get(6)) & 0x0F) << 56;
                ticks |= (Byte.toUnsignedLong(bb.get(7))) << 48;
                ticks |= (Byte.toUnsignedLong(bb.get(4))) << 40;
                ticks |= (Byte.toUnsignedLong(bb.get(5))) << 32;
                ticks |= (Byte.toUnsignedLong(bb.get(0))) << 24;
                ticks |= (Byte.toUnsignedLong(bb.get(1))) << 16;
                ticks |= (Byte.toUnsignedLong(bb.get(2))) << 8;
                ticks |= Byte.toUnsignedLong(bb.get(3));

                long counter = (bb.get(8) & 0x3F) << 8;
                counter |= Byte.toUnsignedLong(bb.get(9));

                timestamp = Timestamp.fromGregorian(ticks, counter);
                break;
            }
            case SORT_MAC: {
                long ticks = (Byte.toUnsignedLong(bb.get(0))) << 52;
                ticks |= (Byte.toUnsignedLong(bb.get(1))) << 44;
                ticks |= (Byte.toUnsignedLong(bb.get(2))) << 36;
                ticks |= (Byte.toUnsignedLong(bb.get(3))) << 28;
                ticks |= (Byte.toUnsignedLong(bb.get(4))) << 20;
                ticks |= (Byte.toUnsignedLong(bb.get(5))) << 12;
                ticks |= (Byte.toUnsignedLong(bb.get(6)) & 0xF) << 8;
                ticks |= Byte.toUnsignedLong(bb.get(7));

                long counter = (Byte.toUnsignedLong(bb.get(8)) & 0x3F) << 8;
                counter |= Byte.toUnsignedLong(bb.get(9));

                timestamp = Timestamp.fromGregorian(ticks, counter);
                break;
            }
            case SORT_RANDOM: {
                UUIDs.FAST.V7.resolveTimestamp(asUUID());
                long millis = (Byte.toUnsignedLong(bb.get(0)) & 0x0F) << 40;
                millis |= (Byte.toUnsignedLong(bb.get(1))) << 32;
                millis |= (Byte.toUnsignedLong(bb.get(2))) << 24;
                millis |= (Byte.toUnsignedLong(bb.get(3))) << 16;
                millis |= (Byte.toUnsignedLong(bb.get(4))) << 8;
                millis |= Byte.toUnsignedLong(bb.get(5));

                long seconds = Maths.div1000(millis);
                int nanos = (int) (millis - (seconds * Maths.ONE_THOUSAND)) * 1_000_000;

                long counter = (Byte.toUnsignedLong(bb.get(6)) & 0xF) << 38;
                counter |= (Byte.toUnsignedLong(bb.get(7))) << 30;
                counter |= (Byte.toUnsignedLong(bb.get(8)) & 0x3F) << 24;
                counter |= (Byte.toUnsignedLong(bb.get(9))) << 16;
                counter |= (Byte.toUnsignedLong(bb.get(10))) << 8;
                counter |= (Byte.toUnsignedLong(bb.get(11)));

                timestamp = new Timestamp(seconds, nanos, counter, 42);
                break;
            }
        }
        return Optional.ofNullable(timestamp);
    }

    /// The node-id of current Uuid
    ///
    /// Only [Version#MAC] and [Version#SORT_MAC] is available
    ///
    /// @return optional node-id of current Uuid
    public Optional<NodeId> node() {
        switch (version()) {
            case MAC:
            case SORT_MAC:
                byte[] nodeId = bb.get(10, 6);
                return Optional.of(NodeId.of(nodeId));
        }
        return Optional.empty();
    }

    /// Convert current Uuid as JDK UUID
    ///
    /// @return JDK UUID
    public UUID asUUID() {
        return new UUID(bb.getLong(0), bb.getLong(Long.BYTES));
    }

    /// Convert current Uuid as 16-bytes array
    ///
    /// @return 16-bytes array
    public byte[] asBytes() {
        return bb.unwrap();
    }

    /// Convert current Uuid as UUID-formatted string
    ///
    /// @return UUID-formatted string
    public String asString() {
        return asUUID().toString();
    }

    /// Convert current Uuid as UUID-formatted string
    ///
    /// @return UUID-formatted string
    @Override
    public String toString() {
        return asString();
    }

    @Override
    public int compareTo(Uuid other) {
        return this.bb.compareTo(other.bb);
    }
}

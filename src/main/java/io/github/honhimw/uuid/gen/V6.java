package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.*;

import java.util.UUID;

/// UUID [Version 6](https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-6) generator, aka Reordered-Time-Based UUID.
///
/// ```text
///  0                   1                   2                   3
///  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                           time_high                           |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |           time_mid            |  ver  |       time_low        |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |var|         clock_seq         |             node              |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                              node                             |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// ```
///
/// - time_high: The most significant 32 bits of the 60-bit starting timestamp. Occupies bits 0 through 31 (octets 0-3).
/// - time_mid: The middle 16 bits of the 60-bit starting timestamp. Occupies bits 32 through 47 (octets 4-5).
/// - ver: The 4-bit version field as defined by Section 4.2, set to 0b0110 (6). Occupies bits 48 through 51 of octet 6.
/// - time_low: 12 bits that will contain the least significant 12 bits from the 60-bit starting timestamp. Occupies bits 52 through 63 (octets 6-7).
/// - var: The 2-bit variant field as defined by Section 4.1, set to 0b10. Occupies bits 64 and 65 of octet 8.
/// - clock_seq: The 14 bits containing the clock sequence. Occupies bits 66 through 79 (octets 8-9).
/// - node: 48-bit spatially unique identifier. Occupies bits 80 through 127 (octets 10-15).
///
/// @author honhimW
/// @see io.github.honhimw.uuid.Version#SORT_MAC
/// @since 2025-12-09
public class V6 extends AbstractGenerator implements Generator.TimeBased {

    public V6() {
    }

    public V6(Context context) {
        super(context);
    }

    @Override
    public UUID next() {
        return now(_ctx.node.get());
    }

    @Override
    public UUID now(NodeId nodeId) {
        return of(Timestamp.now(_ctx.clockSequence), nodeId);
    }

    @Override
    public UUID of(Timestamp ts) {
        return of(ts, _ctx.node.get());
    }

    /// Generate time-based UUID with timestamp and node-id
    ///
    /// @param ts     UUID timestamp
    /// @param nodeId node-id
    /// @return UUIDv6
    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

}

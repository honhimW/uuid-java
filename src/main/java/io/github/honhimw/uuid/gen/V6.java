package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.NodeId;
import io.github.honhimw.uuid.Timestamp;
import io.github.honhimw.uuid.UuidBuilder;

import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-6">Version 6</a>
 * UUIDv6 generator.
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#SORT_MAC
 * @since 2025-12-09
 */

public class V6 extends AbstractGenerator {

    public V6() {
    }

    public V6(Context context) {
        super(context);
    }

    @Override
    public UUID next() {
        return now(_ctx.node.get());
    }

    /**
     * Generate UUID with node-id
     *
     * @param nodeId 6-bits node id
     * @return UUIDv6
     */
    public UUID now(NodeId nodeId) {
        Timestamp ts = Timestamp.now(_ctx.clockSequence);
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

    /**
     * Generate UUIDv6 with timestamp and node-id
     *
     * @param ts     UUIDv6 timestamp
     * @param nodeId node-id
     * @return UUIDv6
     */
    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

}

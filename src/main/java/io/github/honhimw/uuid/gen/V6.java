package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.*;

import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-6">Version 6</a>
 * UUIDv6 generator.
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#SORT_MAC
 * @since 2025-12-09
 */

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

    /**
     * Generate time-based UUID with timestamp and node-id
     *
     * @param ts     UUID timestamp
     * @param nodeId node-id
     * @return UUIDv6
     */
    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

}

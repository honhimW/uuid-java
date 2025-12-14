package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.NodeId;
import io.github.honhimw.uuid.Timestamp;
import io.github.honhimw.uuid.UuidBuilder;

import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-1">Version 1</a>
 * UUIDv1 generator
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#MAC
 * @since 2025-12-08
 */

public class V1 extends AbstractGenerator {

    public V1() {
    }

    public V1(Context context) {
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
     * @return UUIDv1
     */
    public UUID now(NodeId nodeId) {
        Timestamp ts = Timestamp.now(_ctx.clockSequence);
        return of(ts, nodeId);
    }

    /**
     * Generate UUIDv1 with timestamp and node-id
     *
     * @param ts     UUIDv1 timestamp
     * @param nodeId node-id
     * @return UUIDv1
     */
    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromGregorian(ts, nodeId).build();
    }

}

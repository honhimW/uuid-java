package io.github.honhimw.uuid.variant;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.NodeId;
import io.github.honhimw.uuid.Timestamp;
import io.github.honhimw.uuid.UuidBuilder;

import java.util.UUID;

/**
 * @author honhimW
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

    public UUID now(NodeId nodeId) {
        Timestamp ts = Timestamp.now(_ctx.clockSequence);
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromSortedGregorian(ts, nodeId).build();
    }

}

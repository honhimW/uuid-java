package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.NodeId;
import io.github.honhimw.uuid.Timestamp;
import io.github.honhimw.uuid.UuidBuilder;

import java.util.UUID;

/**
 * @author honhimW
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

    public UUID now(NodeId nodeId) {
        Timestamp ts = Timestamp.now(_ctx.clockSequence);
        return of(ts, nodeId);
    }

    public static UUID of(Timestamp ts, NodeId nodeId) {
        return UuidBuilder.fromGregorian(ts, nodeId).build();
    }

}

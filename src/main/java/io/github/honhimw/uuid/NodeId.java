package io.github.honhimw.uuid;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable
 *
 * @author honhimW
 * @since 2025-12-08
 */

public class NodeId {

    private final byte[] nodeId;

    public static NodeId of(byte[] nodeId) {
        byte[] _bytes = new byte[6];
        int len = Math.min(nodeId.length, _bytes.length);
        System.arraycopy(nodeId, 0, _bytes, 0, len);
        return new NodeId(_bytes);
    }

    private NodeId(byte[] nodeId) {
        this.nodeId = nodeId;
    }

    public byte[] unwrap() {
        return nodeId;
    }

    public byte get(int index) {
        return nodeId[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : nodeId) {
            if (sb.length() != 0) {
                sb.append(':');
            }
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId1 = (NodeId) o;
        return Objects.deepEquals(nodeId, nodeId1.nodeId);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodeId);
    }
}

package io.github.honhimw.uuid;

import org.jspecify.annotations.Nullable;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Mac address resolver, using mac address as node-id by default in UUID-v1 and UUID-v6.
 *
 * @author honhimW
 * @since 2025-12-09
 */

public class MacAddress {

    private static volatile byte @Nullable [] MAC_ADDRESS;

    /**
     * Get first mac address as NodeId
     * @return mac address
     */
    public static NodeId nodeId() {
        return NodeId.of(tryGetFirst());
    }

    /**
     * Get first hardware address via network interface
     * @return 6-bytes hardware address
     */
    public static byte[] tryGetFirst() {
        if (MAC_ADDRESS == null) {
            synchronized (MacAddress.class) {
                if (MAC_ADDRESS == null) {
                    try {
                        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                        while (netInterfaces.hasMoreElements()) {
                            NetworkInterface ni = netInterfaces.nextElement();
                            byte[] hardwareAddress = ni.getHardwareAddress();
                            if (hardwareAddress != null) {
                                MAC_ADDRESS = hardwareAddress;
                            }
                        }
                    } catch (SocketException ignored) {
                    }
                }
                if (MAC_ADDRESS == null) {
                    MAC_ADDRESS = new byte[6];
                }
            }
        }
        return Objects.requireNonNull(MAC_ADDRESS);
    }

    /**
     * Setter for MAC_ADDRESS
     * @see #MAC_ADDRESS
     * @param macAddress mac address
     */
    public static void prefer(byte[] macAddress) {
        if (macAddress.length != 6) {
            throw new IllegalArgumentException("Mac address length must be 6 bytes");
        }
        MAC_ADDRESS = Bytes.copyOf(macAddress, 6);
    }

}

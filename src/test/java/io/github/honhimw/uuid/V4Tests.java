package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

/// @author honhimW
/// @since 2025-12-08
public class V4Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V4.next();
        Assertions.assertEquals(4, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    /// [Example of a UUIDv4 Value](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv4-value)
    /// ```text
    /// -------------------------------------------
    /// field     bits value
    /// -------------------------------------------
    /// random_a  48   0x919108f752d1
    /// ver        4   0x4
    /// random_b  12   0x320
    /// var        2   0b10
    /// random_c  62   0b01, 0xbacf847db4148a8
    /// -------------------------------------------
    /// total     128
    /// -------------------------------------------
    /// final: 919108f7-52d1-4320-9bac-f847db4148a8
    ///
    /// Random hex:            919108f752d133205bacf847db4148a8
    /// Random hex and dash:   919108f7-52d1-3320-5bac-f847db4148a8
    /// Ver and Var Overwrite: xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx
    /// Final:                 919108f7-52d1-4320-9bac-f847db4148a8
    /// ```
    @Test
    @SneakyThrows
    void example() {
        long h = 0x919108f752d13320L;
        long l = 0x5bacf847db4148a8L;
        UUID uuid = UuidBuilder.fromPair(h, l).version(Version.RANDOM).variant(Variant.RFC4122).build();
        Assertions.assertEquals("919108f7-52d1-4320-9bac-f847db4148a8", uuid.toString());
        Assertions.assertEquals(4, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

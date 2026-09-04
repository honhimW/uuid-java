package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V1;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

/// @author honhimW
/// @since 2025-12-08
public class V1Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V1.next();
        Assertions.assertEquals(1, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        long seconds = 1_496_854_535L;
        int nanos = 812_946_000;
        Timestamp timestamp = Timestamp.of(new CounterSequence(0), Instant.ofEpochSecond(seconds, nanos));
        NodeId nodeId = NodeId.of(new byte[]{1, 2, 3, 4, 5, 6});
        UUID uuid = V1.of(timestamp, nodeId);
        Assertions.assertEquals("20616934-4ba2-11e7-8000-010203040506", uuid.toString());
        Assertions.assertEquals(1, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void context() {
        CounterSequence counterSequence = new CounterSequence(-1);
        {
            long i = counterSequence.generateSequence(0, 0);
            Assertions.assertEquals(16383, i);
        }
        {
            long i = counterSequence.generateSequence(0, 0);
            Assertions.assertEquals(0, i);
        }
    }

    /// [Example of a UUIDv1 Value](https://www.rfc-editor.org/rfc/rfc9562.html#name-example-of-a-uuidv1-value)
    /// ```text
    /// -------------------------------------------
    /// field      bits value
    /// -------------------------------------------
    /// time_low   32   0xC232AB00
    /// time_mid   16   0x9414
    /// ver         4   0x1
    /// time_high  12   0x1EC
    /// var         2   0b10
    /// clock_seq  14   0b11, 0x3C8
    /// node       48   0x9F6BDECED846
    /// -------------------------------------------
    /// total      128
    /// -------------------------------------------
    /// final: C232AB00-9414-11EC-B3C8-9F6BDECED846
    /// ```
    @Test
    @SneakyThrows
    void example() {
        long ticks = 0x1EC9414C232AB00L;
        long counter = 0x33C8;
        Timestamp timestamp = Timestamp.fromGregorian(ticks, counter);
        NodeId nodeId = NodeId.of(new byte[]{(byte) 0x9F, 0x6B, (byte) 0xDE, (byte) 0xCE, (byte) 0xD8, 0x46});
        UUID uuid = V1.of(timestamp, nodeId);
        Assertions.assertEquals("c232ab00-9414-11ec-b3c8-9f6bdeced846", uuid.toString());
        Assertions.assertEquals(1, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

}

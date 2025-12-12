package io.github.honhimw.uuid;

import io.github.honhimw.uuid.variant.V1;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public class V1Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.v1().next();
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

}

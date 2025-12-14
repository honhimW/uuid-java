package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V6;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public class V6Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V6.next();
        Assertions.assertEquals(6, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        long seconds = 1_496_854_535L;
        int nanos = 812_946_000;
        Timestamp timestamp = Timestamp.of(new CounterSequence(0), Instant.ofEpochSecond(seconds, nanos));
        NodeId nodeId = NodeId.of(new byte[]{1, 2, 3, 4, 5, 6});
        UUID uuid = V6.of(timestamp, nodeId);
        Assertions.assertEquals("1e74ba22-0616-6934-8000-010203040506", uuid.toString());
        Assertions.assertEquals(6, uuid.version());
        Assertions.assertEquals(2, uuid.variant());

        Optional<Timestamp> optional = Timestamp.of(uuid);
        Assertions.assertTrue(optional.isPresent());
        Timestamp ts = optional.get();
        Assertions.assertEquals(14_968_545_358_129_460L, ts.asGregorian() - 0x01B2_1DD2_1381_4000L);
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

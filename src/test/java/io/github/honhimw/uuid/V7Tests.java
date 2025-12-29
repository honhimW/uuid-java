package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.V7;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/// @author honhimW
/// @since 2025-12-08
public class V7Tests {

    @Test
    @SneakyThrows
    void random() {
        UUID uuid = UUIDs.FAST.V7.next();
        Assertions.assertEquals(7, uuid.version());
        Assertions.assertEquals(2, uuid.variant());
    }

    @Test
    @SneakyThrows
    void testNew() {
        long millis = 1645557742000L;
        Instant instant = Instant.ofEpochMilli(millis);
        long seconds = instant.getEpochSecond();
        int nanos = instant.getNano();
        V7.ClockSequenceV7 clock = new V7.ClockSequenceV7();
        V7 v7 = new V7(Context.builder().clock(clock).build());
        Timestamp timestamp = Timestamp.of(clock, Instant.ofEpochSecond(seconds, nanos));
        UUID uuid = v7.of(timestamp);
        Assertions.assertTrue(uuid.toString().startsWith("017f22e2-79b0-7"), uuid.toString());
        Assertions.assertEquals(7, uuid.version());
        Assertions.assertEquals(2, uuid.variant());

        Optional<Timestamp> optional = Timestamp.of(uuid);
        Assertions.assertTrue(optional.isPresent());
        Timestamp ts = optional.get();
        Assertions.assertEquals(millis, ts.asInstant().toEpochMilli());

    }

    @Test
    @SneakyThrows
    void additionPrecision() {
        V7.ClockSequenceV7 clockSequence = new V7.ClockSequenceV7().withAdditionalPrecision();
        V7 v7 = new V7(Context.builder().clock(clockSequence).build());
        Timestamp now = Timestamp.now(clockSequence);
        UUID uuid = v7.of(now);
        Timestamp resolve = v7.resolveTimestamp(uuid);
        Assertions.assertEquals(now.seconds, resolve.seconds);
        Assertions.assertEquals(now.counter, resolve.counter);
        Assertions.assertEquals(now.usableCounterBits, resolve.usableCounterBits);
    }

    @Test
    @SneakyThrows
    void precision20() {
        V7.ClockSequenceV7 clockSequence = new V7.ClockSequenceV7().withAdditionalPrecision(20);
        V7 v7 = new V7(Context.builder().clock(clockSequence).build());
        Timestamp now = Timestamp.now(clockSequence);
        UUID uuid = v7.of(now);

        Timestamp resolved = v7.resolveTimestamp(uuid);
        Assertions.assertEquals(now, resolved);
    }

}

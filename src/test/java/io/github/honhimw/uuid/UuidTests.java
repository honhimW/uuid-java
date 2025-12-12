package io.github.honhimw.uuid;

import com.fasterxml.uuid.Generators;
import io.github.honhimw.uuid.variant.V1;
import io.github.honhimw.uuid.variant.V6;
import io.github.honhimw.uuid.variant.V7;
import io.github.robsonkades.uuidv7.UUIDv7;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-09
 */

public class UuidTests {

    @Test
    @SneakyThrows
    void uuid() {
        NodeId nodeId = MacAddress.nodeId();
        CounterSequence shared = CounterSequence.SHARED;
        Timestamp now = Timestamp.now(shared);
        {
            UUID _uuid = V1.of(now, nodeId);
            Uuid uuid = Uuid.fromUUID(_uuid);
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.MAC, uuid.version());
            Optional<Timestamp> timestamp = uuid.timestamp();
            Assertions.assertTrue(timestamp.isPresent());
            Timestamp ts = timestamp.get();
            Assertions.assertEquals(now.seconds, ts.seconds);
            Assertions.assertEquals(now.nanos / 100, ts.seconds / 100); // precision issue on linux
            Assertions.assertEquals(now.counter, ts.counter);
            Assertions.assertEquals(now.usableCounterBits, ts.usableCounterBits);
            Optional<NodeId> node = uuid.node();
            Assertions.assertTrue(node.isPresent());
            Assertions.assertEquals(nodeId, node.get());
        }
        {
            UUID _uuid = V6.of(now, nodeId);
            Uuid uuid = Uuid.fromUUID(_uuid);
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.SORT_MAC, uuid.version());
            Optional<Timestamp> timestamp = uuid.timestamp();
            Assertions.assertTrue(timestamp.isPresent());
            Timestamp ts = timestamp.get();
            Assertions.assertEquals(now.seconds, ts.seconds);
            Assertions.assertEquals(now.nanos / 100, ts.seconds / 100); // precision issue on linux
            Assertions.assertEquals(now.counter, ts.counter);
            Assertions.assertEquals(now.usableCounterBits, ts.usableCounterBits);
            Optional<NodeId> node = uuid.node();
            Assertions.assertTrue(node.isPresent());
            Assertions.assertEquals(nodeId, node.get());
        }
        {
            Context context = Context.builder().clock(new V7.ClockSequenceV7()).build();
            Timestamp _now = Timestamp.now(context.clockSequence);
            UUID _uuid = new V7(context).of(_now);
            Uuid uuid = Uuid.fromUUID(_uuid);
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.SORT_RANDOM, uuid.version());
            Optional<Timestamp> timestamp = uuid.timestamp();
            Assertions.assertTrue(timestamp.isPresent());
            Timestamp ts = timestamp.get();
            Assertions.assertEquals(_now.seconds, ts.seconds);
            Assertions.assertEquals(_now.nanos, ts.nanos);
            Optional<NodeId> node = uuid.node();
            Assertions.assertFalse(node.isPresent());
        }
    }

    @Test
    @SneakyThrows
    void resolve() {
        UUID _uuid = UUIDv7.randomUUID();
        Uuid uuid = Uuid.fromUUID(_uuid);
        Assertions.assertEquals(Variant.RFC4122, uuid.variant());
        Assertions.assertEquals(Version.SORT_RANDOM, uuid.version());
        Assertions.assertTrue(uuid.timestamp().isPresent());
    }

    @Test
    @SneakyThrows
    void resolveFasterxml() {
        {
            Uuid uuid = Uuid.fromUUID(Generators.timeBasedGenerator().generate());
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.MAC, uuid.version());
            Assertions.assertTrue(uuid.timestamp().isPresent());
        }
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Uuid uuid = Uuid.fromUUID(Generators.nameBasedGenerator(UUIDs.NAMESPACE_DNS, md5).generate("foo"));
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.MD5, uuid.version());
            Assertions.assertFalse(uuid.timestamp().isPresent());
        }
        {
            Uuid uuid = Uuid.fromUUID(Generators.randomBasedGenerator().generate());
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.RANDOM, uuid.version());
            Assertions.assertFalse(uuid.timestamp().isPresent());
        }
        {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            Uuid uuid = Uuid.fromUUID(Generators.nameBasedGenerator(UUIDs.NAMESPACE_DNS, sha1).generate("foo"));
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.SHA1, uuid.version());
            Assertions.assertFalse(uuid.timestamp().isPresent());
        }
        {
            Uuid uuid = Uuid.fromUUID(Generators.timeBasedReorderedGenerator().generate());
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.SORT_MAC, uuid.version());
            Assertions.assertTrue(uuid.timestamp().isPresent());
        }
        {
            Uuid uuid = Uuid.fromUUID(Generators.timeBasedEpochRandomGenerator().generate());
            Assertions.assertEquals(Variant.RFC4122, uuid.variant());
            Assertions.assertEquals(Version.SORT_RANDOM, uuid.version());
            Assertions.assertTrue(uuid.timestamp().isPresent());
        }
    }

    @Test
    @SneakyThrows
    void namespace() {
        {
            int[] bytes = {
                0x6b, 0xa7, 0xb8, 0x10, 0x9d, 0xad, 0x11, 0xd1, 0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8,
            };
            Assertions.assertArrayEquals(toByteArray(bytes), UUIDs.toBytes(UUIDs.NAMESPACE_DNS));
        }
        {
            int[] bytes = {
                0x6b, 0xa7, 0xb8, 0x12, 0x9d, 0xad, 0x11, 0xd1, 0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8,
            };
            Assertions.assertArrayEquals(toByteArray(bytes), UUIDs.toBytes(UUIDs.NAMESPACE_OID));
        }
        {
            int[] bytes = {
                0x6b, 0xa7, 0xb8, 0x11, 0x9d, 0xad, 0x11, 0xd1, 0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8,
            };
            Assertions.assertArrayEquals(toByteArray(bytes), UUIDs.toBytes(UUIDs.NAMESPACE_URL));
        }
        {
            int[] bytes = {
                0x6b, 0xa7, 0xb8, 0x14, 0x9d, 0xad, 0x11, 0xd1, 0x80, 0xb4, 0x00, 0xc0, 0x4f, 0xd4, 0x30, 0xc8,
            };
            Assertions.assertArrayEquals(toByteArray(bytes), UUIDs.toBytes(UUIDs.NAMESPACE_X500));
        }
    }

    byte[] toByteArray(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

}

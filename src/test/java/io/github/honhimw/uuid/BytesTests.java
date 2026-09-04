package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/// @author honhimW
/// @since 2025-12-12
public class BytesTests {

    @Test
    @SneakyThrows
    void testToString() {
        Bytes bytes = new Bytes(20);
        bytes.putLong(Long.MAX_VALUE);
        bytes.putLong(4L);
        bytes.putInt(2);
        Assertions.assertEquals("pos=20, byte-len=20, bit-len=160, bytes=7FFFFFFFFFFFFFFF000000000000000400000002", bytes.toString());
    }

    @Test
    @SneakyThrows
    void _long() {
        Bytes bytes = new Bytes(16);
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        long l = random.nextLong();
        long l1 = random.nextLong();

        bytes.putLong(l).putLong(l1);
        byteBuffer.putLong(l).putLong(l1);

        Assertions.assertEquals(16, bytes.position());
        Assertions.assertEquals(16, byteBuffer.position());
        Assertions.assertArrayEquals(bytes.unwrap(), byteBuffer.array());
        Assertions.assertEquals(bytes.getLong(0), byteBuffer.getLong(0));
        Assertions.assertEquals(bytes.getLong(4), byteBuffer.getLong(4));
    }

    @Test
    @SneakyThrows
    void shiftRight() {
        Bytes bytes = new Bytes(16);
        bytes.putLong(-1);

        bytes
            .partialShiftRight(48, 12, 4)
            .partialShiftRight(64, 4, 2);

        Assertions.assertEquals("ffffffff-ffff-0fff-3c00-000000000000", new UUID(bytes.getLong(0), bytes.getLong(Long.BYTES)).toString());
    }

    @Test
    @SneakyThrows
    void shiftLeft() {
        Bytes bytes = new Bytes(16);
        bytes.putLong(0).putLong(-1);

        bytes
            .partialShiftLeft(72, 40, 32);

        Assertions.assertEquals("00000000-00ff-ffff-ffff-00000000ffff", Uuid.fromBytes(bytes.unwrap()).toString());

        bytes
            .partialShiftRight(48, 12, 4);

        Assertions.assertEquals("00000000-00ff-0fff-ffff-00000000ffff", new UUID(bytes.getLong(0), bytes.getLong(Long.BYTES)).toString());
    }

}

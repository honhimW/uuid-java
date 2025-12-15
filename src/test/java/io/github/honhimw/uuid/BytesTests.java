package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
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

}

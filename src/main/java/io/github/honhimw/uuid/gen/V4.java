package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;

import java.util.Random;
import java.util.UUID;

/// [Version 4](https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-4)
/// ```text
///  0                   1                   2                   3
///  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                           random_a                            |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |          random_a             |  ver  |       random_b        |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |var|                       random_c                            |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                           random_c                            |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// ```
///
/// - random_a: The first 48 bits of the layout that can be filled with random data as specified in Section 6.9. Occupies bits 0 through 47 (octets 0-5).
/// - ver: The 4-bit version field as defined by Section 4.2, set to 0b0100 (4). Occupies bits 48 through 51 of octet 6.
/// - random_b: 12 more bits of the layout that can be filled random data as per Section 6.9. Occupies bits 52 through 63 (octets 6-7).
/// - var: The 2-bit variant field as defined by Section 4.1, set to 0b10. Occupies bits 64 and 65 of octet 8.
/// - random_c: The final 62 bits of the layout immediately following the var field to be filled with random data as per Section 6.9. Occupies bits 66 through 127 (octets 8-15).
///
/// @author honhimW
/// @see io.github.honhimw.uuid.Version#RANDOM
/// @since 2025-12-09
public class V4 extends AbstractGenerator {

    public V4() {
        super();
    }

    public V4(Context context) {
        super(context);
    }

    @Override
    public UUID next() {
        Random random = _ctx.random.get();

        long h = random.nextLong();
        long l = random.nextLong();

        h &= ~0xF000L;
        h |= 4 << 12;

        l = (l << 2) >>> 2;
        l |= (2L << 62);

        return new UUID(h, l);
    }
}

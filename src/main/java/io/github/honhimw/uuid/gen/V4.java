package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;

import java.util.Random;
import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-4">Version 4</a>
 * UUIDv4 generator.
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#RANDOM
 * @since 2025-12-09
 */

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

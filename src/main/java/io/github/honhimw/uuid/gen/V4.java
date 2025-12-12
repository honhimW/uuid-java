package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;

import java.util.Random;
import java.util.UUID;

/**
 * @author honhimW
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

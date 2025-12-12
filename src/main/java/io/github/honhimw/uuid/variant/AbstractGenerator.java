package io.github.honhimw.uuid.variant;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.Generator;

/**
 * @author honhimW
 * @since 2025-12-08
 */

public abstract class AbstractGenerator implements Generator {

    protected final Context _ctx;

    protected AbstractGenerator() {
        this(Context.builder().build());
    }

    protected AbstractGenerator(Context context) {
        this._ctx = context;
    }

}

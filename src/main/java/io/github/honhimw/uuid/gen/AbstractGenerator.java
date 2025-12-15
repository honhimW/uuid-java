package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.Generator;

/// generator with context
///
/// @author honhimW
/// @since ${YEAR}-${MONTH}-${DAY}
public abstract class AbstractGenerator implements Generator {

    /// Generator context
    protected final Context _ctx;

    /// Construct with default context
    protected AbstractGenerator() {
        this(Context.builder().build());
    }

    /// Construct with given context
    /// @param context given context
    protected AbstractGenerator(Context context) {
        this._ctx = context;
    }

}

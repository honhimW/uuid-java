package io.github.honhimw.uuid;

import io.github.honhimw.uuid.variant.*;

/**
 * @author honhimW
 * @since 2025-12-12
 */
public final class Generators {
    private final V1 v1;
    private final V3 v3;
    private final V4 v4;
    private final V5 v5;
    private final V6 v6;
    private final V7 v7;

    public Generators(V1 v1, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        this.v1 = v1;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
    }

    public V1 v1() {
        return v1;
    }

    public V3 v3() {
        return v3;
    }

    public V4 v4() {
        return v4;
    }

    public V5 v5() {
        return v5;
    }

    public V6 v6() {
        return v6;
    }

    public V7 v7() {
        return v7;
    }

}

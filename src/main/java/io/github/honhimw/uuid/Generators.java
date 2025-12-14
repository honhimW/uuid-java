package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.*;

/**
 * UUID generator holder.
 *
 * @author honhimW
 * @see UUIDs#FAST faster preset
 * @see UUIDs#SECURE secure preset
 * @since 2025-12-12
 */
public final class Generators {
    public final V1 V1;
    public final V3 V3;
    public final V4 V4;
    public final V5 V5;
    public final V6 V6;
    public final V7 V7;

    public Generators(V1 v1, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        this.V1 = v1;
        this.V3 = v3;
        this.V4 = v4;
        this.V5 = v5;
        this.V6 = v6;
        this.V7 = v7;
    }

}

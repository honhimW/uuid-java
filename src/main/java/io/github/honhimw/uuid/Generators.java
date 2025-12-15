package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.*;

/// UUID generator holder.
///
/// @author honhimW
/// @see UUIDs#FAST faster preset
/// @see UUIDs#SECURE secure preset
/// @since 2025-12-12
public final class Generators {
    /// UUIDv1 generator
    public final V1 V1;
    /// UUIDv3 generator
    public final V3 V3;
    /// UUIDv4 generator
    public final V4 V4;
    /// UUIDv5 generator
    public final V5 V5;
    /// UUIDv6 generator
    public final V6 V6;
    /// UUIDv7 generator
    public final V7 V7;

    /// Construct an immutable generators-holder
    ///
    /// @param v1 UUIDv1 generator
    /// @param v3 UUIDv3 generator
    /// @param v4 UUIDv4 generator
    /// @param v5 UUIDv5 generator
    /// @param v6 UUIDv6 generator
    /// @param v7 UUIDv7 generator
    public Generators(V1 v1, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        this.V1 = v1;
        this.V3 = v3;
        this.V4 = v4;
        this.V5 = v5;
        this.V6 = v6;
        this.V7 = v7;
    }

}

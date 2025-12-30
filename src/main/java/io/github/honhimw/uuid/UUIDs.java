package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/// Prelude
///
/// ### UUID namespace
/// [Namespace ID Usage and Allocation](https://www.rfc-editor.org/rfc/rfc9562.html#name-namespace-id-usage-and-allo)
///
/// @author honhimW
/// @since 2025-12-09
public class UUIDs {

    // UUID namespace

    /// - Namespace ID Value: 6ba7b810-9dad-11d1-80b4-00c04fd430c8
    /// - Reference: [RFC9499](https://www.rfc-editor.org/rfc/rfc9562.html#RFC9499)
    public static final UUID NAMESPACE_DNS = new UUID(7757371264673321425L, -9172705715073830712L);
    /// - Namespace ID Value: 6ba7b811-9dad-11d1-80b4-00c04fd430c8
    /// - Reference: [RFC1738](https://www.rfc-editor.org/rfc/rfc9562.html#RFC1738)
    public static final UUID NAMESPACE_OID = new UUID(7757371273263256017L, -9172705715073830712L);
    /// - Namespace ID Value: 6ba7b812-9dad-11d1-80b4-00c04fd430c8
    /// - Reference: [X660](https://www.rfc-editor.org/rfc/rfc9562.html#X660)
    public static final UUID NAMESPACE_URL = new UUID(7757371268968288721L, -9172705715073830712L);
    /// - Namespace ID Value: 6ba7b814-9dad-11d1-80b4-00c04fd430c8
    /// - Reference: [X500](https://www.rfc-editor.org/rfc/rfc9562.html#X500)
    public static final UUID NAMESPACE_X500 = new UUID(7757371281853190609L, -9172705715073830712L);

    /// - ID Value: 00000000-0000-0000-0000-000000000000
    /// [Nil UUID](https://www.rfc-editor.org/rfc/rfc9562.html#name-nil-uuid)
    public static final UUID NIL = new UUID(0, 0);
    /// - ID Value: FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF
    /// [Max UUID](https://www.rfc-editor.org/rfc/rfc9562.html#name-max-uuid)
    public static final UUID MAX = new UUID(-1, -1);

    // Generators

    public static final Generators FAST;
    public static final Generators SECURE;

    static {
        {
            Supplier<Random> random = ThreadLocalRandom::current;
            RandomSequence randomSequence = new RandomSequence(random);
            V1 v1 = new V1(Context.builder().clock(randomSequence).random(random).build());
            V3 v3 = new V3(Context.builder().clock(randomSequence).random(random).build(), NAMESPACE_DNS);
            V4 v4 = new V4(Context.builder().clock(randomSequence).random(random).build());
            V5 v5 = new V5(Context.builder().clock(randomSequence).random(random).build(), NAMESPACE_DNS);
            V6 v6 = new V6(Context.builder().clock(randomSequence).random(random).build());
            V7 v7 = new V7(Context.builder().clock(new RandomSequence(random) {
                @Override
                public Instant now() {
                    return ClockSequence.fastNow();
                }
            }).random(random).build());
            FAST = new Generators(v1, v3, v4, v5, v6, v7);
        }
        {
            SecureRandom secureRandom;
            try {
                secureRandom = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                secureRandom = new SecureRandom();
            }
            SecureRandom finalSecureRandom = secureRandom;
            Supplier<Random> random = () -> finalSecureRandom;

            V1 v1 = new V1(Context.builder().random(random).build());
            V3 v3 = new V3(Context.builder().random(random).build(), NAMESPACE_DNS);
            V4 v4 = new V4(Context.builder().random(random).build());
            V5 v5 = new V5(Context.builder().random(random).build(), NAMESPACE_DNS);
            V6 v6 = new V6(Context.builder().random(random).build());
            V7 v7 = new V7(Context.builder()
                .random(random)
                .clock(new V7.ClockSequenceV7().withAdditionalPrecision())
                .build());
            SECURE = new Generators(v1, v3, v4, v5, v6, v7);
        }
    }

    /// Utilities

    public static byte[] toBytes(UUID uuid) {
        Bytes bb = new Bytes(16);
        return bb
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .unwrap();
    }

}

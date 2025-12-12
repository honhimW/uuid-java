package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Bytes;
import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.UUIDs;
import io.github.honhimw.uuid.UuidBuilder;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * @author honhimW
 * @since 2025-12-09
 */

public class V5 extends AbstractGenerator {

    private final UUID namespace;

    private final byte[] _bytes;

    public static V5 dns() {
        return new V5(UUIDs.NAMESPACE_DNS);
    }

    public static V5 oid() {
        return new V5(UUIDs.NAMESPACE_OID);
    }

    public static V5 url() {
        return new V5(UUIDs.NAMESPACE_URL);
    }

    public static V5 x500() {
        return new V5(UUIDs.NAMESPACE_X500);
    }

    public V5(UUID namespace) {
        super();
        this.namespace = namespace;
        this._bytes = UUIDs.toBytes(namespace);
    }

    public V5(Context context, UUID namespace) {
        super(context);
        this.namespace = namespace;
        this._bytes = UUIDs.toBytes(namespace);
    }

    @Override
    public UUID next() {
        Random random = _ctx.random.get();
        int len = random.ints(64, 512).findFirst().orElse(64);
        byte[] name = new byte[len];
        random.nextBytes(name);
        return of(name);
    }

    public UUID of(byte[] name) {
        MessageDigest md = _ctx.messageDigest.apply("SHA1");
        md.update(_bytes);
        md.update(name);
        byte[] digest = md.digest();
        md.reset();
        digest = Bytes.copyOf(digest, 16);
        return UuidBuilder.fromSha1Bytes(digest).build();
    }

}

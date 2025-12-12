package io.github.honhimw.uuid.variant;

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

public class V3 extends AbstractGenerator {

    private final UUID namespace;

    private final byte[] _bytes;

    public static V3 dns() {
        return new V3(UUIDs.NAMESPACE_DNS);
    }

    public static V3 oid() {
        return new V3(UUIDs.NAMESPACE_OID);
    }

    public static V3 url() {
        return new V3(UUIDs.NAMESPACE_URL);
    }

    public static V3 x500() {
        return new V3(UUIDs.NAMESPACE_X500);
    }

    public V3(UUID namespace) {
        super();
        this.namespace = namespace;
        this._bytes = UUIDs.toBytes(namespace);
    }

    public V3(Context context, UUID namespace) {
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
        MessageDigest md = _ctx.messageDigest.apply("MD5");
        md.update(_bytes);
        md.update(name);
        byte[] digest = md.digest();
        md.reset();
        return UuidBuilder.fromMd5Bytes(digest).build();
    }

}

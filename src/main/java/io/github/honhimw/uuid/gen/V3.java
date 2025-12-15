package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.Generator;
import io.github.honhimw.uuid.UUIDs;
import io.github.honhimw.uuid.UuidBuilder;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-3">Version 3</a>
 * UUIDv3 generator
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#MD5
 * @since 2025-12-09
 */

public class V3 extends AbstractGenerator implements Generator.NameBased {

    private final byte[] namespace;

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
        this.namespace = UUIDs.toBytes(namespace);
    }

    public V3(Context context, UUID namespace) {
        super(context);
        this.namespace = UUIDs.toBytes(namespace);
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
        md.update(namespace);
        md.update(name);
        byte[] digest = md.digest();
        md.reset();
        return UuidBuilder.fromMd5Bytes(digest).build();
    }

    public UUID of(String name) {
        return of(name.getBytes(_ctx.charset));
    }
}

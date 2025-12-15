package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.*;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * <a href="https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-5">Version 5</a>
 * UUIDv5 generator.
 *
 * @author honhimW
 * @see io.github.honhimw.uuid.Version#SHA1
 * @since 2025-12-09
 */

public class V5 extends AbstractGenerator implements Generator.NameBased {

    private final byte[] namespace;

    /**
     * DNS namespace generator
     *
     * @return DNS namespace generator
     */
    public static V5 dns() {
        return new V5(UUIDs.NAMESPACE_DNS);
    }

    /**
     * Object-ID namespace generator
     *
     * @return Object-ID namespace generator
     */
    public static V5 oid() {
        return new V5(UUIDs.NAMESPACE_OID);
    }

    /**
     * URL namespace generator
     *
     * @return URL namespace generator
     */
    public static V5 url() {
        return new V5(UUIDs.NAMESPACE_URL);
    }

    /**
     * X500 namespace generator
     *
     * @return X500 namespace generator
     */
    public static V5 x500() {
        return new V5(UUIDs.NAMESPACE_X500);
    }

    public V5(UUID namespace) {
        super();
        this.namespace = UUIDs.toBytes(namespace);
    }

    public V5(Context context, UUID namespace) {
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
        MessageDigest md = _ctx.messageDigest.apply("SHA1");
        md.update(namespace);
        md.update(name);
        byte[] digest = md.digest();
        md.reset();
        digest = Bytes.copyOf(digest, 16);
        return UuidBuilder.fromSha1Bytes(digest).build();
    }

    public UUID of(String name) {
        return of(name.getBytes(_ctx.charset));
    }

}

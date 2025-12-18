package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Bytes;
import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.Generator;
import io.github.honhimw.uuid.UUIDs;
import org.jspecify.annotations.Nullable;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/// Abstract name-based generator.
/// - If [MessageDigest] is cloneable: clone from a namespace updated MessageDigest.
/// - If not: get from MessageDigest provider and reset it before use it.
///
/// @author honhimW
/// @since 2025-12-18
public abstract class AbstractNameBasedGenerator extends AbstractGenerator implements Generator.NameBased {

    private final @Nullable MessageDigest clonableMessageDigest;

    protected final byte @Nullable [] namespace;

    protected AbstractNameBasedGenerator(@Nullable UUID namespace) {
        this(Context.builder().build(), namespace);
    }

    protected AbstractNameBasedGenerator(Context context, @Nullable UUID namespace) {
        super(context);
        if (namespace == null) {
            this.namespace = null;
            this.clonableMessageDigest = null;
        } else {
            this.namespace = UUIDs.toBytes(namespace);
            MessageDigest md = context.messageDigest.apply(algorithm());
            if (md instanceof Cloneable) {
                this.clonableMessageDigest = md;
                this.clonableMessageDigest.update(this.namespace);
            } else {
                this.clonableMessageDigest = null;
            }
        }
    }

    @Override
    public UUID next() {
        Random random = _ctx.random.get();
        Bytes bytes = new Bytes(16);
        byte[] name = bytes
            .putLong(random.nextLong())
            .putLong(random.nextLong())
            .unwrap();
        return of(name);
    }

    @Override
    public UUID of(String name) {
        return of(name.getBytes(_ctx.charset));
    }

    protected abstract String algorithm();

    protected MessageDigest getDigester() {
        if (clonableMessageDigest != null) {
            try {
                return (MessageDigest) clonableMessageDigest.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(e);
            }
        } else {
            MessageDigest digester = _ctx.messageDigest.apply(algorithm());
            digester.reset();
            if (this.namespace != null) {
                digester.update(namespace);
            }
            return digester;
        }
    }

}

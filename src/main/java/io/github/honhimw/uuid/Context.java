package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * UUID generator context.
 *
 * @author honhimW
 * @since 2025-12-09
 */

public class Context {

    public final ClockSequence clockSequence;

    public final Supplier<Random> random;

    public final Function<String, MessageDigest> messageDigest;

    public final Supplier<NodeId> node;

    private Context(ClockSequence clockSequence, Supplier<Random> random, Function<String, MessageDigest> messageDigest, Supplier<NodeId> node) {
        this.clockSequence = clockSequence;
        this.random = random;
        this.messageDigest = messageDigest;
        this.node = node;
    }

    public static Builder builder() {
        return new Builder();
    }

    public V1 v1() {
        return new V1(this);
    }

    public V3 v3(UUID namespace) {
        return new V3(this, namespace);
    }

    public V4 v4() {
        return new V4(this);
    }

    public V5 v5(UUID namespace) {
        return new V5(this, namespace);
    }

    public V6 v6() {
        return new V6(this);
    }

    public V7 v7() {
        return new V7(this);
    }

    public static class Builder {
        private ClockSequence clockSequence;
        private Supplier<Random> random;
        private Function<String, MessageDigest> messageDigest;
        private Supplier<NodeId> node;

        private Builder() {
            this.clockSequence = new CounterSequence(0);
            this.random = ThreadLocalRandom::current;
            this.messageDigest = algorithm -> {
                try {
                    return MessageDigest.getInstance(algorithm);
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException(String.format("`%s` not supported", algorithm), e);
                }
            };
            this.node = MacAddress::nodeId;
        }

        public Builder clock(ClockSequence clockSequence) {
            this.clockSequence = clockSequence;
            return this;
        }

        public Builder random(Supplier<Random> random) {
            this.random = random;
            return this;
        }

        public Builder messageDigest(Function<String, MessageDigest> messageDigest) {
            this.messageDigest = messageDigest;
            return this;
        }

        public Builder node(Supplier<NodeId> node) {
            this.node = node;
            return this;
        }

        public Context build() {
            return new Context(
                clockSequence,
                random,
                messageDigest,
                node
            );
        }
    }

}

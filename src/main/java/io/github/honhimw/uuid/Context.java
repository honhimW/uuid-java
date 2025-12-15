package io.github.honhimw.uuid;

import io.github.honhimw.uuid.gen.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

/// UUID generator context.
///
/// @author honhimW
/// @since 2025-12-09
public class Context {

    /// Name based UUID generator string name charset.
    public final Charset charset;

    /// UUID clock sequence generator.
    public final ClockSequence clockSequence;

    /// Random provider.
    public final Supplier<Random> random;

    /// Message digest provider.
    public final Function<String, MessageDigest> messageDigest;

    /// Node id provider.
    public final Supplier<NodeId> node;

    private Context(Charset charset, ClockSequence clockSequence, Supplier<Random> random, Function<String, MessageDigest> messageDigest, Supplier<NodeId> node) {
        this.charset = charset;
        this.clockSequence = clockSequence;
        this.random = random;
        this.messageDigest = messageDigest;
        this.node = node;
    }

    /// New context builder
    ///
    /// @return builder with default configuration
    public static Builder builder() {
        return new Builder();
    }

    /// Build a new UUIDv1 generator with current context.
    ///
    /// @return UUIDv1 generator
    public V1 v1() {
        return new V1(this);
    }

    /// Build a new UUIDv3 generator with current context.
    ///
    /// @param namespace UUIDv3 namespace
    /// @return UUIDv3 generator
    public V3 v3(UUID namespace) {
        return new V3(this, namespace);
    }

    /// Build a new UUIDv4 generator with current context.
    ///
    /// @return UUIDv4 generator
    public V4 v4() {
        return new V4(this);
    }

    /// Build a new UUIDv5 generator with current context.
    ///
    /// @param namespace UUIDv5 namespace
    /// @return UUIDv5 generator
    public V5 v5(UUID namespace) {
        return new V5(this, namespace);
    }

    /// Build a new UUIDv6 generator with current context.
    ///
    /// @return UUIDv6 generator
    public V6 v6() {
        return new V6(this);
    }

    /// Build a new UUIDv7 generator with current context.
    ///
    /// @return UUIDv7 generator
    public V7 v7() {
        return new V7(this);
    }

    /// Context builder
    public static class Builder {
        private Charset charset;
        private ClockSequence clockSequence;
        private Supplier<Random> random;
        private Function<String, MessageDigest> messageDigest;
        private Supplier<NodeId> node;

        private Builder() {
            this.charset = StandardCharsets.UTF_8;
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

        /// Name-based string charset
        ///
        /// @param charset name-based string charset
        /// @return self
        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        /// Clock sequence generator
        ///
        /// @param clockSequence clock sequence generator
        /// @return self
        public Builder clock(ClockSequence clockSequence) {
            this.clockSequence = clockSequence;
            return this;
        }

        /// Random provider
        ///
        /// @param random random provider
        /// @return self
        public Builder random(Supplier<Random> random) {
            this.random = random;
            return this;
        }

        /// Message digest provider
        ///
        /// @param messageDigest message digest provider
        /// @return self
        public Builder messageDigest(Function<String, MessageDigest> messageDigest) {
            this.messageDigest = messageDigest;
            return this;
        }

        /// NodeId provider
        ///
        /// @param node node-id provider
        /// @return self
        public Builder node(Supplier<NodeId> node) {
            this.node = node;
            return this;
        }

        /// Build immutable context
        ///
        /// @return new context
        public Context build() {
            return new Context(
                charset,
                clockSequence,
                random,
                messageDigest,
                node);
        }
    }

}

package io.github.honhimw.uuid;

import java.util.UUID;

/// UUID generator.
///
/// @author honhimW
/// @since 2025-12-08
public interface Generator {

    /// No-args UUID generation.
    ///
    /// For time-based generator, using current timestamp.
    ///
    /// For name-based generator, using random bytes.
    ///
    /// @return random uuid
    UUID next();

    /// Name-based UUID generator
    interface NameBased extends Generator {

        /// Generate name-based UUID with name
        ///
        /// @param name name
        /// @return UUID
        UUID of(byte[] name);

        /// Generate name-based UUID with string name
        ///
        /// @param name string name
        /// @return UUID
        UUID of(String name);

    }

    /// Time-based UUID generator
    interface TimeBased extends Generator {

        /// Generate time-based UUID with timestamp and default node-id
        ///
        /// @param ts UUID timestamp
        /// @return UUID
        UUID of(Timestamp ts);

        /// Generate time-based UUID with current-timestamp and node-id
        ///
        /// @param nodeId UUID timestamp
        /// @return UUID
        UUID now(NodeId nodeId);

    }

}

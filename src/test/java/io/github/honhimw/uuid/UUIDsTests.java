package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/// @author honhimW
/// @since 2025-12-19
public class UUIDsTests {
    
    @Test
    @SneakyThrows
    void namespaces() {
        Assertions.assertEquals("6ba7b810-9dad-11d1-80b4-00c04fd430c8", UUIDs.NAMESPACE_DNS.toString());
        Assertions.assertEquals("6ba7b811-9dad-11d1-80b4-00c04fd430c8", UUIDs.NAMESPACE_URL.toString());
        Assertions.assertEquals("6ba7b812-9dad-11d1-80b4-00c04fd430c8", UUIDs.NAMESPACE_OID.toString());
        Assertions.assertEquals("6ba7b814-9dad-11d1-80b4-00c04fd430c8", UUIDs.NAMESPACE_X500.toString());
        Assertions.assertEquals("00000000-0000-0000-0000-000000000000", UUIDs.NIL.toString());
        Assertions.assertEquals("ffffffff-ffff-ffff-ffff-ffffffffffff", UUIDs.MAX.toString());
    }
    
}

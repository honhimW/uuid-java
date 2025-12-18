package io.github.honhimw.uuid.gen;

import io.github.honhimw.uuid.Context;
import io.github.honhimw.uuid.UUIDs;
import io.github.honhimw.uuid.UuidBuilder;
import org.jspecify.annotations.Nullable;

import java.security.MessageDigest;
import java.util.UUID;

/// [Version 3](https://www.rfc-editor.org/rfc/rfc9562.html#name-uuid-version-3)
/// ```text
///  0                   1                   2                   3
///  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                            md5_high                           |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |          md5_high             |  ver  |       md5_mid         |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |var|                        md5_low                            |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// |                            md5_low                            |
/// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
/// ```
///
/// - md5_high: The first 48 bits of the layout are filled with the most significant, leftmost 48 bits from the computed MD5 value. Occupies bits 0 through 47 (octets 0-5).
/// - ver: The 4-bit version field as defined by Section 4.2, set to 0b0011 (3). Occupies bits 48 through 51 of octet 6.
/// - md5_mid: 12 more bits of the layout consisting of the least significant, rightmost 12 bits of 16 bits immediately following md5_high from the computed MD5 value. Occupies bits 52 through 63 (octets 6-7).
/// - var: The 2-bit variant field as defined by Section 4.1, set to 0b10. Occupies bits 64 and 65 of octet 8.
/// - md5_low: The final 62 bits of the layout immediately following the var field to be filled with the least significant, rightmost bits of the final 64 bits from the computed MD5 value. Occupies bits 66 through 127 (octets 8-15)
///
/// @author honhimW
/// @see io.github.honhimw.uuid.Version#MD5
/// @since 2025-12-09
public class V3 extends AbstractNameBasedGenerator {

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

    public V3(@Nullable UUID namespace) {
        super(namespace);
    }

    public V3(Context context, @Nullable UUID namespace) {
        super(context, namespace);
    }

    @Override
    protected String algorithm() {
        return "MD5";
    }

    @Override
    public UUID of(byte[] name) {
        MessageDigest md = getDigester();
        byte[] digest = md.digest(name);
        return UuidBuilder.fromMd5Bytes(digest).build();
    }

}

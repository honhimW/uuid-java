package io.github.honhimw.uuid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/// Prevent from using division. Make it as fast as possible.
///
/// @author honhimW
/// @since 2025-12-30
public class Maths {

    public static final long ONE_THOUSAND = 1_000L;
    public static final long ONE_MILLION = 1_000_000L;
    /// 1 << 10 == 1024 greater than 1000, so we just take highest 10-bits in i64, 64 - 10 = 54
    public static final int NANOS_TO_MILLIS_SHIFT = 54;
    public static final long NANOS_TO_MILLIS_MULTIPLIER = ((1L) << NANOS_TO_MILLIS_SHIFT) / ONE_MILLION;

    public static final long DIV_1000_MULTIPLIER = new BigDecimal(BigInteger.ONE.shiftLeft(64)).divide(BigDecimal.valueOf(125), RoundingMode.CEILING).longValueExact();

    /// Optimize division by one million
    ///
    /// @param nanos range 1_000_000 - 1_000_000_000
    /// @return millis seconds
    public static int ns2ms(int nanos) {
        return (int) (((nanos + 1) * NANOS_TO_MILLIS_MULTIPLIER) >>> NANOS_TO_MILLIS_SHIFT);
    }

    /// Optimize remainder of one million
    ///
    /// @param nanos range 1_000_000 - 1_000_000_000
    /// @return lower nano seconds
    public static int nsMod(int nanos) {
        int ms = ns2ms(nanos);
        return nanos - (int) (ms * ONE_MILLION);
    }

    /// Optimize division by one thousand.
    ///
    /// 1000 == 8 * 125
    /// 1. right shift 3(>>> 3) for division by 8
    /// 2. divide by 125 using the "magic number" method. For odd divisors, we can compute [#DIV_1000_MULTIPLIER]
    ///  as the multiplier constant.
    /// 3. use [#multiplyHigh(long, long)] to compute the high 64 bits of the 128-bit product.
    ///  This effectively performs floor(x / 125) for all 64‑bit inputs.
    ///
    /// @param l long value
    /// @return result of division by 1000
    public static long div1000(long l) {
        l = l >>> 3; // division by 8
        return multiplyHigh(l, DIV_1000_MULTIPLIER);
    }

    /// Convert the lowest 3 decimal digits of milliseconds into nanoseconds.
    ///
    /// @param millis milli seconds
    /// @return nano seconds
    public static int ms2ns(long millis) {
        long l = div1000(millis);
        l = millis - (l * ONE_THOUSAND);
        return (int) (l * ONE_MILLION);
    }

    /// Returns as a `long` the most significant 64 bits of the 128-bit product of two 64-bit factors.
    ///
    /// Copied from JDK9+
    ///
    /// @param x the first value
    /// @param y the second value
    /// @return the result
    public static long multiplyHigh(long x, long y) {
        // Use technique from section 8-2 of Henry S. Warren, Jr.,
        // Hacker's Delight (2nd ed.) (Addison Wesley, 2013), 173-174.
        long x1 = x >> 32;
        long x2 = x & 0xFFFFFFFFL;
        long y1 = y >> 32;
        long y2 = y & 0xFFFFFFFFL;

        long z2 = x2 * y2;
        long t = x1 * y2 + (z2 >>> 32);
        long z1 = t & 0xFFFFFFFFL;
        long z0 = t >> 32;
        z1 += x2 * y1;

        return x1 * y1 + z0 + (z1 >> 32);
    }

}

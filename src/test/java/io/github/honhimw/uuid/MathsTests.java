package io.github.honhimw.uuid;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/// @author honhimW
/// @since 2025-12-30
public class MathsTests {

    @Test
    @SneakyThrows
    void ns2ms() {
        for (long l = Maths.ONE_MILLION; l < Maths.ONE_MILLION * 1000; l += 100) {
            int ms = Maths.ns2ms((int) l);
            Assertions.assertEquals(l / Maths.ONE_MILLION, ms);
        }
    }

    @Test
    @SneakyThrows
    void nsMod() {
        for (long l = Maths.ONE_MILLION; l < Maths.ONE_MILLION * 1000; l += 100) {
            int lowerNanos = Maths.nsMod((int) l);
            Assertions.assertEquals(l % Maths.ONE_MILLION, lowerNanos);
        }
    }

    @Test
    @SneakyThrows
    void ms2ns() {
        for (long l = Maths.ONE_THOUSAND; l < Maths.ONE_THOUSAND * 1000; l += 100) {
            long result = Maths.div1000((int) l);
            long nanos = Maths.ms2ns((int) l);

            Assertions.assertEquals((l / 1000), result);
            Assertions.assertEquals((l % 1000) * Maths.ONE_MILLION, nanos);
        }
    }

}

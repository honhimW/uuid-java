package bench.target;

import bench.AbstractBench;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/// @author honhimW
/// @since 2025-12-10
public class V7Fastest extends AbstractBench {

    @Override
    public void run() throws Exception {
        // 1) Fetch current time in ms, mask to 48 bits
        long currentMillis = System.currentTimeMillis();
        long ts48 = currentMillis & 0xFFFFFFFFFFFFL;  // 48-bit mask

        // 2) Get 74 bits of entropy from ThreadLocalRandom: 64 + 32 bits
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long random64 = random.nextLong();
        int random32 = random.nextInt();

        // Assemble the high 64 bits:
        //   [ 48-bit timestamp ] [ 4-bit version=7 ] [ 12 high random bits ]
        long high = (ts48 << 16);                         // place 48 ms bits at bits 0–47 of high<<16 = bits 16–63
        long randHigh12 = (random64 >>> 52) & 0x0FFFL;    // top 12 bits of random64
        high |= randHigh12;                              // bits 52–63
        high |= 0x0000000000007000L;                     // set version (4 bits = 0b0111) at bits 48–51

        // Assemble the low 64 bits:
        //   [ 2-bit variant=10 ] [ 52 low bits of random64 ] [ 10 high bits of random32 ]
        long low = 0x8000000000000000L;                   // set variant 0b10 at bits 64–65
        long randLow52 = random64 & 0x000FFFFFFFFFFFFFL;  // lower 52 bits of random64
        int rand32High10 = (random32 >>> 22) & 0x3FF;     // top 10 bits of random32
        low |= (randLow52 << 10);                         // place 52 bits at bits 66–117
        low |= rand32High10;                              // place 10 bits at bits 118–127
        new UUID(high, low);
    }
}

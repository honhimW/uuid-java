# UUID Generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.honhimw/uuid-java.svg)](https://central.sonatype.com/artifact/io.github.honhimw/uuid-java)

## Usage

```groovy
implementation 'io.github.honhimw:uuid-java:{latest}'
```

```java
import io.github.honhimw.uuid.*;
import io.github.honhimw.uuid.gen.*;

void main() {
    // Using via global shared instances.
    Generator generator = UUIDs
        .FAST  // FAST | SECURE default configuration
        .V7; // V1 | V3 | V4 | V5 | V6 | V7
    UUID uuid = generator.next(); // Generation

    // Customize generator
    V7 v7 = new V7(Context.builder()
        .clock(
            new V7.ClockSequenceV7()   // Reseeding timestamp context
                .withAdditionalPrecision() // Add 12-bits for timestamp-precision
        )
        .random(() -> ThreadLocalRandom.current()) // For fast. `new SecureRandom()` for secure
        .node(() -> MacAddress.nodeId()) // Using other by specificizing NodeId.of(new byte[] {1, 2, 3, 4, 5, 6})
        .messageDigest(algorithm -> MessageDigest.getInstance(algorithm)) // MessageDigest provider(getting from thread-local cache?)
        .build());
}
```

## Benchmark

<details>
<summary>Details</summary>

> JMH version: 1.37  
> VM version: JDK 21.0.6, Java HotSpot(TM) 64-Bit Server VM, 21.0.6+8-LTS-jvmci-23.1-b55  
> Warmup: 2 iterations, 1 s each  
> Measurement: 4 iterations, 1 s each  
> Threads: 8 threads, will synchronize iterations  
> Benchmark mode: Throughput, ops/time

| Name          |       Score(thrpt) |
|---------------|-------------------:|
| V1Fasterxml   |    9887.780 ops/ms |
| V1Self        |  261844.990 ops/ms |
| V1UuidCreator |   36179.278 ops/ms |
| V3Fasterxml   |    7027.750 ops/ms |
| V3Self        |   57394.957 ops/ms |
| V3UuidCreator |   42964.542 ops/ms |
| V4Fasterxml   | 3006616.633 ops/ms |
| V4Jdk         |    1027.490 ops/ms |
| V4Self        | 3022930.708 ops/ms |
| V4UuidCreator |   43633.409 ops/ms |
| V5Fasterxml   |    3349.892 ops/ms |
| V5Self        |   26425.357 ops/ms |
| V5UuidCreator |   22710.366 ops/ms |
| V6Fasterxml   |    9893.974 ops/ms |
| V6Self        |  266092.569 ops/ms |
| V6UuidCreator |   34884.844 ops/ms |
| V7Fasterxml   |   30225.132 ops/ms |
| V7Fastest     | 1277599.442 ops/ms |
| V7Self        | 1028503.228 ops/ms |
| V7UuidCreator |   20101.737 ops/ms |

</details>


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
> VM version: JDK 25.0.1, Java HotSpot(TM) 64-Bit Server VM, 25.0.1+8-LTS-jvmci-b01  
> Warmup: 2 iterations, 1 s each  
> Measurement: 4 iterations, 1 s each  
> Threads: 6 threads, will synchronize iterations  
> Benchmark mode: Throughput, ops/time

| Name          |       Score(thrpt) |
|---------------|-------------------:|
| V1Fasterxml   |    9896.610 ops/ms |
| V1Self        |  286766.275 ops/ms |
| V1UuidCreator |   33975.788 ops/ms |
| V3Fasterxml   |    7042.645 ops/ms |
| V3Self        |   57528.830 ops/ms |
| V3UuidCreator |   43219.599 ops/ms |
| V4Fasterxml   | 3140606.305 ops/ms |
| V4Jdk         |     968.399 ops/ms |
| V4Self        | 2962402.564 ops/ms |
| V4UuidCreator |   41973.260 ops/ms |
| V5Fasterxml   |    3384.154 ops/ms |
| V5Self        |   26700.751 ops/ms |
| V5UuidCreator |   22702.842 ops/ms |
| V6Fasterxml   |    9883.793 ops/ms |
| V6Self        |  243767.693 ops/ms |
| V6UuidCreator |   34400.246 ops/ms |
| V7Fasterxml   |   31643.356 ops/ms |
| V7Fastest     | 1578077.735 ops/ms |
| V7Self        |  994545.869 ops/ms |
| V7UuidCreator |   19878.532 ops/ms |

</details>


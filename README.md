# UUID Generator

## Usage

```java
import io.github.honhimw.uuid.*;
import io.github.honhimw.uuid.gen.*;
void main() {
    // Using via global shared instances.
    Generator generator = UUIDs
        .FAST  // FAST | SECURE default configuration
        .v1(); // v1 | v3 | v4 | v5 | v6 | v7
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
| V1Fasterxml   |    9885.222 ops/ms |
| V1Self        |  297450.331 ops/ms |
| V1UuidCreator |   38422.519 ops/ms |
| V3Fasterxml   |    5724.019 ops/ms |
| V3Self        |   40946.284 ops/ms |
| V3UuidCreator |   43344.585 ops/ms |
| V4Fasterxml   | 3972463.887 ops/ms |
| V4Jdk         |     814.632 ops/ms |
| V4Self        | 3376319.678 ops/ms |
| V4UuidCreator |   44105.475 ops/ms |
| V5Fasterxml   |    2580.088 ops/ms |
| V5Self        |   21503.773 ops/ms |
| V5UuidCreator |   20561.813 ops/ms |
| V6Fasterxml   |    9888.172 ops/ms |
| V6Self        |  283465.495 ops/ms |
| V6UuidCreator |   38232.100 ops/ms |
| V7Fasterxml   |   29296.055 ops/ms |
| V7Fastest     | 1595986.093 ops/ms |
| V7Self        | 1046789.720 ops/ms |
| V7UuidCreator |   18271.016 ops/ms |

</details>


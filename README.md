# UUID Generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.honhimw/uuid-java.svg)](https://central.sonatype.com/artifact/io.github.honhimw/uuid-java)

```groovy
implementation 'io.github.honhimw:uuid-java:{latest}'
```

---

## Benchmark

```shell
./gradlew bench
```

<details>
<summary>Details</summary>

> JMH version: 1.37  
> VM version: JDK 25.0.1, Java HotSpot (TM) 64-Bit Server VM, 25.0.1+8-LTS-jvmci-b01  
> Warmup: 2 iterations, 1 s each  
> Measurement: 4 iterations, 1 s each  
> Threads: 6 threads, will synchronize iterations  
> Benchmark mode: Throughput, ops/time

| Name          |       Score(thrpt) |
|---------------|-------------------:|
| V1Fasterxml   |    9889.128 ops/ms |
| V1Self        |  248979.250 ops/ms |
| V1SelfSecure  |   23203.039 ops/ms |
| V1UuidCreator |   34402.695 ops/ms |
| V3Fasterxml   |    6918.611 ops/ms |
| V3Self        |   56499.424 ops/ms |
| V3SelfSecure  |   54563.924 ops/ms |
| V3UuidCreator |   41852.958 ops/ms |
| V4Fasterxml   | 3081285.846 ops/ms |
| V4Jdk         |    1021.013 ops/ms |
| V4Self        | 3040052.353 ops/ms |
| V4SelfSecure  |     251.294 ops/ms |
| V4UuidCreator |   44051.426 ops/ms |
| V5Fasterxml   |    3327.681 ops/ms |
| V5Self        |   27256.804 ops/ms |
| V5SelfSecure  |   27326.388 ops/ms |
| V5UuidCreator |   23258.985 ops/ms |
| V6Fasterxml   |    9896.053 ops/ms |
| V6Self        |  274965.694 ops/ms |
| V6SelfSecure  |   23408.190 ops/ms |
| V6UuidCreator |   35989.205 ops/ms |
| V7Fasterxml   |   31870.053 ops/ms |
| V7Fastest     | 1292723.959 ops/ms |
| V7Self        | 1404916.850 ops/ms |
| V7SelfSecure  |     297.321 ops/ms |
| V7UuidCreator |   19100.390 ops/ms |

</details>

---

## Usage

```java
import io.github.honhimw.uuid.*;
import io.github.honhimw.uuid.gen.*;

void main() {
    // Using via global shared instances.
    Generator generator = UUIDs
        .FAST  // FAST | SECURE default configuration
        .V7; // V1 | V3 | V4 | V5 | V6 | V7
    UUID uuid = generator.next(); // Generation
//    UUID uuid = UUIDs.FAST.V1.now(MacAddress.nodeId());
//    UUID uuid = UUIDs.FAST.V3.of("foo");
//    UUID uuid = UUIDs.FAST.V4.next();
//    UUID uuid = UUIDs.FAST.V5.of("bar");
//    UUID uuid = UUIDs.FAST.V6.now(MacAddress.nodeId());
//    UUID uuid = UUIDs.FAST.V7.of(CounterSequence.SHARED.timestamp());
}
```

### Customize

```java
void main() {
    V7 v7 = new V7(Context.builder()
        .clock(
            new V7.ClockSequenceV7()   // Reseeding timestamp context
                .withAdditionalPrecision() // Add 12-bits for timestamp-precision
        )
        .random(() -> ThreadLocalRandom.current()) // For fast. `new SecureRandom()` for secure
        .node(() -> MacAddress.nodeId()) // Using other by specificizing NodeId.of(new byte[] {1, 2, 3, 4, 5, 6})
        .messageDigest(algorithm -> MessageDigest.getInstance(algorithm)) // MessageDigest provider(getting from thread-local cache?)
        .build());
    v7.next(); // Generation
}
```

### Typed Uuid

```java
import io.github.honhimw.uuid.Uuid;

void main() {
    UUID jdkUUID = UUID.randomUUID();
    Uuid uuid = Uuid.fromUUID(jdkUUID);

    assert Variant.RFC4122 == uuid.variant() : "variant RFC4122";
    assert Version.RANDOM == uuid.version() : "version 4";
    assert uuid.timestamp().isEmpty() : "not time based";
    assert uuid.node().isEmpty() : "no node";

    jdkUUID = uuid.asUUID();
    byte[] bytes = uuid.asBytes();
} 
```

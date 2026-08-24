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
> Warmup: 5 iterations, 5 s each
> Measurement: 4 iterations, 4 s each
> Threads: 6 threads, will synchronize iterations  
> Benchmark mode: Throughput, ops/time

| Name          |       Score(thrpt) |
|---------------|-------------------:|
| V1Fasterxml   |    9875.550 ops/ms |
| V1Self        |  251569.564 ops/ms |
| V1UuidCreator |   33725.897 ops/ms |
| V3Fasterxml   |    6505.923 ops/ms |
| V3Self        |   50167.978 ops/ms |
| V3UuidCreator |   37720.554 ops/ms |
| V4Fasterxml   | 2807239.341 ops/ms |
| V4Jdk         |    1012.926 ops/ms |
| V4Self        | 2801417.654 ops/ms |
| V4UuidCreator |   42465.890 ops/ms |
| V5Fasterxml   |    3285.011 ops/ms |
| V5Self        |   23947.733 ops/ms |
| V5UuidCreator |   19930.041 ops/ms |
| V6Fasterxml   |    9875.309 ops/ms |
| V6Self        |  273115.546 ops/ms |
| V6UuidCreator |   35289.157 ops/ms |
| V7Fastest     | 1373584.697 ops/ms |
| V7Self        | 1268108.792 ops/ms |

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

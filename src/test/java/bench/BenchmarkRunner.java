package bench;

import bench.target.*;
import lombok.SneakyThrows;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.util.ScoreFormatter;

import java.util.*;

/// @author honhimW
/// @since 2025-12-09
public class BenchmarkRunner {

    private static final List<RunResult> RESULTS = new ArrayList<>();

    private static int threads;

    private static int forks;

    @SneakyThrows
    public static void main(String[] args) {
        threads = Runtime.getRuntime().availableProcessors();
        forks = 5;
        v1();
        v3();
        v4();
        v5();
        v6();
        v7();
        printResult();
//        RESULTS.clear();
//        secure();
//        printResult();
    }

    public static void printResult() {
        List<String> lines = new ArrayList<>();
        Map<String, Map<String, Double>> avg = new TreeMap<>();
        for (RunResult runResult : RESULTS) {
            BenchmarkParams params = runResult.getParams();
            Collection<BenchmarkResult> benchmarkResults = runResult.getBenchmarkResults();
            for (BenchmarkResult benchmarkResult : benchmarkResults) {
                String benchmark = params.getBenchmark();
                benchmark = benchmark.replace("bench.target.", "");
                benchmark = benchmark.replace(".run", "");
                Map<String, Double> unitScoreMap = avg.compute(benchmark, (name, map) -> {
                    if (map == null) {
                        map = new HashMap<>();
                    }
                    return map;
                });
                Result<?> primaryResult = benchmarkResult.getPrimaryResult();
                unitScoreMap.compute(primaryResult.getScoreUnit(), (unit, score) -> score == null ? primaryResult.getScore() : (score + primaryResult.getScore()) / 2);
//                String score = String.format("%s %s", ScoreFormatter.format(primaryResult.getScore()), primaryResult.getScoreUnit());
//                lines.add(String.format("| %-20s | %20s |", benchmark, score));
            }
        }
        avg.forEach((name, unitScoreMap) -> {
            unitScoreMap.forEach((unit, score) -> {
                String s = String.format("%s %s", ScoreFormatter.format(score), unit);
                lines.add(String.format("| %-20s | %20s |", name, s));
            });
        });
        lines.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println("#############################################################");
        System.out.println("| Name                 | Score(thrpt)         |");
        System.out.println("| -------------------- | --------------------:|");
        lines.forEach(System.out::println);
        System.out.println("#############################################################");
    }

    @SneakyThrows
    public static void v1() {
        Options options = builder(
            V1UuidCreator.class,
            V1Fasterxml.class,
            V1Self.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v3() {
        Options options = builder(
            V3UuidCreator.class,
            V3Fasterxml.class,
            V3Self.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v4() {
        Options options = builder(
            V4UuidCreator.class,
            V4Fasterxml.class,
            V4Jdk.class,
            V4Self.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v5() {
        Options options = builder(
            V5Fasterxml.class,
            V5UuidCreator.class,
            V5Self.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v6() {
        Options options = builder(
            V6UuidCreator.class,
            V6Fasterxml.class,
            V6Self.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v7() {
        Options options = builder(
            V7Fastest.class,
//            V7UuidCreator.class,
//            V7Fasterxml.class，
            V7Self.class
        ).threads(threads)
            .forks(3)
//            .addProfiler(GCProfiler.class)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void secure() {
        Options options = builder(
            V1SelfSecure.class,
            V3SelfSecure.class,
            V4SelfSecure.class,
            V5SelfSecure.class,
            V6SelfSecure.class,
            V7SelfSecure.class
        )
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void others() {
        Options options = new OptionsBuilder()
            .threads(threads)
            .forks(forks)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    public static OptionsBuilder builder(Class<?>... clazzs) {
        OptionsBuilder builder = new OptionsBuilder();
        for (Class<?> clazz : clazzs) {
            builder.include(clazz.getName() + ".run");
        }
        return builder;
    }

}

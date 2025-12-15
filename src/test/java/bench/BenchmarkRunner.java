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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// @author honhimW
/// @since 2025-12-09
public class BenchmarkRunner {

    private static final List<RunResult> RESULTS = new ArrayList<>();

    @SneakyThrows
    public static void main(String[] args) {
        v1();
        v3();
        v4();
        v5();
        v6();
        v7();
        printResult();
    }

    public static void printResult() {
        List<String> lines = new ArrayList<>();
        for (RunResult runResult : RESULTS) {
            BenchmarkParams params = runResult.getParams();
            Collection<BenchmarkResult> benchmarkResults = runResult.getBenchmarkResults();
            for (BenchmarkResult benchmarkResult : benchmarkResults) {
                String benchmark = params.getBenchmark();
                benchmark = benchmark.replace("bench.target.", "");
                benchmark = benchmark.replace(".run", "");
                Result<?> primaryResult = benchmarkResult.getPrimaryResult();
                String score = String.format("%s %s", ScoreFormatter.format(primaryResult.getScore()), primaryResult.getScoreUnit());
                lines.add(String.format("| %-20s | %20s |", benchmark, score));
            }
        }
        lines.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println("#############################################################");
        System.out.println("| Name | Score(thrpt) |");
        System.out.println("| ---- | ------------:|");
        lines.forEach(System.out::println);
        System.out.println("#############################################################");
    }

    @SneakyThrows
    public static void v1() {
        Options options = new OptionsBuilder()
            .include(V1Self.class.getSimpleName())
            .include(V1UuidCreator.class.getSimpleName())
            .include(V1Fasterxml.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v3() {
        Options options = new OptionsBuilder()
            .include(V3Self.class.getSimpleName())
            .include(V3UuidCreator.class.getSimpleName())
            .include(V3Fasterxml.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v4() {
        Options options = new OptionsBuilder()
            .include(V4Self.class.getSimpleName())
            .include(V4UuidCreator.class.getSimpleName())
            .include(V4Fasterxml.class.getSimpleName())
            .include(V4Jdk.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v5() {
        Options options = new OptionsBuilder()
            .include(V5Self.class.getSimpleName())
            .include(V5UuidCreator.class.getSimpleName())
            .include(V5Fasterxml.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v6() {
        Options options = new OptionsBuilder()
            .include(V6Self.class.getSimpleName())
            .include(V6UuidCreator.class.getSimpleName())
            .include(V6Fasterxml.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

    @SneakyThrows
    public static void v7() {
        Options options = new OptionsBuilder()
            .include(V7Fastest.class.getSimpleName())
            .include(V7Self.class.getSimpleName())
            .include(V7UuidCreator.class.getSimpleName())
            .include(V7Fasterxml.class.getSimpleName())
            .threads(8)
            .forks(1)
            .build();

        Collection<RunResult> run = new Runner(options).run();
        RESULTS.addAll(run);
    }

}

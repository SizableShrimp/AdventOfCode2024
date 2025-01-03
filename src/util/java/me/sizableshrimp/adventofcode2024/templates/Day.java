/*
 * AdventOfCode2024
 * Copyright (C) 2024 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode2024.templates;

import com.google.common.annotations.VisibleForTesting;
import me.sizableshrimp.adventofcode2024.helper.DataManager;

import java.io.IOException;
import java.util.List;

/**
 * A single day which has two challenges to solve.
 * There are 25 days in <a href="http://adventofcode.com">Advent Of Code</a>.
 * Each day has two parts to it to solve the entire day.
 */
public abstract class Day {
    /**
     * An <b>unmodifiable</b> list of the lines parsed from the input file for the challenge.
     * For example, an input file with the data:
     * <pre>{@code 1
     * 2
     * 3
     * 4
     * 5
     * }</pre>
     * would be parsed as {"1", "2", "3", "4", "5"}.
     * <p>
     * <b>NOTE:</b> This variable is assigned using {@link DataManager#read}, which means it has the possibility to hit
     * the Advent Of Code servers to request the input data. See {@link DataManager#read} for more details.
     */
    protected final List<String> lines;

    protected Day() {
        try {
            this.lines = DataManager.read(Integer.parseInt(getClass().getSimpleName().substring(3, 5)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the raw file input, denoting lines by <b>Unix-style endings</b> or <code>\n</code>
     */
    protected final String getRawInput() {
        return String.join("\n", this.lines);
    }

    /**
     * Execute a given day; printing out part 1, part 2, and the time taken.
     * Time taken is using {@link System#nanoTime()} and is not a real benchmark.
     *
     * @return A {@link Result} holding data of the first and second part.
     */
    public final Result run() {
        long before = System.nanoTime();
        Result result = parseAndEvaluate();
        long after = System.nanoTime();
        float time = (after - before) / 1_000_000f;
        System.out.println("Part 1: " + result.part1);
        System.out.println("Part 2: " + result.part2);
        System.out.printf("Completed in %.3fms%n%n", time);
        return result;
    }

    /**
     * Execute a given day; returning a {@link TimedResult} object holding part 1, part 2, and the time taken.
     * Time taken is using {@link System#nanoTime()} and is not a real benchmark.
     *
     * @return A {@link TimedResult} holding data of the first part, second part, and time taken.
     */
    public final TimedResult runTimed() {
        long before = System.nanoTime();
        Result result = parseAndEvaluate();
        long after = System.nanoTime();
        return new TimedResult(result.part1, result.part2, after - before);
    }

    /**
     * Parse and then evaluate a day's code.
     * This should be guaranteed to be repeatable without constructing a new instance of the class.
     *
     * @return A {@link Result} holding data of the first and second part.
     */
    public final Result parseAndEvaluate() {
        parse();
        return evaluate();
    }

    /**
     * This internal method is what actually evaluates the result of part 1 and part 2.
     */
    protected abstract Result evaluate();

    /**
     * This internal method can be overridden to parse the {@link #lines} of the day into something more useful for
     * the challenge.
     * <p>
     * This method will automatically be run before {@link #evaluate()}.
     */
    protected void parse() {}

    /**
     * This should only be using for benchmarking purposes. Other uses are not supported.
     */
    @VisibleForTesting
    public final void parseTesting() {
        parse();
    }

    /**
     * This should only be using for benchmarking purposes. Other uses are not supported.
     */
    @VisibleForTesting
    public final void evaluateTesting() {
        evaluate();
    }

    public record Result(Object part1, Object part2) {
        public static Result of(Object part1, Object part2) {
            return new Result(part1, part2);
        }
    }

    public record TimedResult(Object part1, Object part2, long timeTaken) {
        /**
         * Time taken in nanoseconds
         */
        @Override
        public long timeTaken() {
            return this.timeTaken;
        }
    }
}

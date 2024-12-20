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

package me.sizableshrimp.adventofcode2024.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Processor {
    public static <T> BinaryOperator<Set<T>> unionBinaryOp() {
        return Processor::union;
    }

    // Union
    public static <T> Set<T> union(Collection<T> first, Collection<T> second) {
        Set<T> union = new HashSet<>(first);
        union.addAll(second);
        return union;
    }

/*    public static <T> Set<T> unionArray(Set<T> result, T[] element) {
        return union(result, Arrays.asList(element));
    }

    public static <T> Set<T> intersectionArray(Set<T> result, T[] element) {
        return intersection(result, Arrays.asList(element));
    }*/

    // Intersection
    public static <T> Set<T> intersection(Collection<T> first, Collection<T> second) {
        Set<T> intersection = new HashSet<>(first);
        intersection.retainAll(second);
        return intersection;
    }

    public static <T> BinaryOperator<Set<T>> intersectionBinaryOp() {
        return Processor::intersection;
    }

    // // Union Array Overloads
    // public static Set<Character> unionArray(Set<Character> result, char[] element) {
    //     List<Character> l = new ArrayList<>();
    //     for (char c : element) {
    //         l.add(c);
    //     }
    //     return union(result, l);
    // }
    //
    // // Intersection Array Overloads
    // public static Set<Character> intersectionArray(Set<Character> result, char[] element) {
    //     List<Character> l = new ArrayList<>();
    //     for (char c : element) {
    //         l.add(c);
    //     }
    //     return intersection(result, l);
    // }

    // Splits
    public static <T> List<List<T>> split(List<T> list, Predicate<T> splitter) {
        int length = list.size();
        return split(length, i -> splitter.test(list.get(i)), list::subList).toList();
    }

    public static List<List<String>> splitOnBlankLines(List<String> list) {
        return split(list, String::isBlank);
    }

    private static <T> Stream<T> split(int length, IntPredicate splitter, BiFunction<Integer, Integer, T> func) {
        List<Integer> indices = new ArrayList<>();
        indices.add(-1);
        for (int i = 0; i < length; i++) {
            if (splitter.test(i))
                indices.add(i);
        }
        indices.add(length);

        return IntStream.range(0, indices.size() - 1)
                .mapToObj(i -> func.apply(indices.get(i) + 1, indices.get(i + 1)));
    }

    public static <T> Stream<Stream<T>> splitStream(List<T> list, Predicate<T> splitter) {
        int length = list.size();
        return split(length, i -> splitter.test(list.get(i)),
                (a, b) -> list.subList(a, b).stream());
    }

    public static <T> List<T[]> split(T[] arr, Predicate<T> splitter) {
        return split(arr.length, i -> splitter.test(arr[i]),
                (a, b) -> Arrays.copyOfRange(arr, a, b))
                .toList();
    }

    public static <T> Stream<Stream<T>> splitStream(T[] arr, Predicate<T> splitter) {
        return split(arr.length, i -> splitter.test(arr[i]),
                (a, b) -> Arrays.stream(Arrays.copyOfRange(arr, a, b)));
    }

    /**
     * Creates a list of windows based on the original collection using the given size per window.
     * The windows cannot overlap.
     *
     * @param collection the base collection
     * @param windowSize how large to make each subgroup
     * @param allowTrailing whether any trailing elements which do not match the window size should be added to the list
     * @return a list of windows
     */
    public static <T> List<List<T>> windowed(Collection<T> collection, int windowSize, boolean allowTrailing) {
        return windowed(collection, windowSize, 0, allowTrailing);
    }

    /**
     * Creates a list of windows based on the original collection using the given size per window.
     * The windows cannot overlap.
     *
     * @param collection the base collection
     * @param windowSize how large to make each subgroup
     * @param skipSize how many elements to skip between each window
     * @param allowTrailing whether any trailing elements which do not match the window size should be added to the list
     * @param <T> the type of the collection
     * @return a list of windows
     */
    public static <T> List<List<T>> windowed(Collection<T> collection, int windowSize, int skipSize, boolean allowTrailing) {
        List<List<T>> windows = new ArrayList<>();
        List<T> currList = null;
        int skippingCount = 0;

        for (T t : collection) {
            if (skippingCount > 0) {
                skippingCount--;
                continue;
            }

            if (currList != null && currList.size() == windowSize) {
                windows.add(currList);
                currList = new ArrayList<>();
                skippingCount = skipSize;
            } else if (currList == null) {
                currList = new ArrayList<>();
            }

            currList.add(t);
        }

        if (currList != null && (allowTrailing || currList.size() == windowSize))
            windows.add(currList);

        return windows;
    }
}

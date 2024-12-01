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

import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import me.sizableshrimp.adventofcode2024.templates.Coordinate;
import me.sizableshrimp.adventofcode2024.templates.EnumState;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class Printer {
    public static String toString(Collection<Coordinate> coords) {
        return toString(coords, (contains, coord) -> contains ? "#" : ".");
    }

    public static String toString(Collection<Coordinate> coords, Coordinate2StringFunction function) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Coordinate coord : coords) {
            if (coord.x() < minX)
                minX = coord.x();
            if (coord.y() < minY)
                minY = coord.y();
            if (coord.x() > maxX)
                maxX = coord.x();
            if (coord.y() > maxY)
                maxY = coord.y();
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (int y = minY; y <= maxY; y++) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (int x = minX; x <= maxX; x++) {
                Coordinate coord = Coordinate.of(x, y);
                builder.append(function.apply(coords.contains(coord), coord));
            }
        }

        return builder.toString();
    }

    public interface Coordinate2StringFunction {
        String apply(boolean contains, Coordinate coord);
    }

    public static <T> String toString(T[][] grid) {
        return toString(grid, Objects::toString);
    }

    public static <T> String toString(T[][] grid, Function<T, String> function) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (T[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (T value : row) {
                builder.append(function.apply(value));
            }
        }

        return builder.toString();
    }

    public static <T extends Enum<T> & EnumState<T>> String toString(T[][] grid) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (T[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (T value : row) {
                builder.append(value.getMappedChar());
            }
        }

        return builder.toString();
    }

    public static String toString(int[][] grid) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (int[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (int value : row) {
                builder.append(value);
            }
        }

        return builder.toString();
    }

    public static String toString(long[][] grid) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (long[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (long value : row) {
                builder.append(value);
            }
        }

        return builder.toString();
    }

    public static String toString(boolean[][] grid) {
        return toString(grid, b -> b ? "#" : ".");
    }

    public static String toString(boolean[][] grid, Boolean2ObjectFunction<String> function) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (boolean[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (boolean value : row) {
                builder.append(function.apply(value));
            }
        }

        return builder.toString();
    }

    public static String toString(char[][] grid) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (char[] row : grid) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            for (char value : row) {
                builder.append(value);
            }
        }

        return builder.toString();
    }
}

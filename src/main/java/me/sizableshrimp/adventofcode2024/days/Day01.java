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

package me.sizableshrimp.adventofcode2024.days;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import me.sizableshrimp.adventofcode2024.templates.Day;

public class Day01 extends Day {
    public static void main(String[] args) {
        new Day01().run();
    }

    @Override
    protected Result evaluate() {
        IntList left = new IntArrayList(this.lines.size());
        IntList right = new IntArrayList(this.lines.size());
        Int2IntMap occurrences = new Int2IntOpenHashMap();

        for (String line : this.lines) {
            int spaceIdx = line.indexOf(' ');
            int first = Integer.parseInt(line.substring(0, spaceIdx));
            int second = Integer.parseInt(line.substring(spaceIdx + 3));

            left.add(first);
            right.add(second);

            occurrences.mergeInt(second, 1, Integer::sum);
        }

        left.sort(IntComparators.NATURAL_COMPARATOR);
        right.sort(IntComparators.NATURAL_COMPARATOR);

        int distances = 0;
        int similarity = 0;

        for (int i = 0; i < left.size(); i++) {
            int first = left.getInt(i);
            int second = right.getInt(i);

            distances += Math.abs(first - second);
            similarity += first * occurrences.get(first);
        }

        return Result.of(distances, similarity);
    }
}

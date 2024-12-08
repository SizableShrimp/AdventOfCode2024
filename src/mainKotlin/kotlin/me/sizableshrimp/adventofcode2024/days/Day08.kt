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

package me.sizableshrimp.adventofcode2024.days

import me.sizableshrimp.adventofcode2024.helper.GridHelper
import me.sizableshrimp.adventofcode2024.helper.Itertools
import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.util.minus
import me.sizableshrimp.adventofcode2024.util.plus
import me.sizableshrimp.adventofcode2024.util.repeat
import me.sizableshrimp.adventofcode2024.util.toCharGrid

class Day08 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toCharGrid()
        val antennas = mutableMapOf<Char, MutableSet<Coordinate>>()
        for ((y, row) in grid.withIndex()) {
            for ((x, c) in row.withIndex()) {
                if (c == '.')
                    continue

                antennas.computeIfAbsent(c) { mutableSetOf() }.add(Coordinate.of(x, y))
            }
        }

        val part1 = mutableSetOf<Coordinate>()
        val part2 = mutableSetOf<Coordinate>()

        for ((_, coords) in antennas) {
            if (coords.size < 2) continue

            val combos = Itertools.combinations(coords, 2)
            for ((a, b) in combos) {
                val diff = a - b
                listOf(generateSequence(a) { it + diff }, generateSequence(b) { it - diff }).map { s ->
                    s.takeWhile { GridHelper.isValid(grid, it) }
                        .forEachIndexed { i, coord ->
                            // First element is the antenna itself, second element is the anode directly following it
                            if (i == 1) part1.add(coord)
                            part2.add(coord)
                        }
                }
            }
        }

        return Result.of(part1.size, part2.size)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}
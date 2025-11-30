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

import me.sizableshrimp.adventofcode2024.helper.Itertools
import me.sizableshrimp.adventofcode2024.templates.SeparatedDay
import me.sizableshrimp.adventofcode2024.util.*

class Day25 : SeparatedDay() {
    override fun part1(): Int {
        val grids = this.lines.splitOnBlankLines()
            .map { l -> l.toBooleanGrid { it == '#' } }
        val locks = grids.filter { it[0][0] }.map(::calcHeights)
        val keys = grids.filter { !it[0][0] }.map(::calcHeights)

        return Itertools.product(locks, keys).count { (l, k) ->
            l.allIndexed { i, h -> k[i] + h <= 5 }
        }
    }

    private fun calcHeights(grid: Array<BooleanArray>): List<Int> {
        val lock = grid[0][0]

        return grid[0].indices.map { x ->
            for (y in grid.indices) {
                if (grid[y][x] != lock)
                    return@map if (lock) y - 1 else grid.size - y - 1
            }

            error("")
        }
    }

    override fun part2() = null // No Part 2! :)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day25().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}
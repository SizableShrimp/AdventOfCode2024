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
import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.util.getCardinalOrdinalNeighbors
import me.sizableshrimp.adventofcode2024.util.getOrdinalNeighbors
import me.sizableshrimp.adventofcode2024.util.plus
import me.sizableshrimp.adventofcode2024.util.toCharGrid

class Day04 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toCharGrid()
        var part1 = 0
        var part2 = 0

        for (y in grid.indices) {
            val row = grid[y]
            for ((x, c) in row.withIndex()) {
                if (c == 'X') {
                    part1 += findXmas(grid, y, x)
                } else if (c == 'A') {
                    part2 += findCrossmas(grid, y, x)
                }
            }
        }

        return Result.of(part1, part2)
    }

    private fun findXmas(grid: Array<CharArray>, y: Int, x: Int): Int {
        val coord = Coordinate(x, y)
        var sum = 0

        for ((dir, second) in grid.getCardinalOrdinalNeighbors(coord)) {
            val third = second + dir
            val fourth = third + dir
            if (!GridHelper.isValid(grid, fourth))
                continue

            if (grid[second.y][second.x] == 'M' && grid[third.y][third.x] == 'A' && grid[fourth.y][fourth.x] == 'S')
                sum++
        }

        return sum
    }

    private fun findCrossmas(grid: Array<CharArray>, y: Int, x: Int): Int {
        val coord = Coordinate(x, y)
        var sum = 0

        for ((dir, neighbor) in grid.getOrdinalNeighbors(coord)) {
            val other = coord + dir.opposite()
            if (!GridHelper.isValid(grid, other))
                continue

            if (grid[neighbor.y][neighbor.x] == 'M' && grid[other.y][other.x] == 'S')
                sum++
        }

        return if (sum == 2) 1 else 0
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().run()
        }
    }
}
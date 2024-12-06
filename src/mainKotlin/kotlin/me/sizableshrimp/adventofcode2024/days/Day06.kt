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
import me.sizableshrimp.adventofcode2024.templates.Direction
import me.sizableshrimp.adventofcode2024.util.*

class Day06 : Day() {
    override fun evaluate(): Result {
        val startPos = this.lines.findCoord('^')!!
        val grid = this.lines.toBooleanGrid { it == '#' }
        grid[startPos] = false

        var guardPos = startPos
        val seen = mutableSetOf<Coordinate>()
        var dir = Direction.NORTH
        while (true) {
            seen.add(guardPos)
            val nextPos = guardPos + dir
            if (!GridHelper.isValid(grid, nextPos))
                break

            if (grid[nextPos]) {
                dir = dir.clockwise()
            } else {
                guardPos = nextPos
            }
        }

        var part2 = 0
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] || startPos == Coordinate.of(x, y))
                    continue

                grid[y][x] = true
                if (isLoop(grid, startPos))
                    part2++
                grid[y][x] = false
            }
        }

        return Result.of(seen.size, part2)
    }

    private fun isLoop(grid: Array<BooleanArray>, startPos: Coordinate): Boolean {
        var guardPos = startPos
        val seen = mutableSetOf<Pair<Coordinate, Direction>>()
        var dir = Direction.NORTH

        while (true) {
            if (!seen.add(guardPos to dir))
                return true

            val nextPos = guardPos + dir
            if (!GridHelper.isValid(grid, nextPos))
                return false

            if (grid[nextPos]) {
                dir = dir.clockwise()
            } else {
                guardPos = nextPos
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().run()
        }
    }
}
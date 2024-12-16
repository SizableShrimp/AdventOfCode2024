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

import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.templates.Direction
import me.sizableshrimp.adventofcode2024.util.*

class Day15 : Day() {
    override fun evaluate(): Result {
        val (gridStr, movesStr) = this.lines.splitOnBlankLines()
        val grid = gridStr.toCharGrid()
        val start = gridStr.findFirstCoord('@')?.let {
            grid[it] = '.'
            Coordinate.of(2 * it.x, it.y)
        }!!
        val moves = movesStr.joinToString("").map { Direction.getCardinalDirection(it) }

        val doubleGrid = Array(grid.size) { CharArray(grid[0].size * 2) }
        grid.forEach2D { x, y, c ->
            doubleGrid[y][2 * x] = if (c == 'O') '[' else c
            doubleGrid[y][2 * x + 1] = if (c == 'O') ']' else c
        }

        return Result.of(
            simulate(doubleGrid.deepCopy(), start, moves, false),
            simulate(doubleGrid, start, moves, true)
        )
    }

    private fun simulate(doubleGrid: Array<CharArray>, start: Coordinate, moves: List<Direction>, part2: Boolean): Int {
        var coord = start

        operator fun Coordinate.plus(move: Direction): Coordinate = if (part2) {
            this.resolve(move)
        } else {
            this.resolve(2 * move.x, move.y)
        }

        loop@ for (move in moves) {
            // doubleGrid[coord] = '@'
            // println(Printer.toString(doubleGrid))
            // doubleGrid[coord] = '.'

            val next = coord + move
            if (doubleGrid[next] == '#') continue
            val seen = mutableSetOf<Coordinate>()
            val boxes = mutableMapOf<Coordinate, Char>()
            val queue = ArrayDeque<Coordinate>()
            queue.add(next)

            while (!queue.isEmpty()) {
                val curr = queue.removeFirst()
                val c = doubleGrid[curr]
                if (c == '#') continue@loop
                if (c == '.') continue

                boxes[curr] = c

                if (seen.add(curr + move))
                    queue.add(curr + move)

                if (part2 && move.axis == Direction.Axis.Y) {
                    if (c == '[') {
                        if (seen.add(curr + Direction.EAST))
                            queue.add(curr + Direction.EAST)
                    } else if (seen.add(curr + Direction.WEST)) {
                        queue.add(curr + Direction.WEST)
                    }
                }
            }

            for ((box, _) in boxes) {
                doubleGrid[box] = '.'
            }
            for ((box, c) in boxes) {
                doubleGrid[box + move] = c
            }

            coord = next
        }

        return doubleGrid.sumOf2D { x, y, c ->
            if (c == '[') {
                (if (part2) x else x / 2) + 100 * y
            } else {
                0
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day15().run()
        }
    }
}
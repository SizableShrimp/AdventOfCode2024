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
import me.sizableshrimp.adventofcode2024.util.get
import me.sizableshrimp.adventofcode2024.util.plus
import me.sizableshrimp.adventofcode2024.util.toCharGrid

class Day12 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toCharGrid()
        val covered = mutableSetOf<Coordinate>()
        var part1 = 0
        var part2 = 0

        for ((y, row) in grid.withIndex()) {
            for ((x, c) in row.withIndex()) {
                val start = Coordinate(x, y)
                if (!covered.add(start)) continue
                val seen = mutableSetOf(start)
                val edges = mutableSetOf<Pair<Direction, Coordinate>>()
                val perimeter = calculatePerimeter(grid, c, start, seen, edges)
                val sides = calculateSides(edges)

                part1 += perimeter * seen.size
                part2 += sides * seen.size
                covered.addAll(seen)
            }
        }

        return Result.of(part1, part2)
    }

    private fun calculatePerimeter(
        grid: Array<CharArray>, type: Char, start: Coordinate,
        seen: MutableSet<Coordinate>, edges: MutableSet<Pair<Direction, Coordinate>>
    ): Int {
        var perimeter = 0
        val queue = ArrayDeque<Coordinate>()
        queue.add(start)

        while (!queue.isEmpty()) {
            val coord = queue.removeFirst()
            for (dir in Direction.cardinalDirections()) {
                val next = coord + dir
                if (GridHelper.isValid(grid, next) && grid[next] == type) {
                    if (next !in seen) {
                        seen.add(next)
                        queue.add(next)
                    }
                } else {
                    perimeter++
                    edges.add(dir to coord)
                }
            }
        }

        return perimeter
    }

    private fun calculateSides(edges: MutableSet<Pair<Direction, Coordinate>>): Int {
        var sides = 0
        val queue = ArrayDeque<Pair<Direction, Coordinate>>()
        val seen = mutableSetOf<Pair<Direction, Coordinate>>()

        while (!edges.isEmpty()) {
            val start = edges.first()
            edges.remove(start)
            seen.add(start)
            queue.add(start)
            sides++

            while (!queue.isEmpty()) {
                val (oldDir, coord) = queue.removeFirst()
                for (newDir in listOf(oldDir.clockwise(), oldDir.counterClockwise())) {
                    val next = coord + newDir
                    val pair = oldDir to next
                    if (!seen.add(pair)) continue
                    if (edges.remove(pair)) queue.add(pair)
                }
            }
        }

        return sides
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().run()
        }
    }
}
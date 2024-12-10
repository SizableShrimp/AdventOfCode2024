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

class Day10 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toIntGrid()
        val starts = this.lines.findAllCoords('0')
        var part1 = 0
        var part2 = 0

        for (start in starts) {
            // BFS
            val queue = ArrayDeque<Coordinate>()
            val visited = mutableSetOf<Pair<Direction, Coordinate>>()
            val nines = mutableSetOf<Coordinate>()
            val pathCounts = mutableMapOf<Coordinate, Int>()
            queue.add(start)
            for (dir in Direction.cardinalDirections()) {
                visited.add(dir to start)
            }
            pathCounts[start] = 1
            while (!queue.isEmpty()) {
                val coord = queue.removeFirst()
                val value = grid[coord]
                val pathCount = pathCounts[coord]!!
                for ((dir, neigh) in grid.getCardinalNeighbors(coord)) {
                    if (visited.contains(dir to neigh)) continue
                    val nextVal = grid[neigh]
                    if (nextVal == value + 1) {
                        visited.add(dir to neigh)
                        pathCounts[neigh] = (pathCounts[neigh] ?: 0) + pathCount
                        if (nextVal == 9) {
                            if (nines.add(neigh)) part1++
                            part2 += pathCount
                        } else {
                            queue.add(neigh)
                        }
                    }
                }
            }
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().run()
        }
    }
}
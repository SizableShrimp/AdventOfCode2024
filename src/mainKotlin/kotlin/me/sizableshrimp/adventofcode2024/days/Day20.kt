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
import me.sizableshrimp.adventofcode2024.util.*

class Day20 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toBooleanGrid { it == '#' }
        val target = this.lines.findFirstCoord('E')!!
        val (starts, ends) = listOf(this.lines.findFirstCoord('S')!!, target).map { t ->
            searchAll(t to 0, { it.first }, Comparator.comparingInt { it.second }) { (coord, step), addNext ->
                for ((_, next) in grid.getCardinalNeighbors(coord)) {
                    if (!grid[next]) addNext(next to (step + 1))
                }
            }
        }
        val minPath = starts[target]!!.second
        // val occurrences = sortedMapOf<Int, Int>()

        return listOf(2, 20).map { cheatSize ->
            grid.filter2D { coord, wall -> !wall && coord != target }.sumOf { (start, _) ->
                val cheatSearch = searchNoRepeats(State(start, false)) { s, addNext ->
                    if (s.coord == target) return@searchNoRepeats
                    for ((_, next) in grid.getCardinalNeighbors(s.coord)) {
                        if (start.distance(next) <= cheatSize)
                            addNext(State(next, s.cheated || grid[next]))
                    }
                }
                val validCheats = cheatSearch.filter { (coord, cheated) ->
                    cheated && coord != start && !grid[coord]
                }.map { it.coord }
                validCheats
                    .map { end -> starts[start]!!.second + ends[end]!!.second + start.distance(end) }
                    // .onEach { if (it < minPath) occurrences.compute(minPath - it) { _, v -> if (v == null) 1 else v + 1 } }
                    .count { it <= minPath - 100 }
            }
        }.toResult()
    }

    private data class State(val coord: Coordinate, val cheated: Boolean)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day20().run()
        }
    }
}
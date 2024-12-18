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

class Day18 : Day() {
    override fun evaluate(): Result {
        val coords = this.lines.map { Coordinate.parse(it) }
        val targetSize = 70
        val target = Coordinate(targetSize, targetSize)
        val grid = Array(targetSize + 1) { BooleanArray(targetSize + 1) }

        for (i in 0..<1024) {
            grid[coords[i]] = true
        }

        var part1 = 0
        var i = 1025
        while (true) {
            grid[coords[i]] = true

            val min = search(State(Coordinate(0, 0), 0), target, { it.coord }) { (coord, score), addNext ->
                for ((_, next) in grid.getCardinalNeighbors(coord)) {
                    if (!grid[next]) {
                        addNext(State(next, score + 1))
                    }
                }
            }.second

            if (part1 == 0) part1 = min!!.score
            if (min == null) break

            i++
        }

        println(i)
        return Result.of(part1, coords[i].let { (x,y) -> "$x,$y" })
    }

    private data class State(val coord: Coordinate, val score: Int) : Comparable<State> {
        override fun compareTo(other: State) = this.score.compareTo(other.score)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day18().run()
        }
    }
}
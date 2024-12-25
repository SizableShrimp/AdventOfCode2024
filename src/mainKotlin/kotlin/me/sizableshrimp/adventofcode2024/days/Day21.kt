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
import kotlin.math.sign

class Day21 : Day() {
    override fun evaluate(): Result = Result.of(simulate(3), simulate(26))

    private fun simulate(targetDepth: Int): Long {
        val recurse: (State) -> Long = searchMemoizing { (coord, depth, code, path), recurse ->
            if (depth == targetDepth) return@searchMemoizing code.length.toLong()
            if (code.isEmpty()) return@searchMemoizing 0L

            val pad = if (depth == 0) NUM_PAD else DIR_PAD
            if (pad.grid[coord] == 'x') return@searchMemoizing Long.MAX_VALUE
            if (code.isEmpty()) return@searchMemoizing 0L

            val target = pad.coordMap[code[0]]!!
            if (coord == target) {
                return@searchMemoizing recurse(State(DIR_PAD.start, depth + 1, path + 'A', "")) +
                        recurse(State(target, depth, code.substring(1), ""))
            }
            val dx = (target.x - coord.x).sign
            val dy = (target.y - coord.y).sign

            val xNext = if (dx == 0) Long.MAX_VALUE else recurse(State(coord.resolve(dx, 0), depth, code, path + Direction.parseDirection(dx, 0).charArrow))
            if (dy == 0) return@searchMemoizing xNext
            val yNext = recurse(State(coord.resolve(0, dy), depth, code, path + Direction.parseDirection(0, dy).charArrow))

            return@searchMemoizing minOf(xNext, yNext)
        }

        return this.lines.sumOf { it.substring(0, 3).toInt() * recurse(State(NUM_PAD.start, 0, it, "")) }
    }

    private data class State(val coord: Coordinate, val depth: Int, val code: String, val path: String)

    private class Keypad(val start: Coordinate, val grid: Array<CharArray>, val coordMap: Map<Char, Coordinate> = grid.map2D { coord, c -> c to coord }.toMap())

    companion object {
        private val NUM_PAD = Keypad(
            Coordinate(2, 3), arrayOf(
                charArrayOf('7', '8', '9'),
                charArrayOf('4', '5', '6'),
                charArrayOf('1', '2', '3'),
                charArrayOf('x', '0', 'A')
            )
        )
        private val DIR_PAD = Keypad(Coordinate(2, 0), arrayOf(charArrayOf('x', '^', 'A'), charArrayOf('<', 'v', '>')))

        @JvmStatic
        fun main(args: Array<String>) {
            Day21().run()
        }
    }
}
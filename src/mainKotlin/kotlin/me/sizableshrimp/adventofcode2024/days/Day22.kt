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

import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.util.*
import kotlin.math.floor

class Day22 : Day() {
    override fun evaluate(): Result {
        val starts = this.lines.toLongs()
        val mix = { i: Long, s: Long -> i xor s }
        var part1 = 0L
        val things = starts.map { start ->
            val prices = generateSequence(start) { s ->
                var state = s
                state = mix(state * 64, state).mod(16777216L)
                state = mix(state / 32, state).mod(16777216L)
                state = mix(state * 2048, state).mod(16777216L)
                state
            }.take(2001).mapIndexed { i, it ->
                if (i == 2000) part1 += it
                it.rem(10).toInt()
            }.toList()
            val change = prices.zipWithNext { a, b -> b - a }
            val after = change.asSequence().windowed(4).map { (a, b, c, d) ->
                (9 + a).shl(15) or (9 + b).shl(10) or (9 + c).shl(5) or (9 + d)
            }.toList()
            prices to after
        }
        val results = mutableMapOf<Int, Int>()
        var max = 0

        for ((a, changes) in things) {
            val seen = mutableSetOf<Int>()
            for ((i, c) in changes.withIndex()) {
                if (!seen.add(c)) continue
                val thatPrice = a[i + 4]
                val new = results.getOrDefault(c, 0) + thatPrice
                if (new > max) max = new
                results[c] = new
            }
        }

        return Result.of(part1, max)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day22().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}
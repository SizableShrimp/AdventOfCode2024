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

class Day11 : Day() {
    override fun evaluate(): Result {
        val stones = this.lines[0].split(" ").toLongs()
        val seen = mutableMapOf<Pair<Long, Int>, Long>()

        return listOf(25, 75).map { blinks -> stones.sumOf { sim(seen, it, blinks) } }.toResult()
    }

    private fun sim(seen: MutableMap<Pair<Long, Int>, Long>, stone: Long, step: Int): Long {
        if (step == 0) return 1
        seen[stone to step]?.let { return it }
        if (stone == 0L) return sim(seen, 1L, step - 1).also { seen[0L to step] = it }

        val stoneStr = stone.toString()

        return if (stoneStr.length % 2 == 0) {
            val half = stoneStr.length / 2
            val left = stoneStr.substring(0, half).toLong()
            val right = stoneStr.substring(half).toLong()
            sim(seen, left, step - 1) + sim(seen, right, step - 1)
        } else {
            sim(seen, stone * 2024, step - 1)
        }.also { seen[stone to step] = it }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().run()
        }
    }
}
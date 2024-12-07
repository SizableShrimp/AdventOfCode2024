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

class Day07 : Day() {
    override fun evaluate(): Result {
        val eqs = this.lines.map { line ->
            val (left, right) = line.split(": ")
            left.toLong() to right.split(" ").toLongs()
        }
        val part1 = eqs.map { (testVal, nums) ->
            hasMatch(nums.drop(1), nums.getLong(0), testVal, false)
        }
        val part2 = eqs.filterIndexed { i, (testVal, nums) ->
            part1[i] || hasMatch(nums.drop(1), nums.getLong(0), testVal, true)
        }.sumOf { (testVal, _) -> testVal }

        return Result.of(eqs.filterIndexed { i, _ -> part1[i] }.sumOf { (testVal, _) -> testVal }, part2)
    }

    private fun hasMatch(nums: List<Long>, prev: Long, target: Long, part2: Boolean): Boolean {
        if (nums.isEmpty()) return target == prev

        val skip = nums.drop(1)
        val num = nums[0]
        if (hasMatch(skip, prev * num, target, part2)) return true
        if (part2 && hasMatch(skip, prev * num.tenPowerDigitCount() * 10 + num, target, true)) return true
        if (hasMatch(skip, (if (prev == -1L) 0 else prev) + num, target, part2)) return true

        return false
    }

    private fun Long.tenPowerDigitCount(): Long = when {
        this == 0L -> 1L
        this < 0 -> (-this).tenPowerDigitCount()
        this < 10L -> 1L
        this < 100L -> 10L
        this < 1000L -> 100L
        this < 10000L -> 1000L
        this < 100000L -> 10000L
        this < 1000000L -> 100000L
        this < 10000000L -> 1000000L
        this < 100000000L -> 10000000L
        this < 1000000000L -> 100000000L
        this < 10000000000L -> 1000000000L
        this < 100000000000L -> 10000000000L
        this < 1000000000000L -> 100000000000L
        this < 10000000000000L -> 1000000000000L
        this < 100000000000000L -> 10000000000000L
        this < 1000000000000000L -> 100000000000000L
        this < 10000000000000000L -> 1000000000000000L
        this < 100000000000000000L -> 10000000000000000L
        this < 1000000000000000000L -> 100000000000000000L
        else -> 1000000000000000000L
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day07().run()
        }
    }
}
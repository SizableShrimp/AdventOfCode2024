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

import me.sizableshrimp.adventofcode2024.helper.OccurrenceHelper
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.util.toInts
import kotlin.math.abs

class Day01 : Day() {
    override fun evaluate(): Result {
        val (left, right) = this.lines.map {
            it.split("   ").toInts().let { (a, b) -> a to b }
        }.unzip()
        val occurrences = OccurrenceHelper.getOccurrences(right)

        return Result.of(
            left.sorted().zip(right.sorted()) { a, b -> abs(b - a) }.sum(),
            left.sumOf { occurrences.getLong(it) * it }
        )
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day01().run()
        }
    }
}
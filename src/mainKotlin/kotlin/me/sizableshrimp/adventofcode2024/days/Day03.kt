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
import me.sizableshrimp.adventofcode2024.util.toInts

class Day03 : Day() {
    override fun evaluate(): Result = this.lines.joinToString(separator = "")
        .let { listOf(it, it.replace(DONT_DO_REGEX, " ")) }
        .map { s -> MUL_REGEX.findAll(s).sumOf { it.groupValues.drop(1).toInts().reduce { a, b -> a * b } } }
        .let { (a, b) -> Result.of(a, b) }

    companion object {
        private val MUL_REGEX = Regex("""mul\((\d+?),(\d+?)\)""")
        private val DONT_DO_REGEX = Regex("""don't\(\).+?(do\(\)|$)""", RegexOption.DOT_MATCHES_ALL)

        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }
    }
}
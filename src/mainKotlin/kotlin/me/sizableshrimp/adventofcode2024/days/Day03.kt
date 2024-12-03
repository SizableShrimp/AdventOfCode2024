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
    override fun evaluate(): Result {
        val data = this.lines.joinToString(separator = "")
        val regex = Regex("mul\\((\\d+?),(\\d+?)\\)")
        var i = 0
        var doit = true
        var part1 = 0
        var part2 = 0

        while (true) {
            if (i > data.length) break
            val mul = regex.find(data, i)
            val mulidx = mul?.range?.start ?: -1
            val domatch = data.indexOf("do()", i)
            val dontmatch = data.indexOf("don't()", i)
            if (domatch != -1) {
                if ((dontmatch == -1 || domatch < dontmatch) && (mulidx == -1 || domatch < mulidx)) {
                    doit = true
                    i = domatch + 4
                    continue
                }
            }
            if (dontmatch != -1) {
                if ((domatch == -1 || dontmatch < domatch) && (mulidx == -1 || dontmatch < mulidx)) {
                    doit = false
                    i = dontmatch + 7
                    continue
                }
            }
            if (mulidx != -1) {
                if ((domatch == -1 || mulidx < domatch) && (dontmatch == -1 || mulidx < dontmatch)) {
                    val product = mul!!.groupValues.drop(1).toInts().let { (a, b) -> a * b }
                    part1 += product
                    if (doit)
                        part2 += product
                    i = mul.range.last
                    continue
                }
            }
            break
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }
    }
}
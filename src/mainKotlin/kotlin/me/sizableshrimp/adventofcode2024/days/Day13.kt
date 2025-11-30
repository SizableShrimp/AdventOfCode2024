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
import java.math.BigInteger

class Day13 : Day() {
    override fun evaluate(): Result {
        val machines = this.lines.splitOnBlankLines().map { l ->
            l.map { s ->
                s.substring(s.indexOf(':') + 2).split(", ").map {
                    it.substring(2)
                }.toLongs().map { BigInteger.valueOf(it) }.toBigIntPair()
            }
        }

        return listOf(false, true).map {
            machines.map { (aBtn, bBtn, prize) -> solve(it, aBtn, bBtn, prize) }
                .reduce { total, n -> total + n }
        }.toResult()
    }

    private data class BigIntPair(val x: BigInteger, val y: BigInteger)

    private fun solve(part2: Boolean, aBtn: BigIntPair, bBtn: BigIntPair, prize: BigIntPair): BigInteger {
        val px = if (part2) prize.x + PART2_OFFSET else prize.x
        val py = if (part2) prize.y + PART2_OFFSET else prize.y

        // Solve this system of linear equations:
        //  a_x*s + b_x*t = p_x
        //  a_y*s + b_y*t = p_y,
        // where s,t are the # of A and B button presses, respectively.

        // A solution exists if s,t are integral and positive.

        val tNum = aBtn.x * py - aBtn.y * px
        val tDenom = aBtn.x * bBtn.y - aBtn.y * bBtn.x

        if (tNum % tDenom != BigInteger.ZERO)
            return BigInteger.ZERO

        val t = tNum / tDenom

        if (t < BigInteger.ZERO)
            return BigInteger.ZERO

        val sNum = px - bBtn.x * t
        val sDenom = aBtn.x

        if (sNum % sDenom != BigInteger.ZERO)
            return BigInteger.ZERO

        val s = sNum / sDenom

        if (s < BigInteger.ZERO)
            return BigInteger.ZERO

        return THREE * s + t
    }

    companion object {
        private val PART2_OFFSET = BigInteger.valueOf(10000000000000L)
        private val THREE = BigInteger.valueOf(3L)

        @JvmStatic
        fun main(args: Array<String>) {
            Day13().run()
        }

        private fun List<BigInteger>.toBigIntPair(): BigIntPair {
            require(this.size == 2) { "List must have exactly 2 elements" }
            return BigIntPair(this[0], this[1])
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}
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

        return machines.map { (aBtn, bBtn, prize) -> runMachine(aBtn, bBtn, prize) }
            .reduce { l, r -> BigIntPair(l.x + r.x, l.y + r.y) }
            .let { (part1, part2) -> Result.of(part1, part2) }
    }

    private data class BigIntPair(val x: BigInteger, val y: BigInteger)

    private fun runMachine(aBtn: BigIntPair, bBtn: BigIntPair, prize: BigIntPair): BigIntPair {
        val (x1, x2, c) = extendedGcd(aBtn.x, bBtn.x)
        val (y1, y2, d) = extendedGcd(aBtn.y, bBtn.y)

        return listOf(false, true).map { solve(it, aBtn, bBtn, prize, x1, x2, c, y1, y2, d) }.toBigIntPair()
    }

    private fun solve(
        part2: Boolean, aBtn: BigIntPair, bBtn: BigIntPair, prize: BigIntPair,
        x1: BigInteger, x2: BigInteger, c: BigInteger,
        y1: BigInteger, y2: BigInteger, d: BigInteger
    ): BigInteger {
        val px = if (part2) prize.x + PART2_OFFSET else prize.x
        val py = if (part2) prize.y + PART2_OFFSET else prize.y

        if (px % c != BigInteger.ZERO || py % d != BigInteger.ZERO) {
            // Impossible to solve; there is no linear combination of one of the X or Y axis (or both)
            // that lands on the prize in that axis.
            // Such a linear combination existing for both axes is a necessary condition, but not a sufficient one.
            return BigInteger.ZERO
        }

        val kx = px / c
        val ky = py / d

        // Solve this system of linear equations:
        //  (A presses)  k_x*x_1 + (b_x/c)*s = k_y*y_1 + (b_y/d)*t
        //  (B presses)  k_x*x_2 - (a_x/c)*s = k_y*y_2 - (a_y/d)*t

        // val solveStr = "solve $kx*$x1 + (${bBtn.x}/$c)*s = $ky*$y1 + (${bBtn.y}/$d)*t, $kx*$x2 - (${aBtn.x}/$c)*s = $ky*$y2 - (${aBtn.y}/$d)*t"

        // s = (c/b_x) * (k_y*y_1 - k_x*x_1 + (b_y/d)*t)
        // k_x*x_2 - (a_x/c)*((c/b_x) * (k_y*y_1 - k_x*x_1 + (b_y/d)*t)) = k_y*y_2 - (a_y/d)*t
        // k_x*x_2 - (a_x/b_x)*(k_y*y_1 - k_x*x_1 + (b_y/d)*t) = k_y*y_2 - (a_y/d)*t
        // k_x*x_2 - (a_x/b_x)*(k_y*y_1 - k_x*x_1) - (a_x/b_x)*(b_y/d)*t = k_y*y_2 - (a_y/d)*t
        // (a_y/d)*t - (a_x/b_x)*(b_y/d)*t = k_y*y_2 - k_x*x_2 + (a_x/b_x)*(k_y*y_1 - k_x*x_1)
        // (a_y/d - (a_x/b_x)*(b_y/d))*t = k_y*y_2 - k_x*x_2 + (a_x/b_x)*(k_y*y_1 - k_x*x_1)
        // t = (k_y*y_2 - k_x*x_2 + (a_x/b_x)*(k_y*y_1 - k_x*x_1)) / (a_y/d - (a_x/b_x)*(b_y/d))
        // To avoid floating-point division: t = d*(b_x*(k_y*y_2 - k_x*x_2) + a_x*(k_y*y_1 - k_x*x_1)) / (b_x*a_y - a_x*b_y)
        val num = d * (bBtn.x * (ky * y2 - kx * x2) + aBtn.x * (ky * y1 - kx * x1))
        val denom = bBtn.x * aBtn.y - aBtn.x * bBtn.y

        if (num % denom != BigInteger.ZERO) {
            // Impossible to solve; there is no way to press the A and B buttons such that
            // the X and Y line up and land on the prize.
            return BigInteger.ZERO
        }

        val t = num / denom
        val aPresses = ky * y1 + (bBtn.y / d) * t
        val bPresses = ky * y2 - (aBtn.y / d) * t

        if (aPresses < BigInteger.ZERO || bPresses < BigInteger.ZERO)
            return BigInteger.ZERO // Impossible to solve; although on my input, this case never comes up

        return BigInteger.valueOf(3) * aPresses + bPresses
    }

    /**
     * Tracks Bézout's identity: ax + by = gcd(a, b)
     */
    private data class GcdResult(val x: BigInteger, val y: BigInteger, val gcd: BigInteger)

    private fun extendedGcd(a: BigInteger, b: BigInteger): GcdResult {
        if (a < b) return extendedGcd(b, a).let { GcdResult(it.y, it.x, it.gcd) }
        if (a == b || b == BigInteger.ZERO) return GcdResult(BigInteger.ONE, BigInteger.ZERO, a)

        // rem = a - floor(a/b)*b = a * 1 + b * -floor(a/b)
        val rem = a % b
        val quot = a / b
        // b * x + rem * y = gcd(b, a % b) = gcd(a, b)
        val res = extendedGcd(b, rem)

        // b * x + (a * 1 + b * -floor(a/b)) * y = gcd(a, b)
        // a * y + b * (x + y * -floor(a/b)) = gcd(a, b)
        return GcdResult(res.y, res.x + res.y * -quot, res.gcd)
    }

    companion object {
        private val PART2_OFFSET = BigInteger.valueOf(10000000000000L)

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
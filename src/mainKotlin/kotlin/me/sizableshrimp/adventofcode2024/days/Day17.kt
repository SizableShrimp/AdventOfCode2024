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

class Day17 : Day() {
    override fun evaluate(): Result {
        val (regsStr, programStr) = this.lines.joinToString("\n").split("\n\n")
        val regsStart = regsStr.split("\n").map { it.substringAfter(':').trim().toInt() }.toIntArray()
        val program = programStr.substringAfter(' ').split(',').toInts()

        // Assumptions that this solution makes for solving Part 2. x and y are variables that can change between inputs.
        check(program.size == 16)
        check(program.subList(0, 2) == listOf(2, 4)) // bst, B = A.mod(8)
        check(program.getInt(2) == 1) // bxl, B = B xor x
        val x = program.getInt(3)
        check(program.subList(4, 6) == listOf(7, 5)) // cdv, C = A / (2^B)
        val reorderable = program.subList(6, 12)
        val reorderableOps = reorderable.filterIndexed { i, _ -> i % 2 == 0 }
        check(reorderableOps.toSet() == setOf(0, 1, 4)) // adv, bxl, bxc
        check(reorderable.getInt(2 * reorderableOps.indexOf(0) + 1) == 3) // adv, A = A / 8
        // bxl, B = B xor y (do not need to check anything)
        // bxc, B = B xor C (do not need to check anything)
        val y = reorderable.getInt(2 * reorderableOps.indexOf(1) + 1)
        check(program.subList(12, 14) == listOf(5, 5)) // out, print(B.mod(8))
        check(program.subList(14, 16) == listOf(3, 0)) // jnz, if A != 0, jump to start of program

        val regs = regsStart.clone()
        val part1 = mutableListOf<Int>()
        var pc = 0
        while (pc in program.indices) {
            val opcode = program.getInt(pc)
            val operand = program.getInt(pc + 1)
            when (opcode) {
                // adv
                0 -> {
                    regs[0] = regs[0] shr getCombo(regs, operand)
                }
                // bxl
                1 -> {
                    regs[1] = operand xor regs[1]
                }
                // bst
                2 -> {
                    regs[1] = getCombo(regs, operand).mod(8)
                }
                // jnz
                3 -> {
                    if (regs[0] != 0) {
                        pc = operand
                        continue
                    }
                }
                // bxc
                4 -> {
                    regs[1] = regs[1] xor regs[2]
                }
                // out
                5 -> {
                    part1.add(getCombo(regs, operand).mod(8))
                }
                // bdv
                6 -> {
                    regs[1] = regs[0] shr getCombo(regs, operand)
                }
                // cdv
                7 -> {
                    regs[2] = regs[0] shr getCombo(regs, operand)
                }
            }

            pc += 2
        }

        val queue = ArrayDeque<State>()
        queue.add(State(15, 0))
        val part2 = mutableSetOf<Long>()

        while (!queue.isEmpty()) {
            val state = queue.removeFirst()
            for (i in 0L..7L) {
                val a = (state.a shl 3) or i
                val b = a.mod(8) xor x
                val temp = a shr b
                val output = (b xor y xor temp.mod(8)).mod(8)

                if (output == program.getInt(state.idx)) {
                    if (state.idx == 0) {
                        part2.add(a)
                    } else {
                        queue.add(State(state.idx - 1, a))
                    }
                }
            }
        }

        return Result.of(part1.joinToString(","), part2.min())
    }

    private fun getCombo(regs: IntArray, operand: Int) = if (operand in 0..3) operand else regs[operand - 4]

    data class State(val idx: Int, val a: Long)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day17().run()
        }
    }
}
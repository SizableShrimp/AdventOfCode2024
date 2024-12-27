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

class Day24 : Day() {
    override fun evaluate(): Result {
        val (start, gatesStr) = this.lines.splitOnBlankLines()
        val dependents = mutableMapOf<String, MutableSet<String>>()
        val gates = mutableMapOf<String, Gate>()
        gatesStr.map { it.split(" -> ") }.forEach { (left, output) ->
            val (a, op, b) = left.split(' ')
            val gate = Gate(output, setOf(a, b), Operation.valueOf(op))
            gates[output] = gate
            dependents.getOrPut(a) { mutableSetOf() }.add(output)
            dependents.getOrPut(b) { mutableSetOf() }.add(output)
        }
        val values = mutableMapOf<String, Int>()
        start.map { it.split(": ") }.forEach { (id, n) ->
            values[id] = n.toInt()
        }
        val part1 = simulate(values, gates, dependents)

        val numInputBits = start.size / 2
        var carryGateIn: String? = null
        val swappedGates = mutableSetOf<String>()
        val swapGates = { a: String, b: String ->
            swappedGates.add(a)
            swappedGates.add(b)

            val aGate = gates[a]!!
            val bGate = gates[b]!!
            gates[a] = bGate
            gates[b] = aGate
            aGate.inputs.forEach { i ->
                dependents[i]!!.also {
                    it.remove(a)
                    it.add(b)
                }
            }
            bGate.inputs.forEach { i ->
                dependents[i]!!.also {
                    it.remove(b)
                    it.add(a)
                }
            }
        }
        val checkGateEquals = { expected: String, actual: String ->
            if (actual != expected)
                swapGates(actual, expected)
        }

        // While the puzzle prose states that the swaps "could be anywhere", it appears that Eric was nice
        // (at least on a sample size of 3 inputs) and did not truly put swaps anywhere.
        // Here are the assumptions my code makes (as it works for my inputs and makes the solution significantly easier):
        // - There is at most one pair of swapped wires in the binary adder for a given bit.
        // - All swapped wires are swapped within a single binary adder for a given bit.
        //
        // These assumptions mean that two swapped wires will be within a single bit binary adder,
        // and a single bit binary adder will have 0 or 1 pair(s) of swapped wires.

        for (i in 0..<numInputBits) {
            val numStr = i.toString().padStart(2, '0')
            // Only the output wires of the gates can be swapped, so the inputs and operations must stay the same for all gates.
            // This means that for a given "known-good" wire, we can skip checking the operations done by the gates.
            // Since the inputs can't be swapped, we also know that the number and types of dependent gates cannot change either.
            var (andInputs, xorInputs) = dependents["x$numStr"]!!.sortedBy { gates[it]!! }
            if (carryGateIn == null) {
                // This is the least significant bit (LSB); there is no carry gate yet,
                // so it's a special case with a reduced number of gates.
                // Based on our assumption, these gates can't be crossed because crossing the two of them would
                // not affect anything.
                carryGateIn = andInputs
                checkGateEquals("z00", xorInputs)
                continue
            }
            // By our assumption of swaps only being inside a single bit binary adder,
            // we assume that the input carry gate is correct at this point
            // since it is the output of a lower-significance single bit binary adder
            // and since we would have already fixed it during that prior iteration.

            if (dependents[xorInputs]?.let { it.size == 2 && it == dependents[carryGateIn] } != true) {
                // If we got here, then the XOR of the inputs must be crossed with another wire.
                // The XOR of the inputs and the input carry gate are used as inputs themselves to two other gates.
                // By our assumptions, we can use the dependents of the input carry gate to fix the swapped XOR of the inputs.

                // Get one of the two dependent gates of the input carry gate. (Either would work as they have the same inputs.)
                val goodGate = gates[dependents[carryGateIn]!!.first()]!!
                // Find the other wire that is an input to the known-good gate besides the input carry gate;
                // this is the wire we need to uncross with the XOR of the inputs.
                val correctGate = (goodGate.inputs - carryGateIn).first()

                // Uncross the wires
                swapGates(xorInputs, correctGate)
                xorInputs = correctGate
            }
            // At this point, xorInputs is known good by our assumptions.

            // Check the final XOR gate is an output bit, or uncross it if not.
            val xorOutGate = dependents[xorInputs]!!.maxBy { gates[it]!! }
            checkGateEquals("z$numStr", xorOutGate)

            // At this point, everything has been fixed.
            // The last thing to do is find the OR gate that serves as the output carry gate
            // of this single bit binary adder and store it for the next iteration.
            val andCarryGate = dependents[xorInputs]!!.minBy { gates[it]!! }
            val orCarryGate = dependents[andCarryGate]!!.first()
            carryGateIn = orCarryGate

            // If this is the most significant bit (MSB), check the output carry gate is another output bit itself.
            // (Based on our assumptions and this being the final gate to check, this shouldn't really need checking.)
            if (i == numInputBits - 1) {
                val maxNumStr = numInputBits.toString().padStart(2, '0')
                checkGateEquals("z$maxNumStr", orCarryGate)
            }
        }

        return Result.of(part1, swappedGates.sorted().joinToString(","))
    }

    private fun simulate(
        values: MutableMap<String, Int>,
        gates: Map<String, Gate>,
        dependents: Map<String, Set<String>>
    ): Long {
        val queue = ArrayDeque<String>()
        queue.addAll(values.keys)

        while (!queue.isEmpty()) {
            val curr = queue.removeFirst()
            if (gates.containsKey(curr)) {
                val gate = gates[curr]!!
                if (!gate.inputs.all { values.containsKey(it) }) continue
                val (a, b) = gate.inputs.map { values[it]!! }

                values[curr] = when (gate.op) {
                    Operation.AND -> a and b
                    Operation.OR -> a or b
                    Operation.XOR -> a xor b
                }
            }

            dependents[curr]?.also { queue.addAll(it) }
        }

        return values.entries.filter { it.key[0] == 'z' }
            .sortedByDescending { it.key }
            .map { it.value }
            .joinToString("")
            .toLong(2)
    }

    private data class Gate(val output: String, val inputs: Set<String>, val op: Operation) : Comparable<Gate> {
        override fun compareTo(other: Gate) = this.op.compareTo(other.op)
    }

    private enum class Operation { AND, OR, XOR }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day24().run()
        }
    }
}
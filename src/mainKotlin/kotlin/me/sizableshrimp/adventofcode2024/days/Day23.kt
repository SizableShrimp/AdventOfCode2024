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

class Day23 : Day() {
    override fun evaluate(): Result {
        val nodes = mutableMapOf<String, Node>()
        this.lines.map { it.split("-").toPair() }.forEach { (a, b) ->
            nodes.getOrPut(a) { Node(a, mutableSetOf()) }.others.add(b)
            nodes.getOrPut(b) { Node(b, mutableSetOf()) }.others.add(a)
        }
        val seen = mutableSetOf<Set<String>>()
        var part2 = setOf<Node>()

        for (n in nodes.values) {
            for (o in n.others) {
                val other = nodes[o]!!
                for (p in other.others) {
                    if (n.others.contains(p)) {
                        if (n.id[0] == 't' || o[0] == 't' || p[0] == 't') {
                            seen.add(setOf(n.id, o, p))
                        }
                    }
                }
            }

            val maxGroup = searchNoRepeats(n to setOf(n)) { (curr, s), addNext ->
                for (o in curr.others) {
                    if (s.all { it.others.contains(o) }) {
                        val next = nodes[o]!!
                        addNext(next to (s + next))
                    }
                }
            }.maxBy { it.second.size }.second
            if (maxGroup.size > part2.size) part2 = maxGroup
        }

        return Result.of(seen.size, part2.map { it.id }.sorted().joinToString(","))
    }

    private data class Node(val id: String, val others: MutableSet<String>)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day23().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}
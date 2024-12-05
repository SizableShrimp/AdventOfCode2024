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

class Day05 : Day() {
    override fun evaluate(): Result {
        val (pairs, rules) = this.rawInput.split("\n\n")
        val nums = pairs.split("\n").map { it.split("|").toInts().let { (a, b) -> a to b } }
        val realRules = rules.split("\n").map { it.split(",").toInts() }
        var part1 = 0
        var part2 = 0

        for (rule in realRules) {
            val uniques = rule.distinct()
            val realNums = nums.filter { (a, b) -> uniques.contains(a) && uniques.contains(b) }
            val map = mutableMapOf<Int, Set<Int>>()
            for (num in uniques) {
                // Do a DFS to find all reachable nodes in nums and add them to map
                val reachable = mutableSetOf<Int>()
                dfs(num, realNums, reachable)
                map[num] = reachable
            }

            val expectedRule = map.entries.sortedByDescending { (_, v) -> v.size }.map { (k, _) -> k }
            if (expectedRule == rule)
                part1 += rule.getInt(rule.size / 2)
            else
                part2 += expectedRule[rule.size / 2]
        }

        return Result.of(part1, part2)
    }

    private fun dfs(start: Int, nums: List<Pair<Int, Int>>, visited: MutableSet<Int>) {
        visited.add(start)
        for ((a, b) in nums) {
            if (a == start && b !in visited) {
                dfs(b, nums, visited)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().run()
        }
    }
}
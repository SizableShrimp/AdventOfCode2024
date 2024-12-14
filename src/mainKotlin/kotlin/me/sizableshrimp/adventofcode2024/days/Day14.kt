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

import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.templates.Direction
import me.sizableshrimp.adventofcode2024.util.*

class Day14 : Day() {
    override fun evaluate(): Result {
        val dimensions = Coordinate(101, 103)
        val (startRobots, velocities) = this.lines.map { line ->
            line.split(" ").map { it.substringAfter("=").split(",").toInts().let { (a, b) -> Coordinate(a, b) } }.toPair()
        }.unzip()
        val robotSeq = {
            generateSequence(startRobots) { robots ->
                robots.mapIndexed { i, pos -> pos.addMod(velocities[i], dimensions) }
            }
        }

        val part1 = robotSeq().elementAt(100)
            .filter { pos -> pos.x != dimensions.x / 2 && pos.y != dimensions.y / 2 }
            .groupingBy { pos -> (pos.x >= dimensions.x / 2) to (pos.y >= dimensions.y / 2) }
            .eachCount()
            .values
            .reduce { a, b -> a * b }
        val part2 = robotSeq().map { it.toMutableSet() }.takeWhile { robots ->
            val queue = ArrayDeque<Coordinate>()
            val seen = mutableSetOf<Coordinate>()
            while (!robots.isEmpty()) {
                seen.clear()
                queue.add(robots.first())
                while (!queue.isEmpty()) {
                    val current = queue.removeFirst()
                    robots.remove(current)
                    for (dir in Direction.cardinalDirections()) {
                        val next = current.addMod(dir, dimensions)
                        if (next !in robots) continue
                        if (seen.add(next)) queue.add(next)
                    }
                }
                if (seen.size >= 100) {
                    // Tree found (probably)
                    return@takeWhile false
                }
            }

            return@takeWhile true
        }.count()

        return Result.of(part1, part2)
    }

    private fun Coordinate.addMod(other: Coordinate, dimensions: Coordinate): Coordinate {
        return Coordinate((this.x + other.x).mod(dimensions.x), (this.y + other.y).mod(dimensions.y))
    }

    private fun Coordinate.addMod(other: Direction, dimensions: Coordinate): Coordinate {
        return Coordinate((this.x + other.x).mod(dimensions.x), (this.y + other.y).mod(dimensions.y))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day14().run()
        }
    }
}
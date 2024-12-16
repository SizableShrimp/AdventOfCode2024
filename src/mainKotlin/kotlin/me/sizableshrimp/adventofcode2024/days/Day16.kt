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

import me.sizableshrimp.adventofcode2024.helper.GridHelper
import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Day
import me.sizableshrimp.adventofcode2024.templates.Direction
import me.sizableshrimp.adventofcode2024.util.*

class Day16 : Day() {
    override fun evaluate(): Result {
        val grid = this.lines.toBooleanGrid { it == '#' }
        val startCoord = this.lines.findFirstCoord('S')!!
        val startDir = Direction.EAST
        val target = this.lines.findFirstCoord('E')!!
        val startState = State(startCoord, startDir, 0, setOf(startCoord))
        val seen = mutableMapOf<Pair<Coordinate, Direction>, Int>()
        val queue = ArrayDeque<State>()
        val bestPaths = mutableSetOf<State>()
        queue.add(startState)
        var minPoints = Int.MAX_VALUE

        while (!queue.isEmpty()) {
            val state = queue.removeFirst()
            if (state.score >= minPoints) continue
            if (seen.containsKey(state.coord to state.dir) && seen[state.coord to state.dir]!! < state.score) {
                continue
            }

            val next = state.coord + state.dir
            if (GridHelper.isValid(grid, next) && !grid[next]) {
                val nextState = State(next, state.dir, state.score + 1, state.path + next)
                if (nextState.score <= minPoints && (next == target || !seen.containsKey(next to state.dir) || seen[next to state.dir]!! > nextState.score)) {
                    if (next == target) {
                        if (nextState.score > minPoints) {
                            continue
                        } else if (nextState.score < minPoints) {
                            minPoints = minOf(minPoints, nextState.score)
                        }
                    } else {
                        seen[next to state.dir] = nextState.score
                        queue.add(nextState)
                    }
                }
            }

            // Try turn clockwise
            var nextDir = state.dir.clockwise()
            val nextScore = state.score + 1000
            if (nextScore < minPoints && (!seen.containsKey(state.coord to nextDir) || seen[state.coord to nextDir]!! > nextScore)) {
                val nextState = State(state.coord, nextDir, nextScore, state.path)
                seen[state.coord to state.dir] = nextState.score
                queue.add(nextState)
            }

            // Try turn counterclockwise
            nextDir = state.dir.counterClockwise()
            if (nextScore < minPoints && (!seen.containsKey(state.coord to nextDir) || seen[state.coord to nextDir]!! > nextScore)) {
                val nextState = State(state.coord, nextDir, nextScore, state.path)
                seen[state.coord to state.dir] = nextState.score
                queue.add(nextState)
            }
        }
        queue.add(startState)

        while (!queue.isEmpty()) {
            val state = queue.removeFirst()
            if (state.score >= minPoints) continue
            if (seen.containsKey(state.coord to state.dir) && seen[state.coord to state.dir]!! < state.score) {
                continue
            }

            val next = state.coord + state.dir
            if (GridHelper.isValid(grid, next) && !grid[next]) {
                val nextState = State(next, state.dir, state.score + 1, state.path + next)
                if (nextState.score <= minPoints && (next == target || !seen.containsKey(next to state.dir) || seen[next to state.dir]!! >= nextState.score)) {
                    if (next == target) {
                        bestPaths.add(state)
                    } else {
                        seen[next to state.dir] = nextState.score
                        queue.add(nextState)
                    }
                }
            }

            // Try turn clockwise
            var nextDir = state.dir.clockwise()
            val nextScore = state.score + 1000
            if (nextScore < minPoints && (!seen.containsKey(state.coord to nextDir) || seen[state.coord to nextDir]!! >= nextScore)) {
                val nextState = State(state.coord, nextDir, nextScore, state.path)
                seen[state.coord to state.dir] = nextState.score
                queue.add(nextState)
            }

            // Try turn counterclockwise
            nextDir = state.dir.counterClockwise()
            if (nextScore < minPoints && (!seen.containsKey(state.coord to nextDir) || seen[state.coord to nextDir]!! >= nextScore)) {
                val nextState = State(state.coord, nextDir, nextScore, state.path)
                seen[state.coord to state.dir] = nextState.score
                queue.add(nextState)
            }
        }

        return Result.of(minPoints, bestPaths.flatMapTo(mutableSetOf()) { it.path }.size + 1)
    }

    private data class State(val coord: Coordinate, val dir: Direction, val score: Int, val path: Set<Coordinate>)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day16().run()
        }
    }
}
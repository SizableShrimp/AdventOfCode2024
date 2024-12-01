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

package me.sizableshrimp.adventofcode2024.util

import me.sizableshrimp.adventofcode2024.helper.GridHelper
import me.sizableshrimp.adventofcode2024.templates.Coordinate
import me.sizableshrimp.adventofcode2024.templates.Direction

inline fun <reified T> convertToGrid(lines: List<String>, func: (Char) -> T) =
    Array(lines.size) { y -> Array(lines[y].length) { x -> func(lines[y][x]) } }

fun List<String>.toCharGrid(): Array<CharArray> = GridHelper.createCharGrid(this)

/**
 * Creates a grid of single-digit integers based on the integer character
 * at each point in the provided grid.
 *
 * @return the 2D grid of single-digit integers
 */
fun List<String>.toIntGrid(): Array<IntArray> = GridHelper.createIntGrid(this)

fun <T> Array<Array<T>>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun <T> Array<Array<T>>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<IntArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<IntArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<LongArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<LongArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<CharArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<CharArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<ByteArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<ByteArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<DoubleArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<DoubleArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<FloatArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<FloatArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<BooleanArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<BooleanArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}
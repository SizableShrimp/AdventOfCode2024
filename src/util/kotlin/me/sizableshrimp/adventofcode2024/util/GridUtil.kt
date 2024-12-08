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

inline fun <reified T> List<String>.toGrid(func: (Char) -> T) =
    Array(this.size) { y -> Array(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toIntGrid(func: (Char) -> Int) =
    Array(this.size) { y -> IntArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toLongGrid(func: (Char) -> Long) =
    Array(this.size) { y -> LongArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toCharGrid(func: (Char) -> Char) =
    Array(this.size) { y -> CharArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toByteGrid(func: (Char) -> Byte) =
    Array(this.size) { y -> ByteArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toDoubleGrid(func: (Char) -> Double) =
    Array(this.size) { y -> DoubleArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toFloatGrid(func: (Char) -> Float) =
    Array(this.size) { y -> FloatArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toBooleanGrid(func: (Char) -> Boolean) =
    Array(this.size) { y -> BooleanArray(this[y].length) { x -> func(this[y][x]) } }

inline fun <reified T> List<String>.toGridWithCoord(func: (Coordinate, Char) -> T) =
    Array(this.size) { y -> Array(this[y].length) { x -> func(Coordinate.of(x, y), this[y][x]) } }

fun List<String>.findFirstCoord(target: Char): Coordinate? {
    for ((y, row) in this.withIndex()) {
        for ((x, c) in row.withIndex()) {
            if (c == target)
                return Coordinate.of(x, y)
        }
    }

    return null
}

fun List<String>.findAllCoords(target: Char): Set<Coordinate> {
    val set = mutableSetOf<Coordinate>()

    for ((y, row) in this.withIndex()) {
        for ((x, c) in row.withIndex()) {
            if (c == target)
                set.add(Coordinate.of(x, y))
        }
    }

    return set
}

fun List<String>.toCharGrid(): Array<CharArray> = GridHelper.createCharGrid(this)

/**
 * Creates a grid of single-digit integers based on the integer character
 * at each point in the provided grid.
 *
 * @return the 2D grid of single-digit integers
 */
fun List<String>.toIntGrid(): Array<IntArray> = GridHelper.createIntGrid(this)

inline operator fun <reified T> Array<Array<T>>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<IntArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<LongArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<CharArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<ByteArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<DoubleArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<FloatArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<BooleanArray>.get(coord: Coordinate) = this[coord.y][coord.x]

inline operator fun <reified T> Array<Array<T>>.set(coord: Coordinate, value: T) {
    this[coord.y][coord.x] = value
}

operator fun Array<IntArray>.set(coord: Coordinate, value: Int) {
    this[coord.y][coord.x] = value
}

operator fun Array<LongArray>.set(coord: Coordinate, value: Long) {
    this[coord.y][coord.x] = value
}

operator fun Array<CharArray>.set(coord: Coordinate, value: Char) {
    this[coord.y][coord.x] = value
}

operator fun Array<ByteArray>.set(coord: Coordinate, value: Byte) {
    this[coord.y][coord.x] = value
}

operator fun Array<DoubleArray>.set(coord: Coordinate, value: Double) {
    this[coord.y][coord.x] = value
}

operator fun Array<FloatArray>.set(coord: Coordinate, value: Float) {
    this[coord.y][coord.x] = value
}

operator fun Array<BooleanArray>.set(coord: Coordinate, value: Boolean) {
    this[coord.y][coord.x] = value
}

fun <T> Array<Array<T>>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun <T> Array<Array<T>>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<IntArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<LongArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<CharArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<ByteArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<DoubleArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<FloatArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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

fun Array<BooleanArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
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
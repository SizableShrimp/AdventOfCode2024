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
import java.util.LinkedList

class Day09 : Day() {
    override fun evaluate(): Result {
        val list = mutableListOf<Int>()
        var newList = LinkedList<Pair<Int, Int>>()
        var ptr = 0
        var id = 0
        for ((i, c) in this.lines[0].withIndex()) {
            val free = i % 2 == 1
            val num = c - '0'
            if (free) {
                for (j in 0..<num) {
                    list.add(-1)
                }
                if (num != 0)
                    newList.add(-num to num)
                ptr += num
            } else {
                for (j in 0..<num) {
                    list.add(id)
                }
                if (num != 0)
                    newList.add(id to num)
                id++
                ptr += num
            }
        }
        var left = 0
        var right = list.size - 1
        while (right >= left) {
            if (list[right] == -1) {
                right--
                continue
            }
            while (list[left] != -1) left++
            list[left] = list[right]
            list[right] = -1
            right--
        }
        list[left - 1] = list[left]
        list[left] = -1

        // Loop in newList from last to earlier but using ptrs or something
        val iter = newList.descendingIterator()
        val nextList = LinkedList(newList)
        var k = newList.size - 1
        while (iter.hasNext()) {
            val (entry, len) = iter.next()
            if (entry < 0) {
                k--
                continue
            }
            val newIter = nextList.listIterator()
            var moved = false
            while (newIter.hasNext()) {
                val (newEntry, newLen) = newIter.next()
                if (newEntry < 0 && newLen >= len) {
                    if (newLen == len) {
                        newIter.set(entry to len)
                    } else {
                        // Move max in then add free block after
                        newIter.set(entry to len)
                        newIter.add(-(newLen - len) to newLen - len)
                    }
                    moved = true
                    break
                }
            }
            k--
        }

        var sum = 0L
        var i = 0
        val seen = mutableSetOf<Int>()
        for ((id, len) in nextList) {
            if (id < 0 || !seen.add(id)) {
                i += len
                continue
            }
            for (j in 0..<len) {
                sum += id * i
                i++
            }
        }

        return Result.of(checksum(list), sum)
    }

    private fun checksum(list: List<Int>): Long {
        var sum = 0L
        for ((i, num) in list.withIndex()) {
            if (num == -1) break
            sum += i * num
        }
        return sum
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().run()
        }
    }
}
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

package me.sizableshrimp.adventofcode2024.days;

import me.sizableshrimp.adventofcode2024.templates.Day;

public class Day03 extends Day {
    private char[] data;

    public static void main(String[] args) {
        new Day03().run();
    }

    @Override
    protected Result evaluate() {
        int part1 = 0;
        int part2 = 0;
        boolean enabled = true;
        int mulStartIdx = -1;
        boolean parseNum = false;
        boolean comma = false;
        int doStartIdx = -1;
        int dontStartIdx = -1;
        int firstNum = 0;
        int num = 0;

        for (int i = 0; i < this.data.length; i++) {
            char c = this.data[i];

            if (c == 'm') {
                mulStartIdx = i;
                parseNum = false;
                comma = false;
                doStartIdx = -1;
                dontStartIdx = -1;
                firstNum = 0;
                num = 0;
            } else if (c == 'd') {
                mulStartIdx = -1;
                doStartIdx = i;
                dontStartIdx = -1;
                parseNum = false;
            }

            if (doStartIdx != -1 && doStartIdx != i) {
                if (doStartIdx == i - 1) {
                    if (c != 'o')
                        doStartIdx = -1;
                } else if (doStartIdx == i - 2) {
                    if (c == 'n') {
                        dontStartIdx = doStartIdx;
                        doStartIdx = -1;
                    } else if (c != '(') {
                        doStartIdx = -1;
                    }
                } else if (doStartIdx == i - 3) {
                    doStartIdx = -1;

                    if (c == ')')
                        enabled = true;
                }
            }

            if (dontStartIdx != -1 && dontStartIdx != i) {
                if (dontStartIdx == i - 3) {
                    if (c != '\'')
                        dontStartIdx = -1;
                } else if (dontStartIdx == i - 4) {
                    if (c != 't')
                        dontStartIdx = -1;
                } else if (dontStartIdx == i - 5) {
                    if (c != '(')
                        dontStartIdx = -1;
                } else if (dontStartIdx == i - 6) {
                    dontStartIdx = -1;

                    if (c == ')')
                        enabled = false;
                }
            }

            if (parseNum) {
                if (c >= '0' && c <= '9') {
                    num = num * 10 + c - '0';
                } else if (c == ',' || c == ')') {
                    parseNum = false;

                    if ((c == ',' && comma) || (c == ')' && !comma)) {
                        mulStartIdx = -1;
                        num = 0;
                    } else {
                        if (comma) {
                            int mul = firstNum * num;
                            part1 += mul;
                            if (enabled)
                                part2 += mul;
                            firstNum = 0;
                        } else {
                            firstNum = num;
                            comma = true;
                            parseNum = true;
                        }
                        num = 0;
                    }
                } else {
                    parseNum = false;
                    mulStartIdx = -1;
                    num = 0;
                }
            }

            if (mulStartIdx != -1 && mulStartIdx != i) {
                if (mulStartIdx == i - 1) {
                    if (c != 'u')
                        mulStartIdx = -1;
                } else if (mulStartIdx == i - 2) {
                    if (c != 'l')
                        mulStartIdx = -1;
                } else if (mulStartIdx == i - 3) {
                    mulStartIdx = -1;
                    parseNum = true;
                }
            }
        }

        return Result.of(part1, part2);
    }

    @Override
    protected void parse() {
        this.data = String.join("", this.lines).toCharArray();
    }
}

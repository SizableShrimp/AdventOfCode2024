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

package me.sizableshrimp.adventofcode2024.templates;

import me.sizableshrimp.adventofcode2024.helper.GridHelper;

/**
 * A 2-dimensional coordinate object that holds an x and y value.
 */
public record Coordinate(int x, int y) {
    public static final Coordinate ORIGIN = new Coordinate(0, 0);

    public static Coordinate of(int x, int y) {
        return new Coordinate(x, y);
    }

    public static Coordinate of(double x, double y) {
        return new Coordinate((int) x, (int) y);
    }

    /**
     * Parses a coordinate in the format "x,y".
     *
     * @param coord The input string of which to parse a coordinate.
     * @return A new {@link Coordinate} object.
     */
    public static Coordinate parse(String coord) {
        int commaIdx = coord.indexOf(',');
        int x = Integer.parseInt(coord.substring(0, commaIdx));
        int y = Integer.parseInt(coord.substring(commaIdx + 1));
        return new Coordinate(x, y);
    }

    public Coordinate resolve(Coordinate other) {
        return resolve(other.x, other.y);
    }

    /**
     * Creates a new {@link Coordinate} based on the offset of x and y given in parameters added to the current
     * coordinate.
     *
     * @param x The offset-x to add to the coordinate x.
     * @param y The offset-y to add to the coordinate y.
     * @return A new {@link Coordinate} made from adding the offset-x to the base x and offset-y to the base y.
     */
    public Coordinate resolve(int x, int y) {
        if (x == 0 && y == 0)
            return this;

        return new Coordinate(this.x + x, this.y + y);
    }

    public Coordinate multiply(Coordinate mut) {
        return multiply(mut.x, mut.y);
    }

    public Coordinate multiply(int xFactor, int yFactor) {
        if (xFactor == 1 && yFactor == 1)
            return this;

        return new Coordinate(this.x * xFactor, this.y * yFactor);
    }

    public Coordinate multiply(int factor) {
        return multiply(factor, factor);
    }

    /**
     * Rotates an (x,y) {@link Coordinate} around the {@link Coordinate#ORIGIN} by {@code degrees} which is a multiple of 90.
     *
     * @param degrees A degree value that is a multiple of 90.
     * @return A rotated {@link Coordinate}.
     */
    public Coordinate rotate90(int degrees) {
        if (degrees < 0)
            degrees = 360 + degrees;
        degrees %= 360;

        return switch (degrees) {
            case 90 -> this.swapXY().multiply(-1, 1);
            case 180 -> this.multiply(-1, -1);
            case 270 -> this.swapXY().multiply(1, -1);
            case 0 -> this;
            default -> throw new IllegalStateException("Degrees is not a multiple of 90: " + degrees);
        };
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Coordinate swapXY() {
        return new Coordinate(this.y, this.x);
    }

    public <T> boolean isValid(T[][] grid) {
        return GridHelper.isValid(grid, this);
    }

    public boolean isValid(int[][] grid) {
        return GridHelper.isValid(grid, this);
    }

    public boolean isValid(long[][] grid) {
        return GridHelper.isValid(grid, this);
    }

    public boolean isValid(boolean[][] grid) {
        return GridHelper.isValid(grid, this);
    }

    public boolean isValid(char[][] grid) {
        return GridHelper.isValid(grid, this);
    }

    /**
     * Finds the Manhattan distance from this coordinate to the origin at (0, 0).
     *
     * @return The Manhattan distance from this coordinate to the origin at (0, 0).
     */
    public int distanceToOrigin() {
        return this.x + this.y;
    }

    /**
     * Finds the Manhattan distance from this coordinate to (x2, y2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @return The Manhattan distance from this coordinate to (x2, y2).
     */
    public int distance(int x2, int y2) {
        return Math.abs(this.x - x2) + Math.abs(this.y - y2);
    }

    /**
     * Finds the Manhattan distance from this coordinate to the other coordinate specified.
     *
     * @param other The coordinate object to compare with.
     * @return The Manhattan distance from this coordinate to the other coordinate specified.
     */
    public int distance(Coordinate other) {
        return distance(other.x, other.y);
    }

    /**
     * Finds the relative {@link Direction Direction} needed to get from this coordinate to the other coordinate specified.
     * Returns null if the two coordinates are not within one tile in cardinal or ordinal directions away.
     *
     * @param other Another coordinate used to find the relative direction towards.
     * @return The {@link Direction Direction} needed to move from this coordinate to the other coordinate specified,
     * or null if this is not possible in one move in cardinal or ordinal directions.
     */
    public Direction relative(Coordinate other) {
        for (Direction dir : Direction.cardinalOrdinalDirections()) {
            if (this.resolve(dir).equals(other))
                return dir;
        }

        return null;
    }

    public Coordinate resolve(Direction direction) {
        return resolve(direction.x, direction.y);
    }

    public Coordinate resolve(Direction direction, int count) {
        return resolve(direction.x * count, direction.y * count);
    }

    /**
     * Returns a coordinate of the form (x, y + 1)
     */
    public Coordinate down() {
        return resolve(Direction.SOUTH);
    }

    /**
     * Returns a coordinate of the form (x, y - 1)
     */
    public Coordinate up() {
        return resolve(Direction.NORTH);
    }

    /**
     * Returns a coordinate of the form (x - 1, y)
     */
    public Coordinate left() {
        return resolve(Direction.WEST);
    }

    /**
     * Returns a coordinate of the form (x + 1, y)
     */
    public Coordinate right() {
        return resolve(Direction.EAST);
    }

    /**
     * Returns the value on the direction axis.
     */
    public int getAxis(Direction.Axis axis) {
        return axis == Direction.Axis.X ? this.x : this.y;
    }

    public int component1() {
        return this.x;
    }

    public int component2() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }
}

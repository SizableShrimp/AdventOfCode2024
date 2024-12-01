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

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The eight cardinal and ordinal directions, associated with degrees and relative x, y positions
 * where NORTH is at 0 degrees and a relative x,y of (0,-1).
 */
public enum Direction {
    NORTH(0, 0, -1, Axis.Y), NORTHEAST(45, 1, -1, null),
    EAST(90, 1, 0, Axis.X), SOUTHEAST(135, 1, 1, null),
    SOUTH(180, 0, 1, Axis.Y), SOUTHWEST(225, -1, 1, null),
    WEST(270, -1, 0, Axis.X), NORTHWEST(315, -1, -1, null);

    private static final Map<Integer, Direction> degreesMap = new HashMap<>();
    private static final Direction[] CARDINAL = {NORTH, EAST, SOUTH, WEST};
    private static final Direction[] ORDINAL = {NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST};
    private static final Direction[] CARDINAL_ORDINAL = {NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST};

    static {
        for (Direction dir : values()) {
            degreesMap.put(dir.degrees, dir);
        }
    }

    public final int degrees;
    public final int x;
    public final int y;
    @Nullable
    public final Axis axis;

    Direction(int degrees, int x, int y, @Nullable Axis axis) {
        this.degrees = degrees;
        this.x = x;
        this.y = y;
        this.axis = axis;
    }

    public static Map<Character, Direction> getCardinalDirections(char up, char right, char down, char left) {
        return Map.of(up, NORTH, right, EAST, down, SOUTH, left, WEST);
    }

    public static Direction parseDirection(int xDiff, int yDiff) {
        for (Direction dir : Direction.values()) {
            if (dir.x == xDiff && dir.y == yDiff)
                return dir;
        }
        throw new IllegalArgumentException();
    }

    public static Direction getCardinalDirection(char c) {
        return switch (c) {
            case 'N', 'U', '^' -> NORTH;
            case 'E', 'R', '>' -> EAST;
            case 'S', 'D', 'v' -> SOUTH;
            case 'W', 'L', '<' -> WEST;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    public static Direction[] cardinalDirections() {
        return CARDINAL;
    }

    /**
     * @return {@link Direction#NORTHEAST}, {@link Direction#SOUTHEAST}, {@link Direction#SOUTHWEST},{@link Direction#NORTHWEST}
     */
    public static Direction[] ordinalDirections() {
        return ORDINAL;
    }

    public static Direction[] cardinalOrdinalDirections() {
        return CARDINAL_ORDINAL;
    }

    public char getCharURDL() {
        return switch (this) {
            case NORTH -> 'U';
            case EAST -> 'R';
            case SOUTH -> 'D';
            case WEST -> 'L';
            default -> throw new IllegalStateException("Not a cardinal direction: " + this);
        };
    }

    public char getCharNESW() {
        return switch (this) {
            case NORTH -> 'N';
            case EAST -> 'E';
            case SOUTH -> 'S';
            case WEST -> 'W';
            default -> throw new IllegalStateException("Not a cardinal direction: " + this);
        };
    }

    public char getCharArrow() {
        return switch (this) {
            case NORTH -> '^';
            case EAST -> '>';
            case SOUTH -> 'v';
            case WEST -> '<';
            default -> throw new IllegalStateException("Not a cardinal direction: " + this);
        };
    }

    public Direction relativeDegrees(int degrees) {
        return fromDegrees(this.degrees + degrees);
    }

    public Direction opposite() {
        return relativeDegrees(180);
    }

    public Direction clockwise() {
        return relativeDegrees(90);
    }

    public Direction counterClockwise() {
        return relativeDegrees(-90);
    }

    public static Direction fromDegrees(int degrees) {
        if (degrees < 0)
            degrees = 360 + degrees;
        degrees %= 360;

        Direction dir = degreesMap.get(degrees);
        if (dir == null)
            throw new IllegalArgumentException("Invalid degrees: " + degrees);
        return dir;
    }

    public Coordinate asCoords() {
        return Coordinate.of(this.x, this.y);
    }

    public int getAxis(Axis axis) {
        return axis == Axis.X ? this.x : this.y;
    }

    public enum Axis {
        X(new Direction[]{Direction.EAST, Direction.WEST}), Y(new Direction[]{Direction.NORTH, Direction.SOUTH});

        private final Direction[] directions;

        Axis(Direction[] directions) {
            this.directions = directions;
        }

        public Axis opposite() {
            return this == X ? Y : X;
        }

        public Direction[] getDirections() {
            return this.directions;
        }
    }
}

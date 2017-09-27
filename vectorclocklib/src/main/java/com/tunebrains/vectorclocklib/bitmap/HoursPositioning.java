package com.tunebrains.vectorclocklib.bitmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 9/27/17.
 */

public class HoursPositioning {
    public static class Position {
        int x;
        int number;

        public Position(int x, int number) {

            this.x = x;
            this.number = number;
        }
    }

    public static final HashMap<Integer, List<Position>> positions = new HashMap<>();

    static {
        //0-9
        positions.put(0, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 0) }));
        positions.put(1, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 1) }));
        positions.put(2, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 2) }));
        positions.put(3, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 3) }));
        positions.put(4, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 4) }));
        positions.put(5, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 5) }));
        positions.put(6, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 6) }));
        positions.put(7, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 7) }));
        positions.put(8, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 8) }));
        positions.put(9, Arrays.asList(new Position[] { new Position(-1, -1), new Position(95, 9) }));

        //10 - 19
        positions.put(10, Arrays.asList(new Position[] { new Position(61, 1), new Position(114, 0) }));
        positions.put(11, Arrays.asList(new Position[] { new Position(61, 1), new Position(114, 1) }));
        positions.put(12, Arrays.asList(new Position[] { new Position(54, 1), new Position(110, 2) }));
        positions.put(13, Arrays.asList(new Position[] { new Position(54, 1), new Position(106, 3) }));
        positions.put(14, Arrays.asList(new Position[] { new Position(54, 1), new Position(110, 4) }));
        positions.put(15, Arrays.asList(new Position[] { new Position(54, 1), new Position(107, 5) }));
        positions.put(16, Arrays.asList(new Position[] { new Position(54, 1), new Position(108, 6) }));
        positions.put(17, Arrays.asList(new Position[] { new Position(54, 1), new Position(106, 7) }));
        positions.put(18, Arrays.asList(new Position[] { new Position(54, 1), new Position(106, 8) }));
        positions.put(19, Arrays.asList(new Position[] { new Position(54, 1), new Position(106, 9) }));

        //20 - 00
        positions.put(20, Arrays.asList(new Position[] { new Position(64, 2), new Position(120, 0) }));
        positions.put(21, Arrays.asList(new Position[] { new Position(58, 2), new Position(111, 1) }));
        positions.put(22, Arrays.asList(new Position[] { new Position(58, 2), new Position(119, 2) }));
        positions.put(23, Arrays.asList(new Position[] { new Position(58, 2), new Position(115, 3) }));
        positions.put(24, Arrays.asList(new Position[] { new Position(58, 0), new Position(121, 0) }));
    }
}

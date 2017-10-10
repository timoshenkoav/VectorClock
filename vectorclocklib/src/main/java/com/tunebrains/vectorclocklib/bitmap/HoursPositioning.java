package com.tunebrains.vectorclocklib.bitmap;

import android.util.Pair;
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

    public static final HashMap<Pair<Integer, Integer>, Float> numbersSpaceMultiplier = new HashMap<>();
    public static final HashMap<Pair<Integer, Integer>, Float> minutesNumbersSpaceMultiplier = new HashMap<>();

    public static final HashMap<Integer, List<Position>> hoursPositions = new HashMap<>();
    public static final HashMap<Integer, List<Position>> minutesPositions = new HashMap<>();
    public static final HashMap<Integer, Position> minutesOffset = new HashMap<>();
    public static final HashMap<Integer, Pair<Float, Float>> clockPads = new HashMap<>();
    private static final int GROUP_16 = 16;
    private static final int GROUP_15 = 15;
    private static final int GROUP_17 = 17;
    private static final int GROUP_18 = 18;

    static {
        //0-9 //54
        hoursPositions.put(0, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 0) }));
        hoursPositions.put(1, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 1) }));
        hoursPositions.put(2, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 2) }));
        hoursPositions.put(3, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 3) }));
        hoursPositions.put(4, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 4) }));
        hoursPositions.put(5, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 5) }));
        hoursPositions.put(6, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 6) }));
        hoursPositions.put(7, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 7) }));
        hoursPositions.put(8, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 8) }));
        hoursPositions.put(9, Arrays.asList(new Position[] { new Position(-1, -1), new Position(41, 9) }));

        //10 - 19
        hoursPositions.put(10, Arrays.asList(new Position[] { new Position(7, 1), new Position(60, 0) }));
        hoursPositions.put(11, Arrays.asList(new Position[] { new Position(7, 1), new Position(60, 1) }));
        hoursPositions.put(12, Arrays.asList(new Position[] { new Position(0, 1), new Position(56, 2) }));
        hoursPositions.put(13, Arrays.asList(new Position[] { new Position(0, 1), new Position(52, 3) }));
        hoursPositions.put(14, Arrays.asList(new Position[] { new Position(0, 1), new Position(56, 4) }));
        hoursPositions.put(15, Arrays.asList(new Position[] { new Position(0, 1), new Position(53, 5) }));
        hoursPositions.put(16, Arrays.asList(new Position[] { new Position(0, 1), new Position(54, 6) }));
        hoursPositions.put(17, Arrays.asList(new Position[] { new Position(0, 1), new Position(52, 7) }));
        hoursPositions.put(18, Arrays.asList(new Position[] { new Position(0, 1), new Position(52, 8) }));
        hoursPositions.put(19, Arrays.asList(new Position[] { new Position(0, 1), new Position(52, 9) }));

        //20 - 00
        hoursPositions.put(20, Arrays.asList(new Position[] { new Position(10, 2), new Position(66, 0) }));
        hoursPositions.put(21, Arrays.asList(new Position[] { new Position(4, 2), new Position(57, 1) }));
        hoursPositions.put(22, Arrays.asList(new Position[] { new Position(4, 2), new Position(65, 2) }));
        hoursPositions.put(23, Arrays.asList(new Position[] { new Position(4, 2), new Position(61, 3) }));
        hoursPositions.put(24, Arrays.asList(new Position[] { new Position(4, 0), new Position(67, 0) }));

        //minutes to hours

        minutesOffset.put(0, new Position(96, 0));
        minutesOffset.put(1, new Position(85, 0));
        minutesOffset.put(2, new Position(96, 0));
        minutesOffset.put(3, new Position(96, 0));
        minutesOffset.put(4, new Position(96, 0));
        minutesOffset.put(5, new Position(96, 0));
        minutesOffset.put(6, new Position(96, 0));
        minutesOffset.put(7, new Position(78, 0));
        minutesOffset.put(8, new Position(96, 0));
        minutesOffset.put(9, new Position(96, 0));
        minutesOffset.put(10, new Position(150, 0));
        minutesOffset.put(11, new Position(144, 0));
        minutesOffset.put(12, new Position(155, 0));
        minutesOffset.put(13, new Position(150, 0));
        minutesOffset.put(14, new Position(152, 0));
        minutesOffset.put(15, new Position(150, 0));
        minutesOffset.put(16, new Position(150, 0));
        minutesOffset.put(17, new Position(130, 0));
        minutesOffset.put(18, new Position(150, 0));
        minutesOffset.put(19, new Position(150, 0));

        minutesOffset.put(20, new Position(152, 0));
        minutesOffset.put(21, new Position(138, 0));
        minutesOffset.put(22, new Position(157, 0));
        minutesOffset.put(23, new Position(152, 0));
        minutesOffset.put(24, new Position(160, 0));

        addMinutesGroup(0, 0, GROUP_16);
        addMinutesGroup(1, 0, GROUP_15);
        addMinutesGroup(2, 0, GROUP_16);
        addMinutesGroup(3, 0, GROUP_16);
        addMinutesGroup(4, 0, GROUP_17);
        addMinutesGroup(5, 0, GROUP_16);
        addMinutesGroup(6, 0, GROUP_16);
        addMinutesGroup(7, 0, GROUP_16);
        addMinutesGroup(8, 0, GROUP_16);
        addMinutesGroup(9, 0, GROUP_16);
        addMinutesGroup(10, 0, GROUP_15);
        addMinutesGroup(11, 0, GROUP_15);
        addMinutesGroup(12, 0, GROUP_16);
        addMinutesGroup(13, 0, GROUP_15);
        addMinutesGroup(14, 0, GROUP_17);
        addMinutesGroup(15, 0, GROUP_15);
        addMinutesGroup(16, 0, GROUP_16);
        addMinutesGroup(17, 0, GROUP_16);
        addMinutesGroup(18, 0, GROUP_16);
        addMinutesGroup(19, 0, GROUP_16);
        addMinutesGroup(20, 0, GROUP_15);
        addMinutesGroup(21, 0, GROUP_15);
        addMinutesGroup(22, 0, GROUP_16);
        addMinutesGroup(23, 0, GROUP_15);
        addMinutesGroup(24, 0, GROUP_17);
        addMinutesGroup(25, 0, GROUP_15);
        addMinutesGroup(26, 0, GROUP_15);
        addMinutesGroup(27, 0, GROUP_16);
        addMinutesGroup(28, 0, GROUP_16);
        addMinutesGroup(29, 0, GROUP_16);
        addMinutesGroup(30, 0, GROUP_16);
        addMinutesGroup(31, 0, GROUP_15);
        addMinutesGroup(32, 0, GROUP_17);
        addMinutesGroup(33, 0, GROUP_16);
        addMinutesGroup(34, 0, GROUP_18);
        addMinutesGroup(35, 0, GROUP_16);
        addMinutesGroup(36, 0, GROUP_17);
        addMinutesGroup(37, 0, GROUP_16);
        addMinutesGroup(38, 0, GROUP_16);
        addMinutesGroup(39, 0, GROUP_16);
        addMinutesGroup(40, 0, GROUP_15);
        addMinutesGroup(41, 0, GROUP_15);
        addMinutesGroup(42, 0, GROUP_17);
        addMinutesGroup(43, 0, GROUP_16);
        addMinutesGroup(44, 0, GROUP_17);
        addMinutesGroup(45, 0, GROUP_16);
        addMinutesGroup(46, 0, GROUP_16);
        addMinutesGroup(47, 0, GROUP_16);
        addMinutesGroup(48, 0, GROUP_16);
        addMinutesGroup(49, 0, GROUP_16);
        addMinutesGroup(50, 0, GROUP_16);
        addMinutesGroup(51, 0, GROUP_15);
        addMinutesGroup(52, 0, GROUP_17);
        addMinutesGroup(53, 0, GROUP_16);
        addMinutesGroup(54, 0, GROUP_17);
        addMinutesGroup(55, 0, GROUP_16);
        addMinutesGroup(56, 0, GROUP_16);
        addMinutesGroup(57, 0, GROUP_16);
        addMinutesGroup(58, 0, GROUP_16);
        addMinutesGroup(59, 0, GROUP_16);

        clockPads.put(0, Pair.create(44.5f, 9.0f));
        clockPads.put(1, Pair.create(51.25f, 12.5f));
        clockPads.put(2, Pair.create(43.75f, 10.5f));
        clockPads.put(3, Pair.create(44.5f, 9.25f));
        clockPads.put(4, Pair.create(39.5f, 9.0f));
        clockPads.put(5, Pair.create(46f, 9.0f));
        clockPads.put(6, Pair.create(43.5f, 9.0f));
        clockPads.put(7, Pair.create(44f, 9.0f));
        clockPads.put(8, Pair.create(44.5f, 9.0f));
        clockPads.put(9, Pair.create(43.75f, 9.0f));

        numbersSpaceMultiplier.put(Pair.create(1, 0), 3f);
    }

    public static float getNumberSpaceMultiplier(int first, int second) {
        Float v = numbersSpaceMultiplier.get(Pair.create(first, second));
        if (v == null) { return 1; }
        return v.floatValue();
    }

    public static float getMinutesNumberSpaceMultiplier(int first, int second) {
        Float v = minutesNumbersSpaceMultiplier.get(Pair.create(first, second));
        if (v == null) { return 1; }
        return v.floatValue();
    }

    private static void addMinutesGroup(int i, int i1, int group16) {
        minutesPositions.put(i, Arrays.asList(new Position[] { new Position(i1, i / 10), new Position(group16, i % 10) }));
    }
}

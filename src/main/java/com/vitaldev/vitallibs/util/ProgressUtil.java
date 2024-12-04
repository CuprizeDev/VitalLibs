package com.vitaldev.vitallibs.util;

import com.google.common.base.Strings;

public class ProgressUtil {
    public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor) {
        float completedPercent = (float) current / max;
        int progressBars = (int) (totalBars * completedPercent);
        String uncompletedColor = ChatUtil.color("&7");

        return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" +
                uncompletedColor + symbol, totalBars - progressBars);

    }
}

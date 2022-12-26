package com.github.kohthecodemaster.utils;

import stdlib.enums.StringOptions;
import stdlib.utils.KOHStringUtil;
import stdlib.utils.MyTimer;

public class PrintHelper {

    public static final String DIVIDER_HORIZONTAL = "\n----------------------------------------------------------------------------------------------------\n";

    public static void pressAnyKeyToContinue() {
        KOHStringUtil.userInputString("Press Any Key To Continue.", StringOptions.DEFAULT, new MyTimer());
    }

}

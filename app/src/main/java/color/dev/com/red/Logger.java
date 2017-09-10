/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */


package color.dev.com.red;

import android.util.Log;

public class Logger {

    public static final int INFO = 0, ERROR = 1;
    private static boolean verboseInternal = false;

    public Logger(boolean verbose) {

        verboseInternal = verbose;

    }

    public static void log(int type, String text) {

        if (type > INFO || verboseInternal) Log.v("Faicheck", text);

    }

}

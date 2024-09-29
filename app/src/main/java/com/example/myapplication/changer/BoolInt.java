package com.example.myapplication.changer;

public interface BoolInt {

    static int boolToInt(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    static boolean intToBool(int i) {
        if (i == 0) {
            return false;
        } else return i == 1;
    }
}

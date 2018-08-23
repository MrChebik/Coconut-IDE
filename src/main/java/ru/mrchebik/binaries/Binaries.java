package ru.mrchebik.binaries;


public abstract class Binaries implements BinariesWrapper {
    public static String compile;
    public static String run;

    public String setBinary(String bin) {
        throw new UnsupportedOperationException();
    }

    public String getBinary(BinariesType type) {
        throw new UnsupportedOperationException();
    }
}

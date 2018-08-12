package ru.mrchebik.arguments;

import java.util.Arrays;

public class Arguments {
    public static void check(String[] args) {
        Arrays.stream(args).forEach(ArgumentsType::find);
    }
}

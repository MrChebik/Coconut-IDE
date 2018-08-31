package ru.mrchebik.arguments;

import ru.mrchebik.arguments.type.ArgumentsType;

import java.util.Arrays;

public class Arguments {
    public static void check(String[] args) {
        Arrays.stream(args).forEach(ArgumentsType::search);
    }
}

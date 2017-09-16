package ru.mrchebik.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/16/17.
 */
public class CommandPath {
    private @Getter @Setter String command;
    private @Getter @Setter Path path;
}

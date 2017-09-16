package ru.mrchebik.model;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mrchebik on 8/29/17.
 */
public class Projects {
    private @Getter Path path = Paths.get(System.getProperty("user.home"), "CoconutProjects");
}

package ru.mrchebik.controller.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by mrchebik on 8/31/17.
 */
public class CustomFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".java");
    }
}

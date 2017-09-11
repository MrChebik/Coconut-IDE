package ru.mrchebik.model.controller.actions.autosave;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
@AllArgsConstructor
public class ExistFileToSave {
    private @Getter
    Path path;
    private @Getter
    String lines;
}

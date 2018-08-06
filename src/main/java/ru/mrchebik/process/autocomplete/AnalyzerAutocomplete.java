package ru.mrchebik.process.autocomplete;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.Getter;
import ru.mrchebik.model.autocomplete.AutocompleteClass;
import ru.mrchebik.model.autocomplete.AutocompleteDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalyzerAutocomplete {
    @Getter
    private AutocompleteDatabase database;
    private List<File> javaFiles = new ArrayList<>();

    public AnalyzerAutocomplete() {
        database = new AutocompleteDatabase();
    }

    public void initialize(Path source) {
        listJavaFilesForFolder(source.toFile());

        javaFiles.forEach(file -> {
            try {
                callAnalysis(new String(Files.readAllBytes(Paths.get(file.toURI()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void listJavaFilesForFolder(File entry) {
        for (final File fileEntry : Objects.requireNonNull(entry.listFiles())) {
            if (fileEntry.isDirectory()) {
                listJavaFilesForFolder(fileEntry);
            } else if (fileEntry.getName().endsWith(".java")) {
                javaFiles.add(fileEntry);
            }
        }
    }

    public void callAnalysis(String text) {
        AutocompleteClass autocompleteClass = new AutocompleteClass();

        try {
            CompilationUnit unit = JavaParser.parse(text);
            TypeDeclaration declaration = unit.getType(0);

            if (unit.getPackageDeclaration().isPresent()) {
                autocompleteClass.setPackageClass(unit.getPackageDeclaration().get().getNameAsString());
            } else {
                autocompleteClass.setPackageClass("");
            }

            autocompleteClass.setNameClass(declaration.getNameAsString());

            if (declaration.getFields().size() > 0) {
                declaration.getFields().forEach(field -> {
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) field;
                    fieldDeclaration.getVariables().forEach(variable -> {
                        autocompleteClass.addVariable(variable.getNameAsString());
                    });
                });
            }
            if (declaration.getMethods().size() > 0) {
                declaration.getMethods().forEach(method -> {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) method;
                    autocompleteClass.addMethod(methodDeclaration.getNameAsString());
                });
            }

            database.addClass(autocompleteClass);
        } catch (ParseProblemException ignored) {}
    }
}

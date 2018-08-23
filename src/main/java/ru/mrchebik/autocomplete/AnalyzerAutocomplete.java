package ru.mrchebik.autocomplete;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import ru.mrchebik.autocomplete.database.AutocompleteDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalyzerAutocomplete {
    public static List<File> javaFiles;

    static {
        javaFiles = new ArrayList<>();
    }

    public static void initialize(Path source) {
        listJavaFilesForFolder(source.toFile());

        javaFiles.forEach(file -> {
            try {
                callAnalysis(new String(Files.readAllBytes(Paths.get(file.toURI()))), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        analysis(3, "java", "java.lang");
        analysis(5, "javax");
        analysis(6, "javafx");
        analysis(7, "ALL");
        AutocompleteDatabase.weaveWeb();
    }

    public static void analysis(int cluster, String... packages) {
        ScanResult scanResult = "ALL".equals(packages[0]) ?
                new ClassGraph()
                        .enableAllInfo()
                        .enableSystemPackages()
                        .disableJarScanning()
                        .disableDirScanning()
                        .removeTemporaryFilesAfterScan()
                        .blacklistPackages("com.sun",
                                "com.oracle",
                                "sun",
                                "jdk",
                                "java",
                                "java.applet",
                                "java.lang",
                                "javax",
                                "javafx")
                        .scan()
                :
                new ClassGraph()
                        .enableAllInfo()
                        .enableSystemPackages()
                        .disableJarScanning()
                        .disableDirScanning()
                        .removeTemporaryFilesAfterScan()
                        .whitelistPackages(packages)
                        .blacklistPackages("java.applet")
                        .scan();

        ClassInfoList list = scanResult.getAllClasses()
                .filter(classInfo -> classInfo.getModifiersStr().contains("public") &&
                        !classInfo.getName().contains("$"));

        list.forEach(classInfo -> {
            String flag = classInfo.isInterface() ?
                    "I"
                    :
                    classInfo.isAbstract() ?
                            "A"
                            :
                            classInfo.isStandardClass() ?
                                    "C"
                                    :
                                    "?";

            String text = classInfo.getName().substring(classInfo.getName().lastIndexOf(".") + 1);
            String packageText = classInfo.getName().substring(0, classInfo.getName().lastIndexOf("."));

            classInfo.getFieldInfo().forEach(field -> {
                if (field.isPublic())
                    AutocompleteDatabase.addItem(cluster, new AutocompleteItem("V", field.getName(), "", text, field.getTypeDescriptor().toString().substring(field.getTypeDescriptor().toString().lastIndexOf(".") + 1)), true);
            });
            classInfo.getMethodInfo().forEach(method -> {
                if (method.isPublic())
                    AutocompleteDatabase.addItem(cluster, new AutocompleteItem("M", method.getName(), "", text, method.getTypeDescriptor().toString().substring(method.getTypeDescriptor().toString().lastIndexOf(".") + 1)), true);
            });

            AutocompleteDatabase.addItem(cluster, new AutocompleteItem(flag, text, packageText, text, text), true);
        });
    }

    private static void listJavaFilesForFolder(File entry) {
        for (final File fileEntry : Objects.requireNonNull(entry.listFiles()))
            if (fileEntry.isDirectory())
                listJavaFilesForFolder(fileEntry);
            else if (fileEntry.getName().endsWith(".java"))
                javaFiles.add(fileEntry);
    }

    // First cluster
    public static void callAnalysis(String text, boolean isNew) {
        try {
            CompilationUnit unit = JavaParser.parse(text);
            if (unit.getTypes().size() > 0) {
                TypeDeclaration declaration = unit.getType(0);

                String packageClass = unit.getPackageDeclaration().isPresent() ?
                        unit.getPackageDeclaration().get().getNameAsString()
                        :
                        "";
                String classN = declaration.getNameAsString();

                if (declaration.getFields().size() > 0)
                    declaration.getFields().forEach(field -> {
                        FieldDeclaration fieldDeclaration = (FieldDeclaration) field;
                        fieldDeclaration.getVariables().forEach(variable -> AutocompleteDatabase.addItem(0, new AutocompleteItem("V", variable.getNameAsString(), "", classN, ((FieldDeclaration) field).getElementType().asString()), isNew));
                    });
                if (declaration.getMethods().size() > 0)
                    declaration.getMethods().forEach(method -> {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) method;
                        AutocompleteDatabase.addItem(0, new AutocompleteItem("M", methodDeclaration.getNameAsString(), "", classN, methodDeclaration.getType().asString()), isNew);
                    });

                AutocompleteDatabase.addItem(0, new AutocompleteItem("C", classN, packageClass, classN, classN), isNew);
            }
        } catch (ParseProblemException ignored) {
        }
    }
}

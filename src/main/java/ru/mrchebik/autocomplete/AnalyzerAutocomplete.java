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
import ru.mrchebik.autocomplete.database.AutocompleteItem;

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
            int flag = classInfo.isInterface() ?
                    2
                    :
                    classInfo.isAbstract() ?
                            0
                            :
                            classInfo.isStandardClass() ?
                                    1
                                    :
                                    5;
            String text = classInfo.getName().substring(classInfo.getName().lastIndexOf(".") + 1);
            String packageText = classInfo.getName().substring(0, classInfo.getName().lastIndexOf("."));
            int needPackage = CollectorAutocompleteText.addPackageName(packageText);
            int needReturnTypeS = CollectorAutocompleteText.addReturnTypeS(text);

            AutocompleteDatabase.addItem(cluster, new AutocompleteItem(flag, text, needPackage, needReturnTypeS), text, needReturnTypeS, true);

            classInfo.getFieldInfo().forEach(field -> {
                if (field.isPublic()) {
                    String fieldType = field.getTypeDescriptor().toString();
                    int returnTypeSField = CollectorAutocompleteText.addReturnTypeS(
                            fieldType.substring(fieldType.lastIndexOf(".") + 1)
                    );

                    AutocompleteDatabase.addItem(cluster, new AutocompleteItem(4, field.getName(), returnTypeSField), text, needReturnTypeS, true);
                }
            });
            classInfo.getMethodInfo().forEach(method -> {
                if (method.isPublic()) {
                    String firstPart = method.getTypeDescriptor().toString().split(" ")[0];
                    int lastIndexDot = firstPart.lastIndexOf(".");
                    int returnTypeSMethod = CollectorAutocompleteText.addReturnTypeS(
                            firstPart.substring(
                                    lastIndexDot == -1 ?
                                            0
                                            :
                                            lastIndexDot + 1));

                    AutocompleteDatabase.addItem(cluster, new AutocompleteItem(3, method.getName(), returnTypeSMethod), text, needReturnTypeS, true);
                }
            });
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

                int needPackage = CollectorAutocompleteText.addPackageName(packageClass);
                int needReturnTypeS = CollectorAutocompleteText.addReturnTypeS(classN);

                AutocompleteDatabase.addItem(0, new AutocompleteItem(1, classN, needPackage, needReturnTypeS), classN, needReturnTypeS, isNew);

                if (declaration.getFields().size() > 0)
                    declaration.getFields().forEach(field -> {
                        FieldDeclaration fieldDeclaration = (FieldDeclaration) field;
                        fieldDeclaration.getVariables().forEach(variable -> {
                            int returnTypeSField = CollectorAutocompleteText.addReturnTypeS(
                                    ((FieldDeclaration) field).getElementType().asString()
                            );

                            AutocompleteDatabase.addItem(0, new AutocompleteItem(4, variable.getNameAsString(), returnTypeSField), classN, needReturnTypeS, isNew);
                        });
                    });
                if (declaration.getMethods().size() > 0)
                    declaration.getMethods().forEach(method -> {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) method;
                        int returnTypeSMethod = CollectorAutocompleteText.addReturnTypeS(
                                methodDeclaration.getType().asString()
                        );

                        AutocompleteDatabase.addItem(0, new AutocompleteItem(3, methodDeclaration.getNameAsString(), returnTypeSMethod), classN, needReturnTypeS, isNew);
                    });
            }
        } catch (ParseProblemException ignored) {
        }
    }
}

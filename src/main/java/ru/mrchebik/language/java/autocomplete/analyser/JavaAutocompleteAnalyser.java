package ru.mrchebik.language.java.autocomplete.analyser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.SneakyThrows;
import ru.mrchebik.autocomplete.CollectorAutocompleteText;
import ru.mrchebik.autocomplete.analyser.AutocompleteAnalyser;
import ru.mrchebik.autocomplete.database.AutocompleteDatabase;
import ru.mrchebik.autocomplete.database.AutocompleteItem;
import ru.mrchebik.project.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaAutocompleteAnalyser extends AutocompleteAnalyser {
    public JavaAutocompleteAnalyser() {
        suffix = "java";
    }

    private static void initialize() {
        CollectorAutocompleteText.addPackageName("");
        CollectorAutocompleteText.addReturnTypeS("");

        listFilesForFolder(Project.pathSource.toFile());
    }

    @SneakyThrows(InterruptedException.class)
    private static void analysis() {
        var global = new Thread(JavaAutocompleteAnalyser::analysisUser);
        var user = new Thread(JavaAutocompleteAnalyser::analysisGraph);

        global.start();
        user.start();

        global.join();
        user.join();
    }

    private static void analysisUser() {
        files.forEach(file -> {
            try {
                callAnalysis(new String(Files.readAllBytes(Paths.get(file.toURI()))), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void analysisGraph() {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .enableSystemPackages()
                .disableJarScanning()
                .disableDirScanning()
                .whitelistPackages("java", "java.lang", "javax", "javafx")
                .blacklistPackages("java.applet", "java.awt")
                .scan()) {
            ClassInfoList list = scanResult.getAllClasses()
                    .filter(classInfo -> classInfo.getModifiersStr().contains("public") &&
                            !classInfo.getName().contains("$"));

            if (list != null)
                list.forEach(classInfo -> {
                    var flag = classInfo.isInterface() ?
                            3
                            :
                            classInfo.isAbstract() ?
                                    1
                                    :
                                    classInfo.isStandardClass() ?
                                            2
                                            :
                                            6;
                    var text = classInfo.getName().substring(classInfo.getName().lastIndexOf(".") + 1);
                    var packageText = classInfo.getName().substring(0, classInfo.getName().lastIndexOf("."));
                    var cluster = packageText.startsWith("javafx") ?
                            6
                            :
                            packageText.startsWith("javax") ?
                                    5
                                    :
                                    packageText.startsWith("java") ?
                                            3
                                            :
                                            7;
                    var needPackage = CollectorAutocompleteText.addPackageName(packageText);
                    var needReturnTypeS = CollectorAutocompleteText.addReturnTypeS(text);

                    AutocompleteDatabase.addItem(cluster, new AutocompleteItem(flag, text, "", needPackage, needReturnTypeS), text, needReturnTypeS, true);

                    classInfo.getFieldInfo().forEach(field -> {
                        if (field.isPublic()) {
                            var fieldType = field.getTypeDescriptor().toString();
                            var returnTypeSField = CollectorAutocompleteText.addReturnTypeS(
                                    parseClassName(fieldType)
                            );

                            AutocompleteDatabase.addItem(cluster, new AutocompleteItem(5, field.getName(), "", returnTypeSField), text, needReturnTypeS, true);
                        }
                    });
                    classInfo.getMethodInfo().forEach(method -> {
                        if (method.isPublic()) {
                            var firstPart = method.getTypeDescriptor().toString().split(" ")[0];
                            var lastIndexDot = firstPart.lastIndexOf(".");
                            var returnTypeSMethod = CollectorAutocompleteText.addReturnTypeS(
                                    firstPart.substring(
                                            lastIndexDot == -1 ?
                                                    0
                                                    :
                                                    lastIndexDot + 1));

                            var arguments = new StringBuilder();
                            var infos = method.getParameterInfo();

                            for (int i = 0; i < infos.length; i++) {
                                var type = infos[i].getTypeDescriptor().toString();
                                var name = infos[i].getName();

                                arguments.append(parseClassName(type))
                                        .append(" ")
                                        .append(name == null ?
                                                "_unnamed"
                                                :
                                                name)
                                        .append(i < infos.length - 1 ?
                                                ", "
                                                :
                                                "");
                            }

                            AutocompleteDatabase.addItem(cluster, new AutocompleteItem(4, method.getName(), arguments.toString(), returnTypeSMethod), text, needReturnTypeS, true);
                        }
                    });
                });
        }
    }

    private static String parseClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    // First cluster
    public static void callAnalysis(String text, boolean isNew) {
        try {
            var unit = JavaParser.parse(text);
            if (unit.getTypes().size() > 0) {
                var declaration = unit.getType(0);

                var packageClass = unit.getPackageDeclaration().isPresent() ?
                        unit.getPackageDeclaration().get().getNameAsString()
                        :
                        "";
                var classN = declaration.getNameAsString();

                var needPackage = CollectorAutocompleteText.addPackageName(packageClass);
                var needReturnTypeS = CollectorAutocompleteText.addReturnTypeS(classN);

                AutocompleteDatabase.addItem(1, new AutocompleteItem(2, classN, "", needPackage, needReturnTypeS), classN, needReturnTypeS, isNew);

                if (declaration.getFields().size() > 0)
                    declaration.getFields().forEach(field -> {
                        var fieldDeclaration = (FieldDeclaration) field;
                        fieldDeclaration.getVariables().forEach(variable -> {
                            int returnTypeSField = CollectorAutocompleteText.addReturnTypeS(
                                    field.getElementType().asString()
                            );

                            AutocompleteDatabase.addItem(1, new AutocompleteItem(5, variable.getNameAsString(), "", returnTypeSField), classN, needReturnTypeS, isNew);
                        });
                    });
                if (declaration.getMethods().size() > 0)
                    declaration.getMethods().forEach(method -> {
                        var methodDeclaration = (MethodDeclaration) method;
                        int returnTypeSMethod = CollectorAutocompleteText.addReturnTypeS(
                                methodDeclaration.getType().asString()
                        );

                        var declarationAsString = methodDeclaration.getDeclarationAsString(false, false);
                        var arguments = declarationAsString.substring(declarationAsString.indexOf("(") + 1, declarationAsString.lastIndexOf(")"));

                        AutocompleteDatabase.addItem(1, new AutocompleteItem(4, methodDeclaration.getNameAsString(), arguments, returnTypeSMethod), classN, needReturnTypeS, isNew);
                    });
            }
        } catch (ParseProblemException ignored) {
        }
    }

    @Override
    public void run() {
        initialize();
        analysis();

        AutocompleteDatabase.weaveWeb();
    }
}

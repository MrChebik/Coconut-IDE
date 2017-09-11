package ru.mrchebik.model;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import javafx.fxml.FXMLLoader;
import ru.mrchebik.model.project.Project;
import ru.mrchebik.model.project.ProjectEntity;
import ru.mrchebik.model.project.ProjectFactory;
import ru.mrchebik.model.project.ProjectFactoryImpl;

/**
 * Created by mrchebik on 9/11/17.
 */
public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(Project.class).to(ProjectEntity.class);
        bind(ProjectFactory.class).to(ProjectFactoryImpl.class);

        install(new FactoryModuleBuilder()
                .implement(Project.class, ProjectEntity.class)
                .build(ProjectFactory.class));
    }
}

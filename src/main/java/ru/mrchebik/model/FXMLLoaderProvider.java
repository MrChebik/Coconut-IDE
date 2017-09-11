package ru.mrchebik.model;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import javafx.fxml.FXMLLoader;

/**
 * Created by mrchebik on 9/11/17.
 */
public class FXMLLoaderProvider implements Provider<FXMLLoader> {
    @Inject
    Injector injector;

    @Override
    public FXMLLoader get() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(p -> injector.getInstance(p));
        return loader;
    }
}

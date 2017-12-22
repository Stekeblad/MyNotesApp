package io.github.stekeblad.mytodoapp;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.github.stekeblad.mytodoapp.dagger.DaggerDaggerComponent;

public class MyApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerDaggerComponent.builder().application(this).build();
    }
}

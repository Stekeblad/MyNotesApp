package io.github.stekeblad.mynotesapp;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.github.stekeblad.mynotesapp.dagger.DaggerDaggerComponent;

public class MyApp extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerDaggerComponent.builder().application(this).build();
    }
}

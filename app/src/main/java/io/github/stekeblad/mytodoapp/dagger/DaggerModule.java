package io.github.stekeblad.mytodoapp.dagger;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DaggerModule {

    @Binds
    abstract Context bindContext(Application application);
}

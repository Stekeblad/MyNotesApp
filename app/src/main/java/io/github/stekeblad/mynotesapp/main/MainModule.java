package io.github.stekeblad.mynotesapp.main;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

@Module
public abstract class MainModule {

    @ActivityScoped
    @Binds abstract MainContract.Presenter mainPresenter(MainPresenter presenter);
}

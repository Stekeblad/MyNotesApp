package io.github.stekeblad.mytodoapp.main;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mytodoapp.dagger.ActivityScoped;

@Module
public abstract class MainModule {

    @ActivityScoped
    @Binds abstract MainContract.Presenter mainPresenter(MainPresenter presenter);
}

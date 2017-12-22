package io.github.stekeblad.mynotesapp.addedit;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

@Module
public abstract class AddEditModule {

    @ActivityScoped
    @Binds abstract AddEditContract.Presenter addEditPresenter(AddEditPresenter presenter);
}

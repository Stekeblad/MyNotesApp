package io.github.stekeblad.mytodoapp.addedit;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mytodoapp.dagger.ActivityScoped;

@Module
public abstract class AddEditModule {

    @ActivityScoped
    @Binds abstract AddEditContract.Presenter addEditPresenter(AddEditPresenter presenter);
}

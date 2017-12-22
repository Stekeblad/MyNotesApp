package io.github.stekeblad.mytodoapp.details;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mytodoapp.dagger.ActivityScoped;

@Module
public abstract class DetailsModule {
    @ActivityScoped
    @Binds abstract DetailsContract.Presenter detailsPresenter(DetailsPresenter presenter);
}

package io.github.stekeblad.mynotesapp.details;

import dagger.Binds;
import dagger.Module;
import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

@Module
public abstract class DetailsModule {
    @ActivityScoped
    @Binds abstract DetailsContract.Presenter detailsPresenter(DetailsPresenter presenter);
}

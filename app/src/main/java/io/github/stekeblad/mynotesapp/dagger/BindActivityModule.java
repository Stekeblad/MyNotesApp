package io.github.stekeblad.mynotesapp.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.stekeblad.mynotesapp.addedit.AddEditActivity;
import io.github.stekeblad.mynotesapp.addedit.AddEditModule;
import io.github.stekeblad.mynotesapp.details.DetailsActivity;
import io.github.stekeblad.mynotesapp.details.DetailsModule;
import io.github.stekeblad.mynotesapp.main.MainActivity;
import io.github.stekeblad.mynotesapp.main.MainModule;

@Module
public abstract class BindActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditModule.class)
    abstract AddEditActivity addEditActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = DetailsModule.class)
    abstract DetailsActivity detailsActivity();

}

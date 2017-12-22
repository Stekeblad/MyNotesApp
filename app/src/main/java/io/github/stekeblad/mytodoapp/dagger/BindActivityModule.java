package io.github.stekeblad.mytodoapp.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.github.stekeblad.mytodoapp.addedit.AddEditActivity;
import io.github.stekeblad.mytodoapp.addedit.AddEditModule;
import io.github.stekeblad.mytodoapp.details.DetailsActivity;
import io.github.stekeblad.mytodoapp.details.DetailsModule;
import io.github.stekeblad.mytodoapp.main.MainActivity;
import io.github.stekeblad.mytodoapp.main.MainModule;

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

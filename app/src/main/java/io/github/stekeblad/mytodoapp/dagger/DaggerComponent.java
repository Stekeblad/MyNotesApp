package io.github.stekeblad.mytodoapp.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import io.github.stekeblad.mytodoapp.MyApp;

@Singleton
@Component(modules =  {DaggerModule.class,
        BindActivityModule.class,
        AndroidSupportInjectionModule.class})
public interface DaggerComponent extends AndroidInjector<MyApp>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        DaggerComponent.Builder application(Application application);
        DaggerComponent build();
    }
}

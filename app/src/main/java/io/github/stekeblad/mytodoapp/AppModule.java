package io.github.stekeblad.mytodoapp;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Purpose: Provide context to the database
 */
@Module
public class AppModule extends Activity{
    private Context mContext;

    @Provides
    public Context providesContext() {
        return getApplicationContext();
    }
}

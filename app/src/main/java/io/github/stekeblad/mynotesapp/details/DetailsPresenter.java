package io.github.stekeblad.mynotesapp.details;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;

// not needed but not forgotten
@ActivityScoped
public class DetailsPresenter implements DetailsContract.Presenter {

    @Nullable
    private DetailsContract.View mDetailsView;

    @Inject
    DetailsPresenter() {
    }

    public void takeView(DetailsContract.View view) {
        mDetailsView = view;
    }

    @Override
    public void dropView() {
        mDetailsView = null;
    }
}

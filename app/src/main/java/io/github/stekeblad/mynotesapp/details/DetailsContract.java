package io.github.stekeblad.mynotesapp.details;

import io.github.stekeblad.mynotesapp.BasePresenter;
import io.github.stekeblad.mynotesapp.BaseView;

public interface DetailsContract {
    interface View extends BaseView<DetailsContract.Presenter> {

    }

    interface Presenter extends BasePresenter<DetailsContract.View> {
    }
}

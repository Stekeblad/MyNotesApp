package io.github.stekeblad.mytodoapp.details;

import io.github.stekeblad.mytodoapp.BasePresenter;
import io.github.stekeblad.mytodoapp.BaseView;

public interface DetailsContract {
    interface View extends BaseView<DetailsContract.Presenter> {

    }

    interface Presenter extends BasePresenter<DetailsContract.View> {
    }
}

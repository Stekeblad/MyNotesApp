package io.github.stekeblad.mynotesapp.addedit;


import io.github.stekeblad.mynotesapp.BasePresenter;
import io.github.stekeblad.mynotesapp.BaseView;

/**
 * The presenter is currently not in use, so there is on communication between the
 * presenter and view to specify here
 */
public interface AddEditContract {

    interface View extends BaseView<Presenter> {
        void showProgressbar();
        void hideProgressbar();
        void finishActivity();
        void finishActivityWithError(String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void addToDB(String header, String desc);
        void updateDB(Long id , String header, String desc);
    }
}

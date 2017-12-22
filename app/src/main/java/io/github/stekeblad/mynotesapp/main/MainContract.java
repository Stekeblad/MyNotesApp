package io.github.stekeblad.mynotesapp.main;

import java.util.List;

import io.github.stekeblad.mynotesapp.BasePresenter;
import io.github.stekeblad.mynotesapp.BaseView;
import io.github.stekeblad.mynotesapp.model.NoteItem;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void setNoteList(List<String> list);
        void removeFromNoteList(int index);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(MainContract.View view);
        void dropView();
        void deleteAll();
        void deleteNoteItem(int id);
        void loadNoteItems();
        NoteItem getNote(int index);
        void freeMemory();
    }
}

package io.github.stekeblad.mytodoapp.main;

import java.util.List;

import io.github.stekeblad.mytodoapp.BasePresenter;
import io.github.stekeblad.mytodoapp.BaseView;
import io.github.stekeblad.mytodoapp.model.TodoItem;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void setTodoList(List<String> list);
        void removeFromTodoList(int index);
    }

    interface Presenter extends BasePresenter<View> {

        void takeView(MainContract.View view);
        void dropView();
        void deleteAll();
        void deleteTodoItem(int id);
        void loadTodoItems();
        TodoItem getTodo(int index);
        void freeMemory();
    }
}

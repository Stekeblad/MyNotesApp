package io.github.stekeblad.mytodoapp.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.github.stekeblad.mytodoapp.dagger.ActivityScoped;
import io.github.stekeblad.mytodoapp.model.Database;
import io.github.stekeblad.mytodoapp.model.TodoItem;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class MainPresenter implements MainContract.Presenter{

    @Nullable
    private MainContract.View mMainView;

    private Database mDatabase;
    private List<TodoItem> todoItems;

    @Inject
    MainPresenter(Database database){
        mDatabase = database;
    }

    @Override
    public void takeView(MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void dropView() {
        mMainView = null;
    }

    @Override
    public void deleteAll() {

        DisposableObserver<Boolean> disposableObserver =
                Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        mDatabase.clearDatabase();
                        return true;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        // nothing here
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        if (mMainView != null) {
                            mMainView.setTodoList(new ArrayList<String>());
                        }
                    }
                });
    }

    @Override
    public void deleteTodoItem(final int index) {

        DisposableObserver<Boolean> disposableObserver =
                Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        mDatabase.deleteRow(index);
                        return true;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        // Only one thing
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        if (mMainView != null) {
                            mMainView.removeFromTodoList(index);
                        }
                    }
                });
    }

    @Override
    public void loadTodoItems() {
        DisposableObserver<Boolean> disposableObserver =
                Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        todoItems = mDatabase.getAll();
                        return true;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        // Only one thing
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        if(mMainView != null) {
                            List<String> viewList = new ArrayList<>();
                            for(TodoItem item : todoItems) {
                                viewList.add(item.getHeader());
                            }
                            mMainView.setTodoList(viewList);
                        }
                    }
                });
    }

    @Override
    public TodoItem getTodo(int index) {
            return mDatabase.getByIndex(index);
    }

    @Override
    public void freeMemory() {
        mDatabase.clearCaches();
        if(mMainView != null) {
            mMainView.setTodoList(new ArrayList<String>());
        }
    }
}

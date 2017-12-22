package io.github.stekeblad.mynotesapp.addedit;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.github.stekeblad.mynotesapp.dagger.ActivityScoped;
import io.github.stekeblad.mynotesapp.model.Database;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class AddEditPresenter implements AddEditContract.Presenter {

    @Nullable
    private AddEditContract.View mAddEditView;

    private Database mDatabase;

    @Inject
    AddEditPresenter(Database database){
        mDatabase = database;
    }

    public void takeView(AddEditContract.View view) {
        mAddEditView = view;
    }

    @Override
    public void dropView() {
        mAddEditView = null;
    }

    @Override
    public void addToDB(final String header, final String desc) {
        if(mAddEditView != null) {
            mAddEditView.showProgressbar();
        }
        DisposableObserver<Long> disposableObserver =
                Observable.fromCallable(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return mDatabase.insert(header, desc);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Long>() {
                            @Override
                            public void onNext(Long id) {
                                // not needed?
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (mAddEditView != null) {
                                    mAddEditView.hideProgressbar();
                                    mAddEditView.finishActivityWithError("Failed to add note to database");
                                }
                            }
                            @Override
                            public void onComplete() {
                                if (mAddEditView != null) {
                                    mAddEditView.hideProgressbar();
                                    mAddEditView.finishActivity();
                                }
                            }
                        });
    }

    @Override
    public void updateDB(final Long id, final String header, final String desc) {
        if (mAddEditView != null) {
            mAddEditView.showProgressbar();
        }
        DisposableObserver<Boolean> disposableObserver =
                Observable.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (id < 0) {
                            throw new Exception("Id can not be negative");
                        }
                        mDatabase.updateRow(id, header, desc);
                        return true;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                // nothing
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (mAddEditView != null) {
                                    mAddEditView.hideProgressbar();
                                    mAddEditView.finishActivityWithError("Failed to update database");
                            }
                            }
                            @Override
                            public void onComplete() {
                                if (mAddEditView != null) {
                                    mAddEditView.hideProgressbar();
                                    mAddEditView.finishActivity();
                                }
                            }
                        });
    }
}

package io.github.stekeblad.mynotesapp;

public interface BasePresenter<T> {
    // Initialize presenter and flag view as active
    void takeView(T view);

    // Flag view as not active
    void dropView();
}

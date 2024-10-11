package com.example.midistouchmalfunction54.net;

import com.example.midistouchmalfunction54.net.beans.UserBean;
import com.example.midistouchmalfunction54.net.tasks.LoginTask;
import com.google.firebase.database.DatabaseReference;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoginService extends Service<UserBean> {
    private LoginTask loginTask;

    public LoginService(LoginTask loginTask) {
        this.loginTask = loginTask;
    }

    @Override
    protected void scheduled() {
        super.scheduled();
    }

    @Override
    protected void running() {
        super.running();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
    }

    @Override
    protected void failed() {
        super.failed();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected Task<UserBean> createTask() {
       return loginTask;

    }

    @Override
    protected void executeTask(Task<UserBean> task) {
        super.executeTask(task);

    }

    @Override
    protected void ready() {
        super.ready();
    }

    protected LoginService() {
        super();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
    }
}

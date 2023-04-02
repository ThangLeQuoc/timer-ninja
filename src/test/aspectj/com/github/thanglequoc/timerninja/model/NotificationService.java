package com.github.thanglequoc.timerninja.model;

public class NotificationService {

    public void notify(User user) {
        notifyViaSMS(user);
        notifyViaEmail(user);
    }

    private void notifyViaSMS(User user) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void notifyViaEmail(User user) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

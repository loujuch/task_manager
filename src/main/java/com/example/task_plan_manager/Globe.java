package com.example.task_plan_manager;

import javafx.stage.Stage;

public class Globe {
    private static String path;
    private static User user=null;
    private static Stage stage=null;
    private static SELECT now=SELECT.TASK;

    public enum SELECT{
        TASK,FINISHED,OLD,PLAN,NEW_TASK,NEW_PLAN,SEARCH,ACCOUNT
    }

    public static void setUser(User use) {
        user=use;
    }

    public static User getUser() {
        return user;
    }

    public static void setStage(Stage out_stage) {
        stage=out_stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public static SELECT getNow() {
        return now;
    }

    public static void setNow(SELECT now_out) {
        now = now_out;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Globe.path = path;
    }
}

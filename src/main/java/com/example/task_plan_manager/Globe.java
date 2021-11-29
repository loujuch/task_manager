package com.example.task_plan_manager;

import com.example.task_plan_manager.Utils.EventUtils;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Globe {
    private static User user=null;
    private static Stage stage=null;
    private static SELECT now=SELECT.TASK;

    public enum SELECT{
        TASK,FINISHED,OLD,PLAN,NEW_TASK,NEW_PLAN,SEARCH,ACCOUNT;
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
}

package com.example.task_plan_manager;

import com.example.task_plan_manager.Controllers.BarShow;
import javafx.scene.layout.VBox;

public class Pass {
    private static BarShow barShow;
    private static int mainNow=0;
    private static VBox mainShow=null;

    public static void setBarShow(BarShow barShow1) {
        barShow=barShow1;
    }

    public static BarShow getBarShow() {
        return barShow;
    }

    public static void setMainNow(int now) {
        mainNow=now;
    }

    public static int getMainNow() {
        return mainNow;
    }

    public static void setMainShow(VBox vBox) {
        mainShow=vBox;
    }

    public static VBox getMainShow() {
        return mainShow;
    }
}

package com.example.task_plan_manager.other;

import com.example.task_plan_manager.Controllers.*;
import com.example.task_plan_manager.Utils.ErrorUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {
    private final String SELECT=
            "-fx-background-color: rgb(255,255,255,0.7); -fx-border-color: rgb(0,0,0,0.0); -fx-border-width: 1";
    private final String COMMON=
            "-fx-background-color: rgb(255,255,255,0); -fx-border-color: rgb(0,0,0,0.4); -fx-border-width: 1;";

    @FXML private Button task;
    @FXML private Button finished;
    @FXML private Button old;
    @FXML private Button plan;
    @FXML private Button new_task;
    @FXML private Button new_plan;
    @FXML private Button search;
    @FXML private Button account;
    @FXML private VBox window;

    @FXML
    protected void onTask() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        task.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.TASK);
        window.getChildren().clear();
        EventsShow eventsShow=new EventsShow();
        window.getChildren().add(eventsShow);
    }

    @FXML
    protected void onFinished() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        finished.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.FINISHED);
        window.getChildren().clear();
        EventsShow eventsShow=new EventsShow();
        window.getChildren().add(eventsShow);
    }

    @FXML
    protected void onOld() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        old.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.OLD);
        window.getChildren().clear();
        EventsShow eventsShow=new EventsShow();
        window.getChildren().add(eventsShow);
    }

    @FXML
    protected void onPlan() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        plan.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.PLAN);
        window.getChildren().clear();
        EventsShow eventsShow=new EventsShow();
        window.getChildren().add(eventsShow);
    }

    @FXML
    protected void onNewTask() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        new_task.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.NEW_TASK);
        window.getChildren().clear();
        window.getChildren().add(new TaskCreateShow());
    }

    @FXML
    protected void onNewPlan() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        new_plan.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.NEW_PLAN);
        window.getChildren().clear();
        window.getChildren().add(new PlanCreateShow());
    }

    @FXML
    protected void onSearch() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        search.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.SEARCH);
        window.getChildren().clear();
        window.getChildren().add(new SearchShow());
    }

    @FXML
    protected void onAccount() {
        Button tmp=getSelected();
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        tmp.setStyle(COMMON);
        account.setStyle(SELECT);
        Globe.setNow(Globe.SELECT.ACCOUNT);
        window.getChildren().clear();
        window.getChildren().add(new AccountShow());
    }

    protected Button getSelected() {
        return switch (Globe.getNow()) {
            case TASK -> task;
            case FINISHED -> finished;
            case OLD -> old;
            case PLAN -> plan;
            case NEW_TASK -> new_task;
            case NEW_PLAN -> new_plan;
            case SEARCH -> search;
            case ACCOUNT -> account;
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Globe.setNow(Globe.SELECT.TASK);
        Pass.setMainShow(window);
        onTask();
    }
}

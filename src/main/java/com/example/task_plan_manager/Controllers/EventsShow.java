package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Event;
import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.Utils.DateBaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;

public class EventsShow extends VBox {

    private ArrayList<Event>events=new ArrayList<>();
    private ArrayList<BarShow>barShows=new ArrayList<>();
    private int num;

    @FXML private VBox window;

    public EventsShow() {
        this(0);
    }

    public EventsShow(int offset) {
        this(null,offset);
    }

    public EventsShow(String s) {
        this(s,0);
    }

    public EventsShow(String s, int offset) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("events_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.setAlignment(Pos.CENTER);
        if (s==null)num=DateBaseUtils.getNum(Globe.getUser().getId(), new Date().getTime(),Globe.getNow());
        else num=DateBaseUtils.getNum(Globe.getUser().getId(),s);
        show(offset,s);
    }

    private void show(int offset, String s) {
        if (s==null)events=DateBaseUtils.getEvent(Globe.getUser().getId(),new Date().getTime(),Globe.getNow(),offset);
        else events=DateBaseUtils.getEvent(Globe.getUser().getId(),s,offset);
        for (Event i:events) {
            barShows.add(i.getBarShow(offset,s,offset!=0&&num%20==1&&offset==num/20));
        }
        for (BarShow i:barShows) {
            window.getChildren().add(i);
        }
        if (num>20) {
            ChoiceBox choiceBox=getChoiceBox();
            choiceBox.setValue(choiceBox.getItems().get(offset<<1));
            choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
                events.clear();
                barShows.clear();
                window.getChildren().clear();
                show(t1.intValue()>>>1,s);
            });
            window.getChildren().add(setMargin(20));
            window.getChildren().add(choiceBox);
            window.getChildren().add(setMargin(20));
        }
    }

    private ChoiceBox getChoiceBox() {
        int i=21;
        ChoiceBox choiceBox=new ChoiceBox();
        choiceBox.getItems().add(1+"-"+20);
        for(i=21;i+19<num;i+=20) {
            choiceBox.getItems().add(new Separator());
            choiceBox.getItems().add(i+"-"+(i+19));
        }
        choiceBox.getItems().add(new Separator());
        choiceBox.getItems().add(i+"-"+num);
        choiceBox.setMinSize(150,40);
        choiceBox.setStyle("-fx-font-size: 20");
        return choiceBox;
    }

    private Label setMargin(double n) {
        Label label=new Label();
        label.setVisible(false);
        label.setMinSize(n,n);
        return label;
    }
}

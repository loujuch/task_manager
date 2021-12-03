package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.ErrorUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class SearchShow extends VBox {

    private int important=-1;

    @FXML private TextField name;
    @FXML private DatePicker start_first;
    @FXML private DatePicker start_second;
    @FXML private DatePicker end_first;
    @FXML private DatePicker end_second;
    @FXML private ChoiceBox importance;
    @FXML private Button search;

    public SearchShow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("search_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        start_first.setValue(LocalDate.now().atStartOfDay().toLocalDate());
        start_second.setValue(LocalDate.now().plusDays(1));
        end_first.setValue(LocalDate.now().atStartOfDay().toLocalDate());
        end_second.setValue(LocalDate.now().plusDays(1));
        importance.getItems().add("");
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[0]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[1]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[2]);
        importance.setValue("");
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> important=(t1.intValue()>>>1)-1);
        search.setOnAction(e -> onSearch());
    }

    private void onSearch() {
        String s=name.getText();
        long start_first_time=start_first.getValue().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long start_second_time=start_second.getValue().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long end_first_time=end_first.getValue().plusDays(1).atStartOfDay().
                toInstant(ZoneOffset.of("+8")).toEpochMilli()-1;
        long end_second_time=end_second.getValue().plusDays(1).atStartOfDay().
                toInstant(ZoneOffset.of("+8")).toEpochMilli()-1;
        if (s.isBlank()||s.length()>255) {
            ErrorUtils.NameError();
            return;
        }
        if (start_first_time>=start_second_time||end_first_time>=end_second_time||start_first_time>=end_second_time) {
            ErrorUtils.DateError();
            return;
        }
        StringBuilder str= new StringBuilder("%");
        for(int i=0;i<s.length();++i) {
            str.append(s.charAt(i)).append("%");
        }
        String judge=String.format("where name like '%s' and start >= %d and " +
                "start <= %d and end >= %d and end <= %d and time = -1 ",str,start_first_time,
                start_second_time,end_first_time,end_second_time);
        if (important>=0)judge+="and importance = "+important;
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new EventsShow(judge));
    }
}

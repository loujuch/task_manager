package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Event;
import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.other.Pass;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Date;

public class BarShow extends VBox {

    private int id;
    private int num=1;
    private int offset;
    private String s;
    private boolean sub;
    private final static String[]STYLE={"#D3D3D3","#FFFFFF","#B0C4DE","#90EE90","#FFFF00",
            "#B0E0E6","#BA55D3","#FF00FF","#FFA500","#FFA500"};

    @FXML private Button way;
    @FXML private HBox tran;
    @FXML private Label name;
    @FXML private Label start;
    @FXML private Label end;
    @FXML private Label importance;

    public BarShow(String name, Date start, Date end, int importance, int id, int offset, String s, boolean sub) {
        if (name==null||start==null||end==null) {
            ErrorUtils.Error();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("bar_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.offset=offset;
        this.s=s;
        this.sub=sub;
        this.name.setText(name);
        this.start.setText(OtherUtils.getDateString(start));
        this.end.setText(OtherUtils.getDateString(end));
        this.importance.setText(Event.IMPORT[importance]);
        this.id=id;
        Date now=new Date();
        num+=3*importance;
        if (now.after(end))++num;
        if (now.before(start))--num;
        reSet();
        way.setOnAction(event -> onBackground());
    }

    public void start(String name, Date start, Date end, int importance, int id) {
        this.name.setText(name);
        this.start.setText(OtherUtils.getDateString(start));
        this.end.setText(OtherUtils.getDateString(end));
        this.importance.setText(Event.IMPORT[importance]);
        this.id=id;
        Date now=new Date();
        num+=3*importance;
        if (now.after(end))++num;
        if (now.before(start))--num;
        reSet();
    }

    protected void onBackground() {
        if (Pass.getBarShow()!=null&&(Pass.getBarShow()==this||Pass.getBarShow().id==this.id)) {
            if (Globe.getNow()== Globe.SELECT.PLAN){
                Pass.getMainShow().getChildren().clear();
                Pass.getMainShow().getChildren().add(new PlanDetailShow(id,offset,sub));
            } else {
                Pass.getMainShow().getChildren().clear();
                Pass.getMainShow().getChildren().add(new TaskDetailShow(id,offset,s,sub));
            }
        } else {
            if (Pass.getBarShow()!=null)Pass.getBarShow().reSet();
            Pass.setBarShow(this);
            tran.setStyle("-fx-background-color: "+STYLE[9]+";");
        }
    }

    protected void reSet() {
        tran.setStyle("-fx-background-color: "+STYLE[num]+";");
    }
}

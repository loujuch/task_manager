package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.User;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import com.example.task_plan_manager.Utils.OtherUtils;
import com.example.task_plan_manager.Utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class AccountShow extends VBox {

    private final static String TAG="com.example.task_plan_manager.Account.";

    @FXML private Label name;
    @FXML private HBox status;
    @FXML private CheckBox cut;
    @FXML private CheckBox delete_flag;
    @FXML private ChoiceBox name_sort;
    @FXML private ChoiceBox name_order;
    @FXML private ChoiceBox start_sort;
    @FXML private ChoiceBox start_order;
    @FXML private ChoiceBox end_sort;
    @FXML private ChoiceBox end_order;
    @FXML private ChoiceBox importance_sort;
    @FXML private ChoiceBox importance_order;
    @FXML private Button save;
    @FXML private Button log_out;

    public AccountShow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("account_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        save.setOnAction(e -> saveSetting());
        log_out.setOnAction(e -> logOut());
    }

    private void saveSetting() {
        boolean new_cut=cut.isSelected();
        boolean new_delete=false;
        if(Globe.getUser().getHost()!=-1)new_delete=delete_flag.isSelected();
        int[]sort={-1,-1,-1,-1};
        for (int i=0;i<4;++i) {
           if (!setSort(sort,i,(int) getSort(i).getValue()%10,"升序".equals(getOrder(i).getValue()))) {
               ErrorUtils.Error();
               return;
           }
        }
        User user=Globe.getUser();
        user.setSort(sort);
        user.setCut(new_cut);
        user.setDelete(new_delete);
        Globe.setUser(user);
        FileUtils.writeNow(user);
        ErrorUtils.OperatorFinish();
    }

    private boolean setSort(int[]sort, int i, int index, boolean order) {
        if (index>4||sort[index]!=-1) return false;
        sort[index]=i+(order?10:0);
        return true;
    }

    private void logOut() {
        if(!FileUtils.deleteNow()) {
            ErrorUtils.FileDeleteFail("now",TAG+"logOut");
            return;
        }
        try {
            UIUtils.showStart(Globe.getStage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        User user=Globe.getUser();
        name.setText(user.getName());
        status.setVisible(false);
        if (user.getHost()==-1) {
            Label label=new Label("离线帐号");
            label.setFont(Font.font(20));
            label.setMinWidth(100);
            status.getChildren().add(label);
            Button button=new Button("添加服务器");
            button.setOnAction(e -> tran());
            button.setFont(Font.font(20));
            status.getChildren().add(button);
            delete_flag.setVisible(false);
            delete_flag.setManaged(false);
        } else {
            Label label=new Label(OtherUtils.setIp(user.getIp(),user.getPort()));
            label.setFont(Font.font(20));
            delete_flag.setSelected(user.isDelete());
        }
        cut.setSelected(user.isCut());
        int[]sort= user.getSort();
        for (int i=0;i<4;++i) {
            initChoice(getSort(sort[i]%10),getOrder(sort[i]%10),i,sort[i]<10);
        }
    }

    private ChoiceBox getSort(int n) {
        return switch (n) {
            case 0 -> name_sort;
            case 1 -> start_sort;
            case 2 -> end_sort;
            case 3 -> importance_sort;
            default -> null;
        };
    }

    private ChoiceBox getOrder(int n) {
        return switch (n) {
            case 0 -> name_order;
            case 1 -> start_order;
            case 2 -> end_order;
            case 3 -> importance_order;
            default -> null;
        };
    }

    private void initChoice(ChoiceBox sort, ChoiceBox order, int space, boolean flag) {
        sort.getItems().add(0);
        sort.getItems().add(new Separator());
        sort.getItems().add(1);
        sort.getItems().add(new Separator());
        sort.getItems().add(2);
        sort.getItems().add(new Separator());
        sort.getItems().add(3);
        sort.setValue(space);
        order.getItems().add("升序");
        order.getItems().add(new Separator());
        order.getItems().add("降序");
        if (flag)order.setValue("降序");
        else order.setValue("升序");
    }

    private void tran() {

    }
}
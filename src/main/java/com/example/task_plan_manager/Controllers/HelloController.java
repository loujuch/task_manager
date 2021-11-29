package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import com.example.task_plan_manager.Utils.OtherUtils;
import com.example.task_plan_manager.Utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class HelloController {
    private boolean in_up=false;

    @FXML private Button log_in;
    @FXML private Button log_up;
    @FXML private TextField user_name;
    @FXML private TextField pass;
    @FXML private CheckBox inline;
    @FXML private TextField ip_input;
    @FXML private HBox invite;
    @FXML private TextField code_input;

    @FXML
    protected void onLogIn() {
        in_up=false;
        log_in.setStyle("-fx-background-color:rgb(255,255,255,1)");
        log_up.setStyle("-fx-background-color:rgb(255,255,255,0.5)");
        inline.setSelected(false);
        invite.setVisible(false);
    }

    @FXML
    protected void onLogUp() {
        in_up=true;
        log_up.setStyle("-fx-background-color:rgb(255,255,255,1)");
        log_in.setStyle("-fx-background-color:rgb(255,255,255,0.5)");
        inline.setSelected(false);
        invite.setVisible(false);
    }

    @FXML
    protected void onInline() {
        invite.setVisible(in_up&&inline.isSelected());
    }

    @FXML
    protected void onEntry() throws IOException {
        String user=user_name.getText();
        String password=pass.getText();
        if (user.length()<6||user.length()>15||password.length()<6||password.length()>15) {
            ErrorUtils.InputLengthError();
            return;
        }
        int[]ip={0,0,0,0};
        int port=-1;
        String ip_str;
        String code="";
        if(inline.isSelected()) {
            ip_str=ip_input.getText();
            code=code_input.getText();
            port= OtherUtils.getIp(ip,ip_str);
            if(port<0)return;
        }
        int flag;
        if(in_up) {
            flag=EventUtils.userSignUp(user,password,ip,port,code);
        } else {
            flag=EventUtils.userSignIn(user,password,ip,port);
        }
        if(flag==0) {
            flag=EventUtils.start();
            if (flag!=0) {
                return;
            }
            UIUtils.showMain(Globe.getStage());
        } else if (flag==1) {
            if (in_up)ErrorUtils.UserExist();
            else ErrorUtils.UserNoExist();
        } else {
            ErrorUtils.OperateFail();
        }
    }
}
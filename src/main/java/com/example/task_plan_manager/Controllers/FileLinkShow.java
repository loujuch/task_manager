package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.Utils.DateBaseUtils;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileLinkShow extends VBox {

    private final boolean type;
    private String name;
    private String path;
    private int index;
    private int event;
    private VBox from;
    private VBox to;

    @FXML private Hyperlink link;

    public FileLinkShow(String name, String path, boolean type, int index, int event,
                        VBox from, VBox to) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("file_link_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.from=from;
        this.to=to;
        this.name=name;
        this.path=path;
        this.type=type;
        this.index=index;
        this.event=event;
        link.setText(name);

        if (type==FileUtils.LOCAL)link.setOnAction(e -> FileUtils.openFile(new File(path)));
        else System.out.println(path);;
        ContextMenu contextMenu=new ContextMenu();
        getItems(contextMenu);
        link.addEventHandler(MouseEvent.MOUSE_CLICKED,  (MouseEvent  me) ->  {
            if (me.getButton() == MouseButton.SECONDARY  || me.isControlDown())  {
                contextMenu.show(link, me.getScreenX(), me.getScreenY());
            }  else  {
                contextMenu.hide();
            }
        });
    }

    private void getItems(ContextMenu contextMenu) {
        if (contextMenu==null) {
            ErrorUtils.Error();
            return;
        }
        MenuItem menuItem=new MenuItem();
        menuItem.setStyle("-fx-font-size:20");
        menuItem.setText("打开");
        if(type==FileUtils.LOCAL) {
            menuItem.setOnAction(e -> FileUtils.openFile(new File(path)));
        } else {
        }
        contextMenu.getItems().add(menuItem);
        menuItem=new MenuItem();
        menuItem.setStyle("-fx-font-size:20");
        if (type==FileUtils.LOCAL) {
            menuItem.setText("清理缓存");
            if(DateBaseUtils.getFileNum(Globe.getUser().getId(),event,index)<=1)menuItem.setDisable(true);
            menuItem.setOnAction(e -> FileUtils.deleteFile(path));
            move();
        } else {
        }
        contextMenu.getItems().add(menuItem);
        menuItem=new MenuItem();
        menuItem.setStyle("-fx-font-size:20");
        menuItem.setText("另存为");
        if (type==FileUtils.LOCAL) {
            menuItem.setOnAction(e -> {
                DirectoryChooser directoryChooser=new DirectoryChooser();
                directoryChooser.setTitle("设置保存目录");
                File file=directoryChooser.showDialog(new Stage());
                if (file!=null) {
                    FileUtils.copyFile(new File(path),file.getPath()+"/"+path.substring(path.lastIndexOf('/')+1));
                }
            });
        } else {
        }
        contextMenu.getItems().add(menuItem);
    }

    private void move() {
        to.getChildren().add(this);
        from.getChildren().remove(this);
        VBox tmp;
        tmp=to;
        to=from;
        from=tmp;
    }
}

package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public class FileShow extends HBox {

    private final static String TAG="com.example.task_plan_manager.FileShow.";

    private final VBox parent;
    private final File file;
    private int len;

    @FXML private TextField name;
    @FXML private Hyperlink link_file;
    @FXML private Button delete;

    public FileShow(VBox father, File file, ArrayList<FileShow>list) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("file_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        parent=father;
        this.file=file;
        len=file.getName().lastIndexOf('.');
        if(len<0) {
            len=file.getName().length();
        }
        name.setText(file.getName().substring(0,len));
        link_file.setText(file.getName());
        link_file.setOnAction(e -> FileUtils.openFile(file));
        delete.setOnAction(e -> {
            parent.getChildren().remove(this);
            list.remove(this);
        });
    }

    public String getName() {
        return name.getText();
    }

    public String getPath() {
        return name.getText()+file.getName().substring(len);
    }

    public boolean moveFile(String path, boolean flag) {
        if (path==null) {
            ErrorUtils.NullPointerInputError(TAG+"moveFile");
            return false;
        }
        String to=path+name.getText()+file.getName().substring(len);
        if (path.startsWith("./data/")||flag)return FileUtils.copyFile(file,to);
        if (Globe.getUser().isCut())return FileUtils.moveFile(file,to);
        else return FileUtils.copyFile(file,to);
    }

    public boolean moveFile(String path) {
        return moveFile(path,false);
    }
}

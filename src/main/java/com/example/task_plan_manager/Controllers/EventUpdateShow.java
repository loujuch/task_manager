package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.Utils.DateBaseUtils;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventUpdateShow extends VBox {

    protected boolean setDateBaseShow(VBox label, VBox local, VBox local_add, VBox cloud, VBox cloud_add,
                                    boolean have, boolean io, ArrayList<FileShow> mid, int event) {
        if (label==null||local==null||local_add==null||cloud==null||cloud_add==null) {
            ErrorUtils.Error();
            return false;
        }
        label.setVisible(have);
        label.setManaged(have);
        if (!have)return true;
        List<Pair<Integer, String>> tmp= DateBaseUtils.getFile(Globe.getUser().getId(),event,io,-1);
        if(tmp!=null) {
            for (Pair<Integer, String> i:tmp) {
                FileShow fileShow=new FileShow(local_add,new File(i.getValue()),mid);
                local_add.getChildren().add(fileShow);
                mid.add(fileShow);
            }
        }
        tmp= DateBaseUtils.getFile(Globe.getUser().getId(),event,io,0);
        if (tmp==null||tmp.isEmpty()) {
            cloud.setVisible(false);
            cloud.setManaged(false);
        } else {
            for (Pair<Integer, String> i:tmp) {
                FileShow fileShow=new FileShow(cloud_add,new File(i.getValue()),mid);
                cloud_add.getChildren().add(fileShow);
                mid.add(fileShow);
            }
        }
        return true;
    }

    protected boolean fileWrite(TextArea text, String path) {
        if (text==null||path==null) {
            ErrorUtils.Error();
            return false;
        }
        ArrayList<String>tmp= FileUtils.readFile(path);
        if (tmp==null||tmp.isEmpty())return false;
        text.setText(tmp.get(0));
        for(int i=1;i<tmp.size();++i) text.appendText("\n"+tmp.get(i));
        return false;
    }
}

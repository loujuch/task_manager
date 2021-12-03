package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.Controllers.FileShow;
import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class EventUtils {

    public static boolean init() {
        return DateBaseUtils.init()&&FileUtils.init();
    }

    public static int start() {
        User user=new User();
        if (user.getId()<0) {
            ErrorUtils.FileParserFail(Globe.getPath()+"/.now");
            return -1;
        }
        Globe.setUser(user);
        return 0;
    }

    public static int userSignUp(String user, String password, int[]ip, int port, String code) {
        if (user==null||password==null||ip==null||code==null) {
            ErrorUtils.Error();
            return -1;
        }
        if (port<0) {
            User use=new User(user,password,ip,2000000000);
            if(DateBaseUtils.existUser(use))return 1;
            use.setId(DateBaseUtils.createUser(use));
            if (use.getId()<=0) {
                return -1;
            }
            if (!FileUtils.createNow()) {
                DateBaseUtils.deleteUser(use.getId());
                return -1;
            }
            if(!FileUtils.writeNow(use)) {
                DateBaseUtils.deleteUser(use.getId());
                FileUtils.deleteNow();
                return -2;
            }
            if (!FileUtils.createFolder(Globe.getPath()+"/.data/"+use.getId())) {
                DateBaseUtils.deleteUser(use.getId());
                FileUtils.deleteNow();
                return -3;
            }
        } else {
            ErrorUtils.ThisFeatureIsNotOpen();
        }
        return 0;
    }

    public static int userSignIn(String user, String password, int[]ip, int port) {
        if (user==null||password==null||ip==null) {
            ErrorUtils.Error();
            return -1;
        }
        if(port<0) {
            User use=new User(user,password,ip,2000000000);
            if(!DateBaseUtils.existUser(use)) return 1;
            use=DateBaseUtils.getUser(use.getMainJudge());
            if (!FileUtils.createNow())return -1;
            if (!FileUtils.writeNow(use)) {
                FileUtils.deleteNow();
                return -2;
            }
        } else {
            ErrorUtils.ThisFeatureIsNotOpen();
        }
        return 0;
    }

    public static int createEvent(String name, LocalDate left, LocalDate right, int importance,
                                  String detail, ArrayList<FileShow>files, int offset, int time) {
        return createEvent(name,left,right,importance,detail,files,offset,time,-1, false);
    }

    public static int createEvent(String name, LocalDate left, LocalDate right, int importance,
                                  String detail, ArrayList<FileShow>files, int offset, int time,
                                  int belong, boolean flag) {
        if (name==null||left==null||right==null||detail==null||files==null) {
            ErrorUtils.Error();
            return -1;
        }
        int num=DateBaseUtils.createEvent(name,
                left.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli(),
                right.plusDays(1).atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli()-1,
                importance,!detail.isBlank(),!files.isEmpty(),offset,time,belong);
        if (num==-1) {
            ErrorUtils.OperateFail();
            return -2;
        }
        String path=Globe.getPath()+"/.data/"+Globe.getUser().getId()+"/"+num+"/";
        if(!FileUtils.eventInit(path)) {
            DateBaseUtils.deleteEventAndFile(num);
            ErrorUtils.OperateFail();
            return -3;
        }
        if (!detail.isBlank()) {
            FileUtils.writeFile(detail,path+"detail");
        }
        path+="in/";
        for (int i=0;i<files.size();++i) {
            FileShow fileShow=files.get(i);
            fileShow.moveFile(path,flag);
            DateBaseUtils.createFile(Globe.getUser().getId(),num,i,
                    fileShow.getName(),path+fileShow.getPath(),FileUtils.IN);
        }
        return num;
    }

    public static int updateEvent(int id, String name, LocalDate left, LocalDate right, int importance, String detail,
                                  String remark, ArrayList<FileShow>in,ArrayList<FileShow>out,int offset, int time) {
        boolean flag;
        if (left==null||right==null) {
            flag=DateBaseUtils.updateEvent(id,name, -1, -1,
                    importance,!detail.isBlank(),!remark.isBlank(),!in.isEmpty(),!out.isEmpty(),offset,time);
        }
        else {
            flag=DateBaseUtils.updateEvent(id,name,
                    left.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    right.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()-1,
                    importance,!detail.isBlank(),!remark.isBlank(),!in.isEmpty(),!out.isEmpty(),offset,time);
        }
        if (!flag)return -1;
        String path=Globe.getPath()+"/.data/"+Globe.getUser().getId()+"/"+id+"/";
        FileUtils.writeFile(detail,path+"detail");
        FileUtils.writeFile(remark,path+"remark");
        FileUtils.tmpInit(path);
        DateBaseUtils.deleteFile(id);
        for (int i=0;i<in.size();++i) {
            FileShow fileShow=in.get(i);
            fileShow.moveFile(path+"tmpIn/");
            DateBaseUtils.createFile(Globe.getUser().getId(),id,i,fileShow.getName(),
                    path+"in/"+fileShow.getPath(),FileUtils.IN);
        }
        for (int i=0;i<out.size();++i) {
            FileShow fileShow=out.get(i);
            fileShow.moveFile(path+"tmpOut/");
            DateBaseUtils.createFile(id,Globe.getUser().getId(),i,fileShow.getName(),
                    path+"out/"+fileShow.getPath(),FileUtils.OUT);
        }
        FileUtils.tmpTo(path);
        return 0;
    }

    public static boolean deleteEvent(int id) {
        return DateBaseUtils.deleteEventAndFile(id)&&
                FileUtils.deleteFolder(Globe.getPath()+"/.data/"+Globe.getUser().getId()+"/"+id);
    }

    public static int finishEvent(int id, String s, ArrayList<FileShow>files) {
        if (files==null||s==null) {
            ErrorUtils.Error();
            return -1;
        }
        String path=Globe.getPath()+"/.data/"+Globe.getUser().getId()+"/"+id+"/";
        if (!s.isBlank())FileUtils.writeFile(s, path+"remark");
        path+="out/";
        for (int i=0;i<files.size();++i) {
            FileShow fileShow=files.get(i);
            fileShow.moveFile(path);
            DateBaseUtils.createFile(Globe.getUser().getId(),id,i,
                    fileShow.getName(),path+fileShow.getPath(),FileUtils.OUT);
        }
        DateBaseUtils.finishEvent(id,!s.isBlank(),!files.isEmpty());
        return 0;
    }
}

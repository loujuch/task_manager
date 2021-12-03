package com.example.task_plan_manager;

import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import com.example.task_plan_manager.Utils.OtherUtils;

import java.util.ArrayList;

public class User {

    private int id;
    private String name;
    private String password;
    private int[] ip;
    private int port;
    private int[]sort;
    private boolean cut;
    private boolean delete_flag;
    private int host;

    public User() {
        ArrayList<String>data= FileUtils.readNow();
        if (data==null) {
            id=-1;
            return;
        }
        if (data.size()!=8) {
            ErrorUtils.Error();
            id=-1;
            return;
        }
        ip=new int[]{0,0,0,0};
        sort=new int[]{0,0,0,0};
        id=Integer.parseInt(data.get(0));
        name=data.get(1);
        password=data.get(2);
        port=OtherUtils.getIp(ip,data.get(3));
        OtherUtils.getSortPart(Integer.parseInt(data.get(4)),sort);
        cut=Boolean.parseBoolean(data.get(5));
        delete_flag=Boolean.getBoolean(data.get(6));
        host=Integer.parseInt(data.get(7));
    }

    public User(String name, String password, int[] ip, int port) {
        if(name==null||password==null||ip==null) {
            ErrorUtils.Error();
            return;
        }
        this.name=name;
        this.password=password;
        this.ip=ip;
        this.port=port;
//        >=10升序，<10降序
//        end 升序 importance 降序 start 升序 name 降序
        sort=new int[]{12,3,11,10};
        cut=false;
        delete_flag=false;
        host=-1;
        id=-1;
    }

    public byte[] getStringStream() {
        if(name==null||password==null||ip==null||sort==null) {
            ErrorUtils.Error();
            return null;
        }
        if (ip.length!=4||sort.length!=4) {
            ErrorUtils.Error();
            return null;
        }
        return String.format("%d\n%s\n%s\n%s\n%d\n%b\n%b\n%d\n",
                id,name,password,OtherUtils.setIp(ip,port),OtherUtils.getSortWhole(sort),cut,delete_flag,host)
                .getBytes();
    }

    public String getOutValues() {
        if(name==null||password==null||ip==null||sort==null) {
            ErrorUtils.Error();
            return "values('','',0,'',0,0,-1);";
        }
        return String.format("values('%s', '%s', %d, '%s', %b, %b, %d);",
                name,password,OtherUtils.getSortWhole(sort),OtherUtils.setIp(ip,port),cut,delete_flag,host);
    }

    public String getMainJudge() {
        if(name==null||password==null||ip==null||sort==null) {
            ErrorUtils.Error();
            return "where name='' and password='' and ip='' ";
        }
        return "where name='" +name+"' and password='"+password+"' and ip='"+OtherUtils.setIp(ip,port)+"';";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int[] getIp() {
        return ip;
    }

    public void setIp(int[] ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int[] getSort() {
        return sort;
    }

    public void setSort(int[] sort) {
        this.sort = sort;
    }

    public boolean isCut() {
        return cut;
    }

    public void setCut(boolean cut) {
        this.cut = cut;
    }

    public boolean isDelete() {
        return delete_flag;
    }

    public void setDelete(boolean delete_flag) {
        this.delete_flag = delete_flag;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

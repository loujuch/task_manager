package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.other.*;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DateBaseUtils {

    private final static String[]TABLE={
            "create table if not exists user" +
                    "(id integer primary key autoincrement, " +
                    "name varchar(16), " +
                    "password varchar(16), " +
                    "sort int, " +
                    "ip char(32), " +
                    "cut boolean, " +
                    "delete_flag boolean, " +
                    "host int);" ,
            "create table if not exists event" +
                    "(id integer primary key autoincrement, " +
                    "holder int, " +
                    "name varchar(64), " +
                    "importance int, " +
                    "belong int, " +
                    "start integer, " +
                    "end integer, " +
                    "last integer, "+
                    "offset int, " +
                    "time int, " +
                    "finish boolean, " +
                    "detail boolean, " +
                    "remark boolean, " +
                    "in_flag boolean, " +
                    "out boolean);" ,
            "create table if not exists file" +
                    "(id integer primary key autoincrement, " +
                    "event int, " +
                    "use int, " +
                    "number int, " +
                    "name varchar(256), " +
                    "path varchar(512), " +
                    "host int, " +
                    "io boolean);"
    };

    private static boolean operator(String s) {
        Connection connection=null;
        Statement statement=null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+ Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate(s);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return true;
    }

    private static int getNumOperator(String s) {
        int num=-1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total "+s);
            if (resultSet.next()) {
                num=resultSet.getInt("total");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return num;
    }

    public static int createOperator(String s, String str)  {
        int num=-1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try {
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate(s);
            resultSet=statement.executeQuery("select last_insert_rowid() now_id from "+str);
            if (resultSet.next()) {
                num=resultSet.getInt("now_id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet!=null;
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (num==0)num=-1;
        return num;
    }

    public static boolean init() {
        return operator(TABLE[0])&&operator(TABLE[1])&&operator(TABLE[2]);
    }

    public static boolean existUser(User user) {
        if (user==null) {
            ErrorUtils.Error();
            return false;
        }
        int num=getNumOperator("from user "+user.getMainJudge());
        return num>0;
    }

    public static int createUser(User user) {
        if (user==null) {
            ErrorUtils.Error();
            return -1;
        }
        return createOperator("insert into user (name, password, sort, ip, cut, delete_flag, host) "+
                user.getOutValues(),"user");
    }

    public static boolean deleteUser(int id) {
        return operator("delete from user where id="+id);
    }

    public static boolean deleteEventAndFile(int id) {
        return operator("delete from event where id="+id)&&
                operator("delete from file where event="+id+" and use="+Globe.getUser().getId());
    }

    public static int createEvent(String name, long start, long end, int importance, boolean detail,
                                  boolean in, int offset, int time) {
        if (name==null) {
            ErrorUtils.Error();
            return -1;
        }
        return createOperator("insert into event " +
                        "(holder, name, importance, start, end, offset, time, finish, detail, " +
                        "remark, in_flag, out, last, belong)" +
                        String.format("values(%d, '%s', %d, %d, %d, %d, %d, %b, %b, %b, %b, %b, %d, %d);",
                                Globe.getUser().getId(),name,importance,start,end,offset,time,false,detail,
                                false,in,false,OtherUtils.getNowTime(),-1),
                "event"
        );
    }

    public static int createEvent(String name, long start, long end, int importance, boolean detail,
                                  boolean in, int offset, int time, int belong) {
        if (name==null) {
            ErrorUtils.Error();
            return -1;
        }
        return createOperator("insert into event " +
                        "(holder, name, importance, start, end, offset, time, finish, detail, " +
                        "remark, in_flag, out, last, belong)" +
                        String.format("values(%d, '%s', %d, %d, %d, %d, %d, %b, %b, %b, %b, %b, %d, %d);",
                                Globe.getUser().getId(),name,importance,start,end,offset,time,false,detail,
                                false,in,false,OtherUtils.getNowTime(), belong), "event"
        );
    }

    public static int createFile(int id, int num, int i, String name, String path, boolean io) {
        if (name==null) {
            ErrorUtils.Error();
            return -1;
        }
        return createOperator("insert into file " +
                "(use, event, number, name, path, host, io)" +
                "values("+id+","+num+","+i+",'"+name+"','"+path+"',"+
                Globe.getUser().getHost()+","+io+");","event");
    }

    public static int getNum(int id, String s) {
        if (s==null) {
            ErrorUtils.Error();
            return -1;
        }
        return getNumOperator("from event "+s+" and holder="+id+";");
    }

    public static int getNum(int id, long time,Globe.SELECT now) {
        String judge=switch (now) {
            case TASK -> String.format("holder=%d and end>=%d and finish=%b and offset=-1",id,time,false);
            case FINISHED -> String.format("holder=%d and finish=%b and offset=-1",id,true);
            case OLD -> String.format("holder=%d and end<%d and finish=%b and offset=-1",id,time,false);
            case PLAN -> String.format("holder=%d and offset>=0",id);
            default -> null;
        };
        if (judge==null)return -1;
        return getNumOperator(" from event where "+judge);
    }

    public static int getFileNum(int user, int event, int index) {
        return getNumOperator(" from file where "+
                String.format("use=%d and event=%d and number=%d;",user,event,index));
    }

    public static boolean finishEvent(int id, boolean remark, boolean out) {
        return operator("update event set finish=true,remark="+remark+"," +
                "out="+out+",last="+OtherUtils.getNowTime()+" where id="+id);
    }

    public static boolean deleteFile(int id) {
        return operator("delete from file where event="+id);
    }

    public static boolean updateEvent(int id, String name, long start, long end, int importance,
                                      boolean detail, boolean remark, boolean in, boolean out, int offset, int time) {
        if (start<0) {
            return operator("update event set " +
                    String.format("name='%s', importance=%d, offset=%d, time=%d, " +
                                    "detail=%b, remark=%b, in_flag=%b, out=%b, last=%d", name, importance,
                            offset, time, detail, remark, in, out, OtherUtils.getNowTime()) + " where id=" + id);
        }
        return operator("update event set " +
                String.format("name='%s', start=%d, end=%d, importance=%d, offset=%d, time=%d, " +
                                "detail=%b, remark=%b, in_flag=%b, out=%b, last=%d", name, start, end, importance,
                        offset, time, detail, remark, in, out, OtherUtils.getNowTime()) + " where id=" + id);
    }

    public static User getUser(String judge) {
        if (judge==null) {
            ErrorUtils.Error();
            return null;
        }
        User user=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total from user "+judge);
            int num=0;
            if (resultSet.next()) {
                num=resultSet.getInt("total");
            }
            resultSet=statement.executeQuery("select * from user "+judge);
            if (num!=1) {
                ErrorUtils.Error();
                return null;
            }
            resultSet.next();
            int[]ip={0,0,0,0};
            int port=OtherUtils.getIp(ip,resultSet.getString("ip"));
            if (port<0) {
                ErrorUtils.IpFormatError();
                return null;
            }
            user=new User(resultSet.getString("name"),
                    resultSet.getString("password"),
                    ip,port);
            user.setId(resultSet.getInt("id"));
            user.setCut(resultSet.getBoolean("cut"));
            user.setDelete(resultSet.getBoolean("delete_flag"));
            int[]sort={0,0,0,0};
            OtherUtils.getSortPart(resultSet.getInt("sort"),sort);
            user.setSort(sort);
            user.setHost(resultSet.getInt("host"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return user;
    }

    public static ArrayList<Event> getEvent(int id, String s, int offset) {
        ArrayList<Event>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select * from event "+s+" and holder="+id+
                    " limit 20 offset "+20*offset+";");
            while (resultSet.next()) {
                out.add(new Task(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("start"),
                        resultSet.getLong("end"),
                        resultSet.getInt("importance"),
                        resultSet.getBoolean("detail"),
                        resultSet.getBoolean("remark"),
                        resultSet.getBoolean("in_flag"),
                        resultSet.getBoolean("out"),
                        resultSet.getBoolean("finish")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return out;
    }

    public static List<Pair<Integer, String>> getFile(int use, int event, boolean io, int host) {
        List<Pair<Integer, String>>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select path,number from file where use="+use+
                    " and event="+event+
                    " and io="+io+
                    " and host="+host+";");
            while (resultSet.next()) {
                out.add(new Pair<>(resultSet.getInt("number"),resultSet.getString("path")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        if (out.isEmpty())return null;
        return out;
    }

    public static List<Object> getEventById(int id) {
        List<Object>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select * from event where id="+id+";");
            if (resultSet.next()) {
                out.add(resultSet.getString("name"));
                out.add(resultSet.getInt("importance"));
                out.add(resultSet.getLong("start"));
                out.add(resultSet.getLong("end"));
                out.add(resultSet.getBoolean("detail"));
                out.add(resultSet.getBoolean("remark"));
                out.add(resultSet.getBoolean("in_flag"));
                out.add(resultSet.getBoolean("out"));
                out.add(resultSet.getInt("offset"));
                out.add(resultSet.getInt("time"));
                out.add(resultSet.getBoolean("finish"));
            } else {
                return null;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return out;
    }

    public static ArrayList<Event> getEvent(int id, long time, Globe.SELECT now, int offset) {
        String judge=switch (now) {
            case TASK -> String.format("holder=%d and end>=%d and finish=%b and offset=-1",id,time,false);
            case FINISHED -> String.format("holder=%d and finish=%b and offset=-1",id,true);
            case OLD -> String.format("holder=%d and end<%d and finish=%b and offset=-1",id,time,false);
            case PLAN -> String.format("holder=%d and offset>=0",id);
            default -> null;
        };
        ArrayList<Event>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select * from event where "+judge+" order by "
                    +OtherUtils.getSortString(Globe.getUser().getSort())+
                    "limit 20 offset "+20*offset);
            if (now == Globe.SELECT.PLAN) {
                while(resultSet.next()) {
                    out.add(new Plan(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getLong("start"),
                            resultSet.getLong("end"),
                            resultSet.getInt("importance"),
                            resultSet.getBoolean("detail"),
                            resultSet.getBoolean("remark"),
                            resultSet.getBoolean("in_flag"),
                            resultSet.getBoolean("out"),
                            resultSet.getInt("offset"),
                            resultSet.getInt("time")));
                }
            } else {
                while (resultSet.next()) {
                    out.add(new Task(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getLong("start"),
                            resultSet.getLong("end"),
                            resultSet.getInt("importance"),
                            resultSet.getBoolean("detail"),
                            resultSet.getBoolean("remark"),
                            resultSet.getBoolean("in_flag"),
                            resultSet.getBoolean("out"),
                            resultSet.getBoolean("finish")));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return out;
    }

    public static List<Integer> getEventIdByBelong(int belong) {
        List<Integer>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:"+Globe.getPath()+"/task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select id from event where finish = false and belong="+belong+";");
            while (resultSet.next()) {
                out.add(resultSet.getInt("id"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException throw_ables) {
                throw_ables.printStackTrace();
            }
        }
        return out;
    }


}

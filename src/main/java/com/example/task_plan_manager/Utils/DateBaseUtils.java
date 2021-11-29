package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.*;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DateBaseUtils {

    private final static String TAG="com.example.task_plan_manager.Utils.DateBaseUtils.";

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
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
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

    public static boolean init() {
        Connection connection=null;
        Statement statement=null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            for (String s:TABLE) {
                statement.executeUpdate(s);
            }
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

    public static boolean existUser(User user) {
        if (user==null) {
            ErrorUtils.NullPointerInputError(TAG+"existUser");
            return false;
        }
        int num=0;
        Connection connection=null;
        Statement statement=null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select count(*) total from user "+user.getMainJudge());
            if (resultSet.next()) {
                num=resultSet.getInt("total");
            }
        } catch (SQLException | ClassNotFoundException throw_ables) {
            throw_ables.printStackTrace();
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
        return num!=0;
    }

    public static User createUser(User user) {
        if (user==null) {
            ErrorUtils.NullPointerInputError(TAG+"createUser");
            return null;
        }
        Connection connection=null;
        Statement statement=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("insert into user " +
                    "(name, password, sort, ip, cut, delete_flag, host) "+user.getOutValues());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
        return getUser(user.getMainJudge());
    }

    public static User getUser(String judge) {
        if (judge==null) {
            ErrorUtils.NullPointerInputError(TAG+"getUser");
            return null;
        }
        User user=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total from user "+judge);
            int num=0;
            if (resultSet.next()) {
                num=resultSet.getInt("total");
            }
            resultSet=statement.executeQuery("select * from user "+judge);
            if (num!=1) {
                ErrorUtils.ArrayLengthError(TAG+"getUser",resultSet.getRow());
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
            ErrorUtils.NullPointerInputError(TAG+"createTask");
            return -1;
        }
        int num=-1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try {
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("insert into event " +
                    "(holder, name, importance, start, end, offset, time, finish, detail, " +
                    "remark, in_flag, out, last)" +
                    String.format("values(%d, '%s', %d, %d, %d, %d, %d, %b, %b, %b, %b, " +
                                    "%b, %d);",
                    Globe.getUser().getId(),name,importance,start,end,offset,time,false,detail,
                            false,in,false,OtherUtils.getNowTime()));
            resultSet=statement.executeQuery("select last_insert_rowid() now_id from event");
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

    public static int createFile(int id, int num, int i, String name, String path, boolean io) {
        if (name==null) {
            ErrorUtils.NullPointerInputError(TAG+"createTask");
            return -1;
        }
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try {
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("insert into file " +
                    "(use, event, number, name, path, host, io)" +
                    "values("+id+","+num+","+i+",'"+name+"','"+path+"',"+
                    Globe.getUser().getHost()+","+io+");");
            resultSet=statement.executeQuery("select last_insert_rowid() now_id from event");
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
                if(resultSet!=null)resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (num==0)num=-1;
        return num;
    }

    public static int getNum(int id, String s) {
        if (s==null) {
            ErrorUtils.NullPointerInputError(TAG+"getNum");
            return -1;
        }
        int num=-1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total from event "+s+" and holder="+id+";");
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

    public static int getNum(int id, long time,Globe.SELECT now) {
        int num=-1;
        String judge=switch (now) {
            case TASK -> String.format("holder=%d and end>=%d and finish=%b and offset=-1",id,time,false);
            case FINISHED -> String.format("holder=%d and finish=%b and offset=-1",id,true);
            case OLD -> String.format("holder=%d and end<%d and finish=%b and offset=-1",id,time,false);
            case PLAN -> String.format("holder=%d and offset>=0",id);
            default -> null;
        };
        if (judge==null)return -1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total from event where "+judge);
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

    public static ArrayList<Event> getEvent(int id, String s, int offset) {
        ArrayList<Event>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
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
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
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

    public static List<Object> getEventById(int id) {
        List<Object>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
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

    public static int getFileNum(int user, int event, int index) {
        int num=-1;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select count(*) total from file where "+
                    String.format("use=%d and event=%d and number=%d;",user,event,index));
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

    public static List<Pair<Integer, String>> getFile(int use, int event, boolean io, int host) {
        List<Pair<Integer, String>>out=new ArrayList<>();
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
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

    public static int finishEvent(int id, boolean remark, boolean out) {
        Connection connection=null;
        Statement statement=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("update event set finish=true,remark="+remark+"," +
                    "out="+out+",last="+OtherUtils.getNowTime()+" where id="+id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
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
        return 0;
    }

    public static boolean deleteFile(int id) {
        Connection connection=null;
        Statement statement=null;
        try {
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("delete from file where event="+id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
                assert statement != null;
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return true;
    }

    public static boolean updateEvent(int id, String name, long start, long end, int importance,
                                      boolean detail, boolean remark, boolean in, boolean out, int offset, int time) {
        Connection connection=null;
        Statement statement=null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection=DriverManager.getConnection("jdbc:sqlite:task_plan.db");
            statement=connection.createStatement();
            statement.executeUpdate("update event set "+
                    String.format("name='%s', start=%d, end=%d, importance=%d, offset=%d, time=%d, " +
                            "detail=%b, remark=%b, in_flag=%b, out=%b, last=%d",name,start,end,importance,
                    offset,time,detail,remark,in,out,OtherUtils.getNowTime())+" where id="+id);
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
}

package com.example.task_plan_manager.Utils;

import javafx.scene.control.RadioButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class OtherUtils {

    private final static String TAG="com.example.task_plan_manager.Utils.OtherUtils.";
    public final static String[]LIST={"name","start","end","importance"};
    public final static String[]ORDER={"asc","desc"};

    public static int getSortWhole(int[]sort) {
        if (sort==null) {
            ErrorUtils.NullPointerInputError(TAG+"getSortWhole");
            return -1;
        }
        if (sort.length!=4) {
            ErrorUtils.ArrayLengthError(TAG+"getSortWhole",sort.length);
            return -2;
        }
        int num=0;
        for(int i=0,j=0;j<4;++j,i+=8) {
            num|=(sort[j]<<i);
        }
        return num;
    }

    public static boolean getSortPart(int whole, int[]sort) {
        if (sort==null) {
            ErrorUtils.NullPointerInputError(TAG+"getSortWhole");
            return false;
        }
        if (sort.length!=4) {
            ErrorUtils.ArrayLengthError(TAG+"getSortWhole",sort.length);
            return false;
        }
        int mark=0xff;
        for(int i=0,j=0;j<4;++j,i+=8) {
            sort[j]=(whole>>>i)&mark;
        }
        return true;
    }

    public static int getIp(int[]ip, String s) {
        if(ip==null||s==null) {
            ErrorUtils.NullPointerInputError(TAG+"getIp");
            return -3;
        }
        if (s.isEmpty()) {
            return -2;
        }
        int num=0;
        int index=0;
        boolean flag=false;
        for(int i=0;i<s.length();++i) {
            switch (s.charAt(i)) {
                case '.':
                    if(index>2) {
                        ErrorUtils.IpFormatError();
                        return -1;
                    }
                    ++index;
                    break;
                case ':':
                    if(index!=3||flag) {
                        ErrorUtils.IpFormatError();
                        return -1;
                    }
                    flag=true;
                    break;
                case '0','1','2','3','4','5','6','7','8','9':
                    if (flag) {
                        num*=10;
                        num+=s.charAt(i)-'0';
                    } else {
                        ip[index]*=10;
                        ip[index]+=s.charAt(i)-'0';
                    }
                    break;
                case ' ':
                    break;
                default:
                    return -1;
            }
        }
        if (!flag) {
            ErrorUtils.IpFormatError();
            return -1;
        }
        for(int i:ip) {
            if (i>255||i<0) {
                ErrorUtils.IpValueError();
                return -2;
            }
        }
        return num;
    }

    public static String setIp(int[]ip, int port) {
        if (ip==null) {
            ErrorUtils.NullPointerInputError(TAG+"setIp");
            return null;
        }
        if (ip.length!=4) {
            ErrorUtils.ArrayLengthError(TAG+"setIp",ip.length);
            return null;
        }
        if (port<0)return "";
        return String.format("%d.%d.%d.%d:%d",ip[0],ip[1],ip[2],ip[3],port);
    }

    public static String getDateString(Date date) {
        if (date==null) {
            ErrorUtils.NullPointerInputError(TAG+"getDateString");
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getSortString(int[]sort) {
        if (sort==null) {
            ErrorUtils.NullPointerInputError(TAG+"getSortString");
            return "";
        }
        if (sort.length!=4) {
            ErrorUtils.ArrayLengthError(TAG+"getDateString",sort.length);
            return "";
        }
        StringBuilder out= new StringBuilder();
        out.append(LIST[sort[0] % 10]).append(" ").append(ORDER[sort[0] < 10 ? 1 : 0]);
        for (int i=0;i<4;++i) {
            out.append(", ").append(LIST[sort[i] % 10]).append(" ").append(ORDER[sort[i] < 10 ? 1 : 0]);
        }
        return out+" ";
    }

    public static long getNowTime() {
        return new Date().getTime();
    }

    public static LocalDate getNowStartTime(int offset) {
        return Instant.ofEpochMilli(getNowTime()).atZone(ZoneId.systemDefault()).toLocalDate().plusDays(offset).
                atStartOfDay().toLocalDate();
    }

    public static LocalDate getOffsetTime(LocalDate date) {
        return date.atStartOfDay().plusHours(12).toLocalDate();
    }

    public static LocalDate getStartTime(LocalDate date) {
        return date.atStartOfDay().toLocalDate();
    }

    public static String getArrayListString(ArrayList<String> readFile) {
        if (readFile==null||readFile.isEmpty())return "";
        StringBuilder tmp= new StringBuilder(readFile.get(0));
        for (int i=1;i<readFile.size();++i) tmp.append("\n").append(readFile.get(i));
        return tmp.toString();
    }
}

package com.example.task_plan_manager.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherUtils {

    public final static String[]LIST={"name","start","end","importance"};
    public final static String[]ORDER={"asc","desc"};

    public static int getSortWhole(int[]sort) {
        if (sort==null) {
            ErrorUtils.Error();
            return -1;
        }
        if (sort.length!=4) {
            ErrorUtils.Error();
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
            ErrorUtils.Error();
            return false;
        }
        if (sort.length!=4) {
            ErrorUtils.Error();
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
            ErrorUtils.Error();
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
            ErrorUtils.Error();
            return null;
        }
        if (ip.length!=4) {
            ErrorUtils.Error();
            return null;
        }
        if (port<0)return "";
        return String.format("%d.%d.%d.%d:%d",ip[0],ip[1],ip[2],ip[3],port);
    }

    public static String getDateString(Date date) {
        if (date==null) {
            ErrorUtils.Error();
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getSortString(int[]sort) {
        if (sort==null) {
            ErrorUtils.Error();
            return "";
        }
        if (sort.length!=4) {
            ErrorUtils.Error();
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
}

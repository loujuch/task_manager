package com.example.task_plan_manager;


import com.example.task_plan_manager.Controllers.BarShow;

import java.util.Date;

public class Event {
    public final static int NECESSARY=2;
    public final static int IMPORTANT=1;
    public final static int COMMON=0;

    public final static String[]IMPORT={"普通","重要","十分重要"};
    public final static String[]status={"正进行","已过时","未开始","已完成"};

    protected int id;
    protected String name;
    protected Date start;
    protected Date end;
    protected int importance;
    protected boolean detail;
    protected boolean remark;
    protected boolean in;
    protected boolean out;

    public Event(int id, String name, long start, long end, int importance,
                 boolean detail, boolean remark, boolean in, boolean out) {
        this.id=id;
        this.name=name;
        this.start=new Date(start);
        this.end=new Date(end);
        this.importance=importance;
        this.detail=detail;
        this.remark=remark;
        this.in=in;
        this.out=out;
    }

    public BarShow getBarShow(int offset, String s, boolean sub) {
        return new BarShow(name,start,end,importance,id,offset,s,sub);
    }
}

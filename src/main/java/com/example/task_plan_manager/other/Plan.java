package com.example.task_plan_manager.other;

public class Plan extends Event {

    private int offset;
    private int time;

    public Plan(int id, String name, long start, long end, int importance,
                boolean detail, boolean remark, boolean in, boolean out, int offset, int time) {
        super(id,name,start,end,importance,detail,remark,in,out);
        this.offset=offset;
        this.time=time;
    }
}

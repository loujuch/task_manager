package com.example.task_plan_manager;

public class Task extends Event {

    private boolean finished;

    public Task(int id, String name, long start, long end, int importance,
                boolean detail, boolean remark, boolean in, boolean out, boolean finished) {
        super(id,name,start,end,importance,detail,remark,in,out);
        this.finished=finished;
    }
}

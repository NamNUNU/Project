package com.soma.nunu.oneto;

/**
 * Created by user on 2016-07-22.
 */
public class user {

    String name;
    String time;
    String num;

    public user(String num, String name, String time){
        this.num = num;
        this.name=name;
        this.time=time;
    }
    public void setNum(String num){
        this.num = num;
    }

    public String getNum(){
        return num;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }
}


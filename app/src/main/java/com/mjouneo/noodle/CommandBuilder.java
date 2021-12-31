package com.mjouneo.noodle;

import java.util.List;

public class CommandBuilder {

    public static String server = "http://192.168.4.1/?";
    private List<String> IDs;
    private String command;
    private String duration;

    public CommandBuilder startTimer(List<String> IDs, int duration){
        this.IDs = IDs;
        this.command = "F";
        this.duration = String.valueOf(duration);
        return this;
    }

    public CommandBuilder stopTimer(List<String> IDs){
        this.IDs = IDs;
        this.command = "B";
        this.duration = "";
        return this;
    }

    public String build(){
        return server + "IDs="+ IDs.toString() +"&&State=" + command + "&&Dur=" + duration;
    }



}

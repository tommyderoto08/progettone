package com.example.progettone.model;

public class Time {
    protected int minute;
    protected int seconds;
    protected int milliseconds;

    public Time(){
        this.milliseconds = 0;
        this.minute = 0;
        this.seconds = 0;
    }

    public void changeMills(){
        if(this.milliseconds==1000){
            seconds++;
            milliseconds = 0;
        }
    }

    public void changeSec(){
        if(this.seconds==60){
            minute++;
            seconds = 0;
        }
    }


}

package com.example.towerdifence.model;

public class Towers {
    private int danno;
    private double velAtt;
    private int lvl;

    public Towers(int danno, double velAtt) {
        this.danno = danno;
        this.velAtt = velAtt;
        this.lvl = 1;
    }

    public void setDanno(int danno) {
        this.danno = danno;
    }

    public void setVelAtt(double velAtt) {
        this.velAtt = velAtt;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getDanno() {
        return danno;
    }

    public double getVelAtt() {
        return velAtt;
    }

    public int getLvl() {
        return lvl;
    }
}

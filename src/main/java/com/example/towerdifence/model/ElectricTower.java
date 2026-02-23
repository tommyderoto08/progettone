package com.example.towerdifence.model;

public class ElectricTower extends Towers{
    private int elecDmg;

    public ElectricTower(int danno, double velAtt) {
        super(danno, velAtt);
        this.elecDmg = 12;
    }

    public void ElecDamage(){

    }
}

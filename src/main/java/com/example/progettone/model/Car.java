package com.example.progettone.model;


public class Car {
    protected String pilot;
    protected Scuderia scuderia;
    protected int vel;
    protected int position;
    protected boolean boostMode;
    protected Time t;
    protected Circuito circuito;

    public Car(String pilot, Scuderia scuderia) {
        this.pilot = pilot;
        this.scuderia = scuderia;
        vel = 0;
        position = 0;
        boostMode = false;
        t = new Time();
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }
}

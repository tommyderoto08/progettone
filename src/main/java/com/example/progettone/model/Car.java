package com.example.progettone.model;


import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Car {
    private String pilota;
    private Scuderia scuderia;
    private Circle car;
    private PathTransition transition;
    private boolean drsDisponibile = false;
    private double performanceGiro;

    public Car(String pilota, Scuderia scuderia, Path tracciato, double tempoBase) {
        this.pilota = pilota;
        this.scuderia = scuderia;
        //crea il cerchio con il colore della scuderia
        this.car = new Circle(8, scuderia.getColor());

        this.transition = new PathTransition(Duration.seconds(tempoBase), tracciato, car);
        this.transition.setInterpolator(Interpolator.LINEAR);
        this.transition.setCycleCount(PathTransition.INDEFINITE);
        this.transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

    }

    public void aggiorna(double progress, Settore settoreAttuale, double gapDavanti){
        double nuovaVelocità = settoreAttuale.getSpeedMultiplier();

        //Logica DRS, gap < 1.0
        if(settoreAttuale.isDRSZone() && gapDavanti < 1.0){
            nuovaVelocità+=0.7;
            car.setStroke(Color.GREEN);
        }else{
            car.setStroke(null);
        }



        transition.setRate(nuovaVelocità);

    }

//    public Circle createCircle(){
//
//    }

    public void nuovaPerformance(){
        this.performanceGiro = 0.95 + (Math.random()*0.10);
    }

    public String getTelemetria(){
        return pilota + " - " + scuderia.getNome();
    }

    public Circle getCar() {
        return car;
    }

    public void setCar(Circle car) {
        this.car = car;
    }

    public PathTransition getTransition() {
        return transition;
    }

    public void setTransition(PathTransition transition) {
        this.transition = transition;
    }

    public boolean isDrsDisponibile() {
        return drsDisponibile;
    }

    public void setDrsDisponibile(boolean drsDisponibile) {
        this.drsDisponibile = drsDisponibile;
    }

    public double getPerformanceGiro() {
        return performanceGiro;
    }
}

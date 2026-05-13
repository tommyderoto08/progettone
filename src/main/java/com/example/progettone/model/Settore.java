package com.example.progettone.model;

import javafx.animation.PathTransition;

public class Settore {
    private double startProgress;
    private double endProgress;
    private boolean isDRSZone;
    private double speedMultiplier;

    public Settore(double startProgress, double endProgress, boolean isDRSZone, double speedMultiplier) {
        this.startProgress = startProgress;
        this.endProgress = endProgress;
        this.isDRSZone = isDRSZone;
        this.speedMultiplier = speedMultiplier;
    }

    public double getStartProgress() {
        return startProgress;
    }

    public double getEndProgress() {
        return endProgress;
    }

    public boolean isDRSZone() {
        return isDRSZone;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}

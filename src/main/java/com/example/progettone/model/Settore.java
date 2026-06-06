package com.example.progettone.model;

public class Settore {
    private final double startProgress;
    private final double endProgress;
    private final boolean isDRSZone;
    private final double speedMultiplier;
    private final String nome;

    public Settore(String nome, double startProgress, double endProgress, boolean isDRSZone, double speedMultiplier) {
        this.nome = nome;
        this.startProgress = startProgress;
        this.endProgress = endProgress;
        this.isDRSZone = isDRSZone;
        this.speedMultiplier = speedMultiplier;
    }

    public String getNome()          { return nome; }
    public double getStartProgress() { return startProgress; }
    public double getEndProgress()   { return endProgress; }
    public boolean isDRSZone()       { return isDRSZone; }
    public double getSpeedMultiplier(){ return speedMultiplier; }
}

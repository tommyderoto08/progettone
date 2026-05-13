package com.example.progettone.model;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.*;

import java.util.List;

public class Circuito {
    private List<Settore> settori;
    private Path percorso;

    public void simula(Car leader, Car inseguitore){
        new AnimationTimer(){
            @Override
            public void handle(long now){
                double progInseguitore = inseguitore.getTransition().getCurrentTime().toMillis()/
                                             inseguitore.getTransition().getDuration().toMillis();
                double progLeader = leader.getTransition().getCurrentTime().toMillis()/
                                    leader.getTransition().getDuration().toMillis();

                //Trova in che settore è la macchina
                Settore attuale = trovaSettore(progInseguitore);

                //Calcolo del gap
                double gap = inseguitore.getTransition().getCurrentTime().toSeconds() - leader.getTransition().getCurrentTime().toSeconds();

                //Aggiorna la logica
                inseguitore.aggiorna(progInseguitore, attuale, gap);

            }
        }.start();
    }

    private Settore trovaSettore(double progress){
        for(Settore s : settori){
            //Verifichiamo se la macchina si trova all'interno del settore
            if(progress >= s.getEndProgress() && progress < s.getEndProgress()){
                return s;
            }
        }

        return settori.getFirst();
    }

    public Path percorsoBahrain(){
        Path tracciato = new Path();

        //Partenza e primo settore
        tracciato.getElements().add(new MoveTo(100, 500));
        tracciato.getElements().add(new LineTo(500, 500));//Rettilineo verso T1
        tracciato.getElements().add(new QuadCurveTo(550, 500, 530, 450));//Curva 01 e 02
        tracciato.getElements().add(new LineTo(510, 420));//Curva 03
        tracciato.getElements().add(new LineTo(150, 80));//Rettilineo verso T4

        //Secondo settore
        tracciato.getElements().add(new QuadCurveTo(130, 50, 180, 70));//Curva 04
        tracciato.getElements().add(new LineTo(300, 350));//Verto T5 e 6
        tracciato.getElements().add(new CubicCurveTo(350, 170, 320, 250, 350, 280));//Curve 05-06-07
        tracciato.getElements().add(new QuadCurveTo(450, 350, 500, 380));//Curva 08
        tracciato.getElements().add(new LineTo(250, 380));//Rettilineo verso T9 e 10
        tracciato.getElements().add(new QuadCurveTo(180, 380, 200, 430));//Curva 09 e 10

        //Terzo settore
        tracciato.getElements().add(new LineTo(550, 430));//Rettilineo verso T11
        tracciato.getElements().add(new QuadCurveTo(600, 450, 580, 350));//Curva 11
        tracciato.getElements().add(new CubicCurveTo(550, 280, 500, 200, 550, 100));//Curva 12 e salita verso T13
        tracciato.getElements().add(new QuadCurveTo(580, 50, 650, 150));//Curva 13
        tracciato.getElements().add(new LineTo(750, 450));//Rettilineo verso T14
        tracciato.getElements().add(new QuadCurveTo(780, 500, 700, 500));//Curva 14 e 15
        
        tracciato.getElements().add(new ClosePath());
        return tracciato;
    }

}

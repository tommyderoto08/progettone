package com.example.progettone.model;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Car {

    // ── Identificazione ────────────────────────────────────────────────────────
    private final String   pilota;
    private final Scuderia scuderia;
    private final int      numeroGara;   // numero sulla macchina

    // ── Grafica ─────────────────────────────────────────────────────────────────
    private final Circle car;
    private final Text   label;          // numero/iniziali sopra il cerchio

    // ── Animazione ──────────────────────────────────────────────────────────────
    private PathTransition transition;

    // ── Stato gara ──────────────────────────────────────────────────────────────
    private boolean drsDisponibile   = false;
    private double  performanceGiro  = 1.0;
    private int     giriCompletati   = 0;
    private int     posizione        = 1;
    private double  progressoTotale  = 0.0;  // giri + frazione corrente
    private boolean ritirata         = false;

    // ── Costruttore ─────────────────────────────────────────────────────────────
    public Car(String pilota, Scuderia scuderia, int numeroGara,
               Shape tracciato, double tempoBase) {
        this.pilota     = pilota;
        this.scuderia   = scuderia;
        this.numeroGara = numeroGara;

        // Cerchio colorato con bordo scuderia
        this.car = new Circle(9, scuderia.getColor());
        this.car.setStrokeWidth(2);

        // Etichetta con numero gara
        this.label = new Text(String.valueOf(numeroGara));
        this.label.setFont(Font.font("Monospace", FontWeight.BOLD, 8));
        this.label.setFill(Color.WHITE);

        // Transizione
        nuovaPerformance();
        double tempoReale = tempoBase / performanceGiro;
        this.transition = new PathTransition(Duration.seconds(tempoReale), tracciato, car);
        this.transition.setInterpolator(Interpolator.LINEAR);
        this.transition.setCycleCount(PathTransition.INDEFINITE);
        this.transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        // Quando completa un giro
        this.transition.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            double durata = transition.getDuration().toMillis();
            if (durata > 0 && oldTime.toMillis() > newTime.toMillis()) {
                // reset del timer = giro completato
                giriCompletati++;
                nuovaPerformance();
                // aggiorna rate con nuova performance
                double baseRate = transition.getRate() > 0
                        ? transition.getRate() : 1.0;
                transition.setRate(performanceGiro);
            }
            // aggiorna progresso totale
            progressoTotale = giriCompletati + (newTime.toMillis() / durata);
        });
    }

    // ── Logica aggiornamento ────────────────────────────────────────────────────
    public void aggiorna(Settore settoreAttuale, double gapDavanti) {
        if (ritirata) return;

        double rate = settoreAttuale.getSpeedMultiplier() * performanceGiro;

        // DRS: attivo se nella zona DRS e gap < 1.0 s
        if (settoreAttuale.isDRSZone() && drsDisponibile && gapDavanti < 1.0 && gapDavanti > 0) {
            rate += 0.15;
            car.setStroke(Color.LIMEGREEN);
            car.setStrokeWidth(2.5);
        } else {
            car.setStroke(null);
            car.setStrokeWidth(0);
        }

        transition.setRate(rate);
    }

    // ── Nuova performance random per ogni giro ──────────────────────────────────
    public void nuovaPerformance() {
        this.performanceGiro = 0.92 + (Math.random() * 0.16); // 0.92 – 1.08
    }

    // ── Posizionamento in griglia ───────────────────────────────────────────────
    /** Posiziona il cerchio + label in coordinate assolute nel Pane */
    public void setGrigliaPosition(double x, double y) {
        car.setCenterX(0);
        car.setCenterY(0);
        car.setTranslateX(x);
        car.setTranslateY(y);
        label.setTranslateX(x - 5);
        label.setTranslateY(y + 4);
    }

    // ── Getters ─────────────────────────────────────────────────────────────────
    public String   getPilota()           { return pilota; }
    public Scuderia getScuderia()         { return scuderia; }
    public int      getNumeroGara()       { return numeroGara; }
    public Circle   getCar()              { return car; }
    public Text     getLabel()            { return label; }
    public PathTransition getTransition() { return transition; }
    public boolean  isDrsDisponibile()    { return drsDisponibile; }
    public double   getPerformanceGiro()  { return performanceGiro; }
    public int      getGiriCompletati()   { return giriCompletati; }
    public int      getPosizione()        { return posizione; }
    public double   getProgressoTotale()  { return progressoTotale; }
    public boolean  isRitirata()          { return ritirata; }

    // ── Setters ─────────────────────────────────────────────────────────────────
    public void setDrsDisponibile(boolean drsDisponibile) {
        this.drsDisponibile = drsDisponibile;
    }
    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }
    public void ritirati() {
        this.ritirata = true;
        transition.pause();
        car.setFill(Color.DARKGRAY);
        car.setOpacity(0.4);
    }

    public String getTelemetria() {
        return String.format("#%d %-20s | %s | Giro %d | P%d",
                numeroGara, pilota, scuderia.getNome(),
                giriCompletati + 1, posizione);
    }
}

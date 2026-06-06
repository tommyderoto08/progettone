package com.example.progettone.model;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.shape.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.geometry.Bounds;
import javafx.scene.transform.Scale;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Circuito {

    // ── Settori del Bahrain ─────────────────────────────────────────────────────
    // progress da 0.0 (partenza) a 1.0 (fine giro)
    private final List<Settore> settori = new ArrayList<>(List.of(
            //           nome          start   end    DRS    speed
            new Settore("Settore 1",   0.00,  0.33, true,  1.05),  // rettilineo principale + DRS
            new Settore("Settore 2",   0.33,  0.66, false, 0.90),  // zona curve lente
            new Settore("Settore 3",   0.66,  1.00, true,  1.00)   // rettilineo arrivo + DRS
    ));

    private AnimationTimer timer;

    // ── Avvia simulazione ───────────────────────────────────────────────────────
    /**
     * Avvia l'AnimationTimer che aggiorna tutte le macchine ogni frame.
     * @param macchine lista completa delle auto in gara
     * @param giriTotali numero di giri della gara
     * @param onGiroCompletato callback chiamato quando una macchina completa un giro (aggiorna UI)
     * @param onFineGara callback chiamato quando la gara finisce
     */
    public void avviaSimulazione(List<Car> macchine, int giriTotali,
                                  Runnable onGiroCompletato, Runnable onFineGara) {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Aggiorna ogni auto
                for (int i = 0; i < macchine.size(); i++) {
                    Car car = macchine.get(i);
                    if (car.isRitirata()) continue;

                    double progCar = getCurrentProgress(car);
                    Settore attuale = trovaSettore(progCar);

                    // Trova l'auto davanti
                    double gapDavanti = Double.MAX_VALUE;
                    if (i > 0) {
                        Car davanti = macchine.get(i - 1);
                        gapDavanti = calcolaGap(car, davanti);
                    }

                    car.aggiorna(attuale, gapDavanti);
                }

                // Aggiorna classifiche
                aggiornaClassifica(macchine);
                onGiroCompletato.run();

                // Controlla fine gara
                Car leader = macchine.get(0);
                if (leader.getGiriCompletati() >= giriTotali) {
                    stop();
                    macchine.forEach(c -> {
                        if (!c.isRitirata()) c.getTransition().pause();
                    });
                    onFineGara.run();
                }
            }
        };
        timer.start();
    }

    public void stopSimulazione() {
        if (timer != null) timer.stop();
    }

    // ── Utilità ─────────────────────────────────────────────────────────────────
    /** Restituisce il progresso corrente (0-1) della transizione */
    public static double getCurrentProgress(Car car) {
        PathTransition t = car.getTransition();
        if (t.getDuration().toMillis() == 0) return 0;
        return t.getCurrentTime().toMillis() / t.getDuration().toMillis();
    }

    /** Trova il settore in base al progresso (BUG corretto: >= start e < end) */
    public Settore trovaSettore(double progress) {
        for (Settore s : settori) {
            if (progress >= s.getStartProgress() && progress < s.getEndProgress()) {
                return s;
            }
        }
        return settori.get(settori.size() - 1);
    }

    /** Gap in secondi tra inseguitore e macchina davanti (positivo = inseguitore è dietro) */
    private double calcolaGap(Car inseguitore, Car davanti) {
        double progI = getCurrentProgress(inseguitore)
                + inseguitore.getGiriCompletati();
        double progD = getCurrentProgress(davanti)
                + davanti.getGiriCompletati();
        double diff = progD - progI;
        // Converti in secondi approssimativi (1 giro ~ 90 s)
        return diff * inseguitore.getTransition().getDuration().toSeconds();
    }

    /** Riordina la lista macchine per progressoTotale decrescente e aggiorna posizione */
    public void aggiornaClassifica(List<Car> macchine) {
        macchine.sort(Comparator.comparingDouble(Car::getProgressoTotale).reversed());
        for (int i = 0; i < macchine.size(); i++) {
            macchine.get(i).setPosizione(i + 1);
        }
    }

    public Shape percorsoBahrain() {
        SVGPath circuito = new SVGPath();

        circuito.setContent("""
        m 158.49777,959.77438 c -128.157212,0.34389 -256.31662,-0.59613 -384.47478,-0.94157 -27.09642,-0.073 -55.88507,2.78195 -81.28895,-0.31386 -3.78481,-0.46123 -5.29043,-6.62971 -6.27714,-10.35728 -0.89643,-3.38652 -1.02614,-7.95965 0.62771,-10.98499 2.63553,-4.82109 8.36009,-8.18601 12.24042,-12.55428 13.59104,-15.3001 27.97785,-30.3067 40.17369,-46.45083 2.3462,-3.10576 3.20343,-7.46424 3.45243,-11.29885 0.2741,-4.22106 -0.45075,-8.78925 -1.88314,-12.86814 -9.55261,-27.20219 -21.83383,-53.68481 -31.07184,-80.97509 -2.16546,-6.39703 -3.09779,-13.35798 -3.13857,-20.08685 -0.0638,-10.53326 1.57459,-21.16664 2.82471,-31.69955 3.66697,-30.89621 7.90467,-61.73123 11.92657,-92.5878 8.74161,-67.0668 17.3182,-134.15638 26.36398,-201.18232 8.32097,-61.65542 17.05469,-123.25591 25.73627,-184.86175 2.19879,-15.60297 3.80988,-31.36183 6.90485,-46.76468 1.40365,-6.98561 2.70475,-15.11287 6.591,-20.40071 2.49551,-3.39554 8.18538,-4.39399 12.55428,-5.64942 4.73295,-1.36004 9.90746,-2.6455 14.75128,-2.197 6.45503,0.59769 13.11469,2.74348 19.14527,5.33557 5.89598,2.53424 11.70381,5.94271 16.63442,10.04342 5.6359,4.68729 10.64805,10.41609 15.06513,16.32056 5.83558,7.80062 10.00732,16.85641 15.69285,24.7947 5.40408,7.54532 11.10003,15.00593 17.57599,21.65613 16.854079,17.30755 34.437832,34.03847 52.414114,50.21712 18.640364,16.77633 37.121536,34.39751 57.4358239,48.64783 7.7236011,5.41805 17.6851621,7.18618 26.6778421,10.35728 10.884928,3.83837 22.513767,6.04616 32.95498,10.67113 5.147016,2.27989 10.008967,5.92215 13.809707,10.04343 4.882636,5.29443 8.929245,11.75262 12.240421,18.2037 2.756725,5.37086 5.245653,11.31004 5.963282,17.26213 1.06089,8.79918 0.18552,18.04614 -0.627713,26.9917 -0.96529,10.61819 -3.600002,21.08749 -4.707855,31.69955 -0.879908,8.42859 -2.374557,17.16218 -1.255428,25.42242 1.07787,7.95571 3.822825,16.32004 7.846426,23.22541 3.7182,6.38124 9.71288,11.67238 15.37899,16.63442 12.0145,10.52157 24.69696,20.43321 37.66283,29.81641 26.78934,19.38702 54.75567,37.17741 81.60281,56.49426 16.04664,11.54576 32.7332,21.52098 47.54934,34.36734 3.28295,2.84648 6.87911,8.06151 7.84642,12.08349 1.02045,4.24292 -0.31413,10.79885 -2.35393,14.43742 -1.8311,3.26629 -6.64129,5.14214 -10.35728,6.90485 -4.44429,2.10819 -9.27949,2.94495 -14.12356,3.45242 -6.14092,0.64334 -15.08883,-0.25778 -21.34228,-0.94156 -98.99326,-10.82431 -195.477525,-23.54284 -294.397825,-32.32727 -9.464971,-0.84052 -19.230979,1.6776 -28.247126,4.394 -7.304415,2.20069 -14.545161,5.88446 -20.714559,10.35727 -6.38488,4.62904 -11.93502,10.76256 -16.94828,16.94828 -7.95949,9.82098 -14.88582,20.55986 -21.96998,31.07184 -2.6454,3.92544 -6.18291,7.96774 -7.21871,12.24042 -0.6381,2.63217 0.31065,6.67552 2.197,8.47414 2.61226,2.49076 7.30004,3.91294 11.29885,4.394 37.744163,4.54065 75.922379,7.58504 113.930072,10.67113 10.9539915,0.88943 21.9624734,1.24368 32.954981,1.25543 184.749617,0.19749 369.741217,1.19531 554.271397,-0.62771 6.08561,-0.0601 12.47366,-2.08232 17.57599,-5.02171 4.52261,-2.60542 8.95285,-6.9207 11.29885,-11.61271 3.82652,-7.65303 5.88465,-16.7757 7.21871,-25.42242 1.49065,-9.6616 2.23393,-19.83255 1.25542,-29.50255 -0.80001,-7.90599 -3.85186,-15.67988 -6.59099,-23.22541 -2.80568,-7.72885 -6.386,-15.2219 -10.04342,-22.59771 -2.61972,-5.28309 -5.2861,-10.66489 -8.788,-15.37899 -4.65838,-6.27089 -10.05934,-12.12718 -15.69285,-17.57599 -7.13,-6.89623 -14.3983,-13.94491 -22.5977,-19.45913 -9.69045,-6.51697 -20.18343,-12.34856 -31.07184,-16.63442 -18.61415,-7.32684 -38.32465,-12.03161 -57.43582,-18.2037 -7.77591,-2.51129 -16.07271,-4.06506 -23.22542,-7.84643 -9.27247,-4.90201 -18.09275,-11.30029 -26.05012,-18.2037 -7.8401,-6.80167 -15.31691,-14.5233 -21.34228,-22.91156 -4.64577,-6.46764 -7.82096,-14.158 -10.67113,-21.65613 -3.95007,-10.39171 -7.02964,-21.21369 -9.72957,-32.01341 -1.79869,-7.19474 -3.85577,-14.63191 -3.76628,-21.96998 0.11975,-9.81944 1.4505,-20.06919 4.39399,-29.50256 5.73988,-18.39529 13.98901,-36.18009 21.65613,-53.9834 10.85044,-25.19509 20.49466,-51.39189 33.89656,-75.01181 5.53414,-9.75353 14.29301,-17.94509 22.91155,-25.10856 7.49278,-6.22776 16.31219,-12.42204 25.42242,-15.06513 7.83805,-2.274 17.2541,-0.94852 25.42241,0.31386 3.33978,0.51615 6.44554,2.83245 9.10185,5.02171 6.86402,5.65716 14.38992,11.17648 19.45913,18.51756 29.24582,42.35294 56.45018,86.5505 83.48596,130.56449 60.42569,98.37245 119.20283,197.76786 178.89846,296.59483 24.73189,40.944 49.38834,81.93551 74.3841,122.71807 18.73497,30.56759 39.40604,60.3033 56.80811,91.33238 2.37092,4.22752 3.09386,9.77338 2.51086,14.43742 -0.67242,5.37939 -1.78491,12.7729 -6.27714,15.69285 -16.43156,10.68052 -37.21199,18.20147 -56.49425,25.10855 -8.75563,3.13634 -18.26824,4.90362 -27.61942,5.02172 -222.58911,2.81123 -445.46659,3.1686 -668.20146,3.76628 z
    """);

        // Dimensioni della zona pista nel Controller
        double paneW = 900;
        double paneH = 580;

// Margine interno
        double margin = 40;

// Leggo le dimensioni reali dell'SVG
        javafx.geometry.Bounds b = circuito.getLayoutBounds();

// Calcolo scala automatica per farlo stare dentro la pista
        double scaleX = (paneW - margin * 2) / b.getWidth();
        double scaleY = (paneH - margin * 2) / b.getHeight();
        double scale = Math.min(scaleX, scaleY);

// Scala rispetto all'origine 0,0
        circuito.getTransforms().add(new javafx.scene.transform.Scale(scale, scale, 0, 0));

// Centro il circuito nel Pane
        double tx = (paneW - b.getWidth() * scale) / 2 - b.getMinX() * scale;
        double ty = (paneH - b.getHeight() * scale) / 2 - b.getMinY() * scale;

        circuito.setTranslateX(tx);
        circuito.setTranslateY(ty);

        return circuito;
    }

    public List<Settore> getSettori() { return settori; }
}

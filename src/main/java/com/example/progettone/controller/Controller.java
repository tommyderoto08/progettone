package com.example.progettone.controller;

import com.example.progettone.model.Car;
import com.example.progettone.model.GaraManager;
import com.example.progettone.model.Scuderia;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.*;

import java.util.List;

public class Controller {

    // ── FXML ────────────────────────────────────────────────────────────────────
    @FXML private StackPane rootStack;      // contenitore principale
    @FXML private Pane      pistPane;       // area di gioco (pista)
    @FXML private VBox      classificaBox;  // lista posizioni laterale
    @FXML private Label     labelGiro;      // "Giro N / TOT"
    @FXML private Label     labelStato;     // "In gara", "Pausa", "Fine"
    @FXML private Button    btnStart;
    @FXML private Button    btnPausa;
    @FXML private Button    btnStop;
    @FXML private VBox      overlayScelta;  // overlay scelta scuderia (pre-gara)
    @FXML private VBox      overlayFine;    // overlay fine gara
    @FXML private VBox      risultatiFine;  // classifica finale nell'overlay

    // ── Stato ───────────────────────────────────────────────────────────────────
    private final GaraManager gara = new GaraManager();
    private boolean garaAvviata = false;
    private boolean garaPausa   = false;
    private Shape tracciato;;

    // ── Costanti layout ──────────────────────────────────────────────────────────
    private static final double PISTA_W      = 900;
    private static final double PISTA_H      = 580;
    private static final double GRIGLIA_X = 575;
    private static final double GRIGLIA_Y = 540;

    // ── Inizializzazione ────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Pulsanti disabilitati finché non si sceglie la scuderia
        btnStart.setDisable(true);
        btnPausa.setDisable(true);
        btnStop.setDisable(true);

        // Mostra overlay selezione scuderia
        mostraSceltaScuderia();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // OVERLAY — Selezione Scuderia
    // ════════════════════════════════════════════════════════════════════════════
    private void mostraSceltaScuderia() {
        overlayScelta.getChildren().clear();
        overlayScelta.setVisible(true);
        overlayScelta.setStyle(
            "-fx-background-color: rgba(10,10,20,0.93);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 30;"
        );

        Label titolo = new Label("🏎  SCEGLI LA TUA SCUDERIA");
        titolo.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
        titolo.setTextFill(Color.web("#E10600"));
        titolo.setPadding(new Insets(0, 0, 18, 0));

        Label sub = new Label("Gestirai entrambi i piloti di questa scuderia");
        sub.setFont(Font.font("Monospace", 12));
        sub.setTextFill(Color.web("#aaaaaa"));
        sub.setPadding(new Insets(0, 0, 20, 0));

        FlowPane bottoniere = new FlowPane();
        bottoniere.setHgap(10);
        bottoniere.setVgap(10);
        bottoniere.setAlignment(Pos.CENTER);
        bottoniere.setPrefWrapLength(560);

        for (Scuderia s : Scuderia.values()) {
            Button btn = creaBottoneScuderia(s);
            bottoniere.getChildren().add(btn);
        }

        overlayScelta.getChildren().addAll(titolo, sub, bottoniere);
        overlayScelta.setAlignment(Pos.CENTER);
    }

    private Button creaBottoneScuderia(Scuderia s) {
        // Badge colorato + nome
        Circle dot = new Circle(7, s.getColor());
        dot.setStroke(Color.WHITE);
        dot.setStrokeWidth(1);

        Label nome = new Label(s.getNome());
        nome.setFont(Font.font("Monospace", FontWeight.BOLD, 11));
        nome.setTextFill(Color.WHITE);

        HBox contenuto = new HBox(8, dot, nome);
        contenuto.setAlignment(Pos.CENTER_LEFT);

        Button btn = new Button();
        btn.setGraphic(contenuto);
        btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 14 8 14;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: rgba(225,6,0,0.25);" +
            "-fx-border-color: #E10600;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 14 8 14;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-border-color: rgba(255,255,255,0.25);" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 14 8 14;"
        ));
        btn.setOnAction(e -> scegliScuderia(s));
        return btn;
    }

    private void scegliScuderia(Scuderia s) {
        overlayScelta.setVisible(false);

        // Inizializza il manager
        gara.inizializza(s, 90.0);

        // Disegna il tracciato sullo sfondo della pista
        disegnaPista();

        // Aggiunge le auto alla pista nella posizione di griglia
        gara.disposiGriglia(GRIGLIA_X, GRIGLIA_Y);
        for (Car c : gara.getMacchine()) {
            if (!pistPane.getChildren().contains(c.getCar()))
                pistPane.getChildren().addAll(c.getCar(), c.getLabel());
        }

        // Evidenzia le auto del giocatore
        for (Car c : gara.getAutoGiocatore()) {
            c.getCar().setStroke(Color.GOLD);
            c.getCar().setStrokeWidth(2.5);
        }

        aggiornaClassifica();
        labelStato.setText("Pronto — clicca START");
        labelGiro.setText("Giro 1 / " + GaraManager.GIRI_TOTALI);
        btnStart.setDisable(false);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // PISTA
    // ════════════════════════════════════════════════════════════════════════════
    private void disegnaPista() {
        // Tracciato visivo (solo bordo, non usato per animazione)
        tracciato = gara.getCircuito().percorsoBahrain();
        tracciato.setStroke(Color.web("#444444"));
        tracciato.setStrokeWidth(22);
        tracciato.setFill(null);

        Shape bordo = gara.getCircuito().percorsoBahrain();
        bordo.setStroke(Color.web("#CCCCCC"));
        bordo.setStrokeWidth(24);
        bordo.setFill(null);
        bordo.setOpacity(0.15);

        // Linea del traguardo
        javafx.scene.shape.Line traguardo = new javafx.scene.shape.Line(
                590, 520, 590, 560);
        traguardo.setStroke(Color.WHITE);
        traguardo.setStrokeWidth(3);
        traguardo.getStrokeDashArray().addAll(4.0, 4.0);

        Label lblT = new Label("START/FINISH");
        lblT.setFont(Font.font("Monospace", 8));
        lblT.setTextFill(Color.WHITE);
        lblT.setLayoutX(563);
        lblT.setLayoutY(534);

        pistPane.getChildren().addAll(0, List.of(bordo, tracciato, traguardo, lblT));
    }

    // ════════════════════════════════════════════════════════════════════════════
    // AZIONI PULSANTI
    // ════════════════════════════════════════════════════════════════════════════
    @FXML
    private void onStart() {
        if (garaAvviata) return;
        garaAvviata = true;
        garaPausa   = false;
        labelStato.setText("🏁  IN GARA");
        btnStart.setDisable(true);
        btnPausa.setDisable(false);
        btnStop.setDisable(false);

        // Rimuove bordo gold dalla griglia (ora parte la transizione)
        for (Car c : gara.getAutoGiocatore()) {
            c.getCar().setStroke(null);
        }

        gara.avviaGara(this::aggiornamentoFrame, this::fineGara);
    }

    @FXML
    private void onPausa() {
        if (!garaAvviata) return;
        if (!garaPausa) {
            gara.pausaGara();
            garaPausa = true;
            labelStato.setText("⏸  PAUSA");
            btnPausa.setText("▶ Riprendi");
        } else {
            gara.riprendi();
            garaPausa = false;
            labelStato.setText("🏁  IN GARA");
            btnPausa.setText("⏸ Pausa");
        }
    }

    @FXML
    private void onStop() {
        gara.stopGara();
        garaAvviata = false;
        garaPausa   = false;
        labelStato.setText("⛔  FERMATA");
        btnStart.setDisable(false);
        btnPausa.setDisable(true);
        btnStop.setDisable(true);
        btnPausa.setText("⏸ Pausa");
    }

    // ════════════════════════════════════════════════════════════════════════════
    // AGGIORNAMENTO UI
    // ════════════════════════════════════════════════════════════════════════════
    private void aggiornamentoFrame() {
        Platform.runLater(() -> {
            // Aggiorna etichetta giro dal leader
            List<Car> m = gara.getMacchine();
            if (!m.isEmpty()) {
                int giroCorrente = m.get(0).getGiriCompletati() + 1;
                labelGiro.setText("Giro " + Math.min(giroCorrente, GaraManager.GIRI_TOTALI)
                        + " / " + GaraManager.GIRI_TOTALI);
            }
            // Sincronizza posizione label sui cerchi
            for (Car c : gara.getMacchine()) {
                c.getLabel().setTranslateX(c.getCar().getTranslateX() +
                        c.getCar().getCenterX() - 5);
                c.getLabel().setTranslateY(c.getCar().getTranslateY() +
                        c.getCar().getCenterY() + 4);
            }
            aggiornaClassifica();
        });
    }

    private void aggiornaClassifica() {
        classificaBox.getChildren().clear();

        List<Car> ordinate = gara.getMacchine();
        for (int i = 0; i < ordinate.size(); i++) {
            Car c = ordinate.get(i);
            boolean isPlayer = c.getScuderia() == gara.getScuderiaGiocatore();

            HBox riga = new HBox(6);
            riga.setAlignment(Pos.CENTER_LEFT);
            riga.setPadding(new Insets(2, 6, 2, 6));
            if (isPlayer)
                riga.setStyle("-fx-background-color: rgba(255,200,0,0.15); -fx-background-radius:4;");

            Label pos = new Label(String.format("P%-2d", i + 1));
            pos.setFont(Font.font("Monospace", FontWeight.BOLD, 11));
            pos.setTextFill(i == 0 ? Color.GOLD : Color.web("#cccccc"));
            pos.setPrefWidth(32);

            Circle dot = new Circle(5, c.getScuderia().getColor());
            dot.setStroke(Color.web("#555555"));

            Label pilota = new Label(c.getPilota().split(" ")[1]); // solo cognome
            pilota.setFont(Font.font("Monospace", isPlayer ? FontWeight.BOLD : FontWeight.NORMAL, 11));
            pilota.setTextFill(isPlayer ? Color.GOLD : Color.WHITE);

            Label giro = new Label("G" + (c.getGiriCompletati() + 1));
            giro.setFont(Font.font("Monospace", 10));
            giro.setTextFill(Color.web("#888888"));
            HBox.setHgrow(pilota, Priority.ALWAYS);

            riga.getChildren().addAll(pos, dot, pilota, giro);
            classificaBox.getChildren().add(riga);
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // FINE GARA
    // ════════════════════════════════════════════════════════════════════════════
    private void fineGara() {
        Platform.runLater(() -> {
            garaAvviata = false;
            labelStato.setText("🏆  FINE GARA");
            btnPausa.setDisable(true);
            btnStop.setDisable(true);
            mostraOverlayFine();
        });
    }

    private void mostraOverlayFine() {
        risultatiFine.getChildren().clear();

        Label titolo = new Label("🏆  CLASSIFICA FINALE");
        titolo.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
        titolo.setTextFill(Color.web("#E10600"));
        titolo.setPadding(new Insets(0, 0, 16, 0));
        risultatiFine.getChildren().add(titolo);

        // Punti F1 ufficiali
        int[] punti = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};
        List<Car> ordinate = gara.getMacchine();
        int punteggioGiocatore = 0;

        for (int i = 0; i < ordinate.size(); i++) {
            Car c = ordinate.get(i);
            int pt = i < punti.length ? punti[i] : 0;
            boolean isPlayer = c.getScuderia() == gara.getScuderiaGiocatore();
            if (isPlayer) punteggioGiocatore += pt;

            HBox riga = new HBox(10);
            riga.setAlignment(Pos.CENTER_LEFT);
            riga.setPadding(new Insets(3));
            if (isPlayer)
                riga.setStyle("-fx-background-color:rgba(255,200,0,0.2);-fx-background-radius:4;");

            String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : String.format("P%-2d", i+1);
            Label pos    = new Label(medal);
            pos.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
            pos.setTextFill(Color.WHITE);
            pos.setPrefWidth(36);

            Circle dot = new Circle(6, c.getScuderia().getColor());

            Label nome = new Label(c.getPilota());
            nome.setFont(Font.font("Monospace", isPlayer ? FontWeight.BOLD : FontWeight.NORMAL, 11));
            nome.setTextFill(isPlayer ? Color.GOLD : Color.WHITE);
            HBox.setHgrow(nome, Priority.ALWAYS);

            Label ptLbl = new Label(pt + " pt");
            ptLbl.setFont(Font.font("Monospace", FontWeight.BOLD, 11));
            ptLbl.setTextFill(pt > 0 ? Color.web("#44ff88") : Color.web("#666666"));

            riga.getChildren().addAll(pos, dot, nome, ptLbl);
            risultatiFine.getChildren().add(riga);
        }

        // Riepilogo giocatore
        Label riepilogo = new Label("La tua scuderia ha totalizzato " + punteggioGiocatore + " punti!");
        riepilogo.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        riepilogo.setTextFill(Color.GOLD);
        riepilogo.setPadding(new Insets(16, 0, 8, 0));

        Button btnRigioca = new Button("🔄  Nuova Gara");
        btnRigioca.setStyle(
            "-fx-background-color: #E10600;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: Monospace;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 24 10 24;" +
            "-fx-cursor: hand;"
        );
        btnRigioca.setOnAction(e -> nuovaGara());

        risultatiFine.getChildren().addAll(riepilogo, btnRigioca);
        overlayFine.setVisible(true);
    }

    private void nuovaGara() {
        overlayFine.setVisible(false);
        pistPane.getChildren().clear();
        gara.stopGara();
        garaAvviata = false;
        garaPausa = false;
        btnStart.setDisable(true);
        btnPausa.setDisable(true);
        btnStop.setDisable(true);
        btnPausa.setText("⏸ Pausa");
        labelStato.setText("—");
        labelGiro.setText("—");
        classificaBox.getChildren().clear();
        mostraSceltaScuderia();
    }
}

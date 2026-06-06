package com.example.progettone.model;

import javafx.scene.paint.Color;

public enum Scuderia {
    FERRARI     (Color.RED,           "Ferrari"),
    REDBULL     (Color.DARKBLUE,      "Red Bull Racing"),
    MCLAREN     (Color.ORANGE,        "McLaren"),
    MERCEDES    (Color.CYAN,          "Mercedes-AMG"),
    ASTONMARTIN (Color.DARKGREEN,     "Aston Martin"),
    ALPINE      (Color.DEEPSKYBLUE,   "Alpine"),
    HAAS        (Color.WHITE,         "Haas Ferrari"),
    CADILLAC    (Color.GRAY,          "Cadillac"),
    AUDI        (Color.ORANGERED,     "Audi"),
    WILLIAMS    (Color.BLUE,          "Williams"),
    RACINGBULLS (Color.DARKSLATEBLUE, "Racing Bulls");

    private final Color  color;
    private final String nome;

    Scuderia(Color color, String nome) {
        this.color = color;
        this.nome  = nome;
    }

    public Color  getColor() { return color; }
    public String getNome()  { return nome;  }
}

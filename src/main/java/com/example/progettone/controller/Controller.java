package com.example.progettone.controller;

import com.example.progettone.model.Car;
import com.example.progettone.model.Circuito;
import com.example.progettone.model.Scuderia;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Controller {
    @FXML
    private Pane pane;

    public void initialize(){
        Car f1 = new Car("Lewis Hamilton", Scuderia.FERRARI, new Circuito().percorsoBahrain(), 90.00);
        Car f2 = new Car("Charles Leclerc", Scuderia.FERRARI, new Circuito().percorsoBahrain(), 90.00);
        Car rb1 = new Car("Max Verstappen", Scuderia.REDBULL, new Circuito().percorsoBahrain(), 90.00);
        Car rb2 = new Car("Isaac Hadjar", Scuderia.REDBULL, new Circuito().percorsoBahrain(), 90.00);
        Car m1 = new Car("Kimi Antonelli", Scuderia.MERCEDES, new Circuito().percorsoBahrain(), 90.00);
        Car m2 = new Car("George Russel", Scuderia.MERCEDES, new Circuito().percorsoBahrain(), 90.00);
        Car mc1 = new Car("Lando Norris", Scuderia.MCLAREN, new Circuito().percorsoBahrain(), 90.00);
        Car mc2 = new Car("Oscar Piastri", Scuderia.MCLAREN, new Circuito().percorsoBahrain(), 90.00);
        Car h1 = new Car("Oliver Bearman", Scuderia.HAAS, new Circuito().percorsoBahrain(), 90.00);
        Car h2 = new Car("Esteban Ocon", Scuderia.HAAS, new Circuito().percorsoBahrain(), 90.00);
        Car as1 = new Car("Fernando Alonso", Scuderia.ASTONMARTIN, new Circuito().percorsoBahrain(), 90.00);
        Car as2 = new Car("Lance Stroll", Scuderia.ASTONMARTIN, new Circuito().percorsoBahrain(), 90.00);
        Car a1 = new Car("Pierre Gasly", Scuderia.ALPINE, new Circuito().percorsoBahrain(), 90.00);
        Car a2 = new Car("Franco Colapinto", Scuderia.ALPINE, new Circuito().percorsoBahrain(), 90.00);
        Car r1 = new Car("Liam Lawson", Scuderia.RACINGBULLS, new Circuito().percorsoBahrain(), 90.00);
        Car r2 = new Car("Arvid Lindblad ", Scuderia.RACINGBULLS, new Circuito().percorsoBahrain(), 90.00);
        Car au1 = new Car("Gabriel Bortoleto", Scuderia.AUDI, new Circuito().percorsoBahrain(), 90.00);
        Car au2 = new Car("Nico Hulkenberg", Scuderia.AUDI, new Circuito().percorsoBahrain(), 90.00);
        Car w1 = new Car("Carlos Sainz", Scuderia.WILLIAMS, new Circuito().percorsoBahrain(), 90.00);
        Car w2 = new Car("Alex Albon", Scuderia.WILLIAMS, new Circuito().percorsoBahrain(), 90.00);
        Car c1 = new Car("Valteri Bottas", Scuderia.CADILLAC, new Circuito().percorsoBahrain(), 90.00);
        Car c2 = new Car("Sergio Perez", Scuderia.CADILLAC, new Circuito().percorsoBahrain(), 90.00);

        Image i = new Image(getClass().getResource("/com/example/progettone/image/Screenshot (1).png").toExternalForm());

        pane.setBackground(new Background(new BackgroundImage(i, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false))));
    }
}
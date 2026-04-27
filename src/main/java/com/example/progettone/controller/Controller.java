package com.example.progettone.controller;

import com.example.progettone.model.Car;
import com.example.progettone.model.Scuderia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    private Button newGame;

    public void initialize(){
        Car f1 = new Car("Lewis Hamilton", Scuderia.FERRARI);
        Car f2 = new Car("Charles Leclerc", Scuderia.FERRARI);
        Car rb1 = new Car("Max Verstappen", Scuderia.REDBULL);
        Car rb2 = new Car("Isaac Hadjar", Scuderia.REDBULL);
        Car m1 = new Car("Kimi Antonelli", Scuderia.MERCEDES);
        Car m2 = new Car("George Russel", Scuderia.MERCEDES);
        Car mc1 = new Car("Lando Norris", Scuderia.MCLAREN);
        Car mc2 = new Car("Oscar Piastri", Scuderia.MCLAREN);
        Car h1 = new Car("Oliver Bearman", Scuderia.HAAS);
        Car h2 = new Car("Esteban Ocon", Scuderia.HAAS);
        Car as1 = new Car("Fernando Alonso", Scuderia.ASTONMARTIN);
        Car as2 = new Car("Lance Stroll", Scuderia.ASTONMARTIN);
        Car a1 = new Car("Pierre Gasly", Scuderia.ALPINE);
        Car a2 = new Car("Franco Colapinto", Scuderia.ALPINE);
        Car r1 = new Car("Liam Lawson", Scuderia.RACINGBULLS);
        Car r2 = new Car("Arvid Lindblad ", Scuderia.RACINGBULLS);
        Car au1 = new Car("Gabriel Bortoleto", Scuderia.AUDI);
        Car au2 = new Car("Nico Hulkenberg", Scuderia.AUDI);
        Car w1 = new Car("Carlos Sainz", Scuderia.WILLIAMS);
        Car w2 = new Car("Alex Albon", Scuderia.WILLIAMS);
        Car c1 = new Car("Valteri Bottas", Scuderia.CADILLAC);
        Car c2 = new Car("Sergio Perez", Scuderia.CADILLAC);


    }
}
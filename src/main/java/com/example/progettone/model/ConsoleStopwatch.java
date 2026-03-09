package com.example.progettone.model;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleStopwatch {

    private volatile boolean running = false;
    private long elapsedNanos = 0;       // tempo accumulato quando non è running
    private long startNanoTime = 0;      // valore di System.nanoTime() all'ultimo start

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "stopwatch-updater");
                t.setDaemon(true);
                return t;
            });

    private void run() {
        // aggiorna la visuale ogni 100 ms
        scheduler.scheduleAtFixedRate(this::printTime, 0, 100, TimeUnit.MILLISECONDS);

        // thread che legge comandi da terminale (blocca su nextLine)
        Thread inputThread = new Thread(this::readCommands, "console-input");
        inputThread.setDaemon(false);
        inputThread.start();

        // aspetta che il thread input termini (quando l'utente esce)
        try {
            inputThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            shutdown();
        }
    }

    private void readCommands() {
        Scanner sc = new Scanner(System.in);
        printHelp();
        while (true) {
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim().toLowerCase();
            if (line.isEmpty()) {
                toggle(); // ENTER senza testo = toggle start/stop
            } else switch (line) {
                case "start":
                    start();
                    break;
                case "stop":
                    stop();
                    break;
                case "reset":
                    reset();
                    break;
                case "toggle":
                    toggle();
                    break;
                case "quit":
                case "q":
                case "exit":
                    println("\nUscita.");
                    return;
                case "help":
                    printHelp();
                    break;
                default:
                    println("Comando non riconosciuto. Digita 'help' per i comandi.");
            }
        }
    }

    private synchronized void start() {
        if (running) return;
        startNanoTime = System.nanoTime();
        running = true;
        println("Start");
    }

    private synchronized void stop() {
        if (!running) return;
        long now = System.nanoTime();
        elapsedNanos += now - startNanoTime;
        running = false;
        println("Stop");
    }

    private synchronized void reset() {
        running = false;
        elapsedNanos = 0;
        println("Reset to 00:00.000");
    }

    private synchronized void toggle() {
        if (running) stop(); else start();
    }

    private void printTime() {
        long totalNanos;
        synchronized (this) {
            totalNanos = elapsedNanos + (running ? (System.nanoTime() - startNanoTime) : 0);
        }
        String s = formatNanos(totalNanos);
        // stampa sullo stesso rigo (carriage return) e svuota il buffer
        System.out.print("\r" + s + "    "); // spazi per sovrascrivere testi più lunghi
        System.out.flush();
    }

    private static String formatNanos(long nanos) {
        long totalMillis = nanos / 1_000_000;
        long millis = totalMillis % 1000;
        long totalSeconds = totalMillis / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format("%d:%02d:%02d.%03d", hours, minutes % 60, seconds, millis);
        } else {
            return String.format("%02d:%02d.%03d", minutes, seconds, millis);
        }
    }

    private void shutdown() {
        scheduler.shutdownNow();
        // assicura che l'ultimo valore sia su linea nuova
        System.out.println();
    }

    // helper per scrivere messaggi su riga nuova (senza rompere la stampa del timer)
    private static void println(String msg) {
        System.out.print("\r"); // torna all'inizio della riga
        System.out.println(msg);
    }

    private static void printHelp() {
        println("Console Stopwatch");
        println("Comandi:");
        println("  start   - avvia");
        println("  stop    - ferma");
        println("  reset   - azzera");
        println("  toggle  - alterna start/stop");
        println("  (ENTER) - senza testo alterna start/stop");
        println("  quit/q  - esci");
        println("  help    - mostra questa guida");
        println("");
        println("Il tempo viene mostrato e aggiornato ogni 100 ms.");
    }
}
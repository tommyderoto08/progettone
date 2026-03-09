package com.example.progettone.model;

import java.util.Scanner;

public class ConsoleStopwatchNoPrint {

    private volatile boolean running = false;
    private long elapsedNanos = 0;    // tempo accumulato quando non è running
    private long startNanoTime = 0;   // System.nanoTime() all'ultimo start

    public static void main(String[] args) {
        new ConsoleStopwatchNoPrint().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        printHelp();
        while (true) {
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim().toLowerCase();
            if (line.isEmpty()) {
                toggle(); // ENTER = toggle start/stop
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
                case "time":
                    // qui stampiamo il tempo solo su richiesta
                    System.out.println("Tempo: " + formatCurrent());
                    break;
                case "quit":
                case "q":
                case "exit":
                    System.out.println("Uscita.");
                    return;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println("Comando non riconosciuto. Digita 'help' per i comandi.");
            }
        }
    }

    private synchronized void start() {
        if (running) return;
        startNanoTime = System.nanoTime();
        running = true;
        System.out.println("Start");
    }

    private synchronized void stop() {
        if (!running) return;
        long now = System.nanoTime();
        elapsedNanos += now - startNanoTime;
        running = false;
        System.out.println("Stop");
    }

    private synchronized void reset() {
        running = false;
        elapsedNanos = 0;
        System.out.println("Reset to 00:00.000");
    }

    private synchronized void toggle() {
        if (running) stop(); else start();
    }

    // calcola il tempo corrente senza modificare lo stato
    private synchronized String formatCurrent() {
        long totalNanos = elapsedNanos + (running ? (System.nanoTime() - startNanoTime) : 0);
        return formatNanos(totalNanos);
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

    private static void printHelp() {
        System.out.println("Console Stopwatch (silent)");
        System.out.println("Comandi:");
        System.out.println("  start   - avvia");
        System.out.println("  stop    - ferma");
        System.out.println("  reset   - azzera");
        System.out.println("  toggle  - alterna start/stop");
        System.out.println("  (ENTER) - senza testo alterna start/stop");
        System.out.println("  time    - mostra il tempo attuale (stampato una tantum)");
        System.out.println("  quit/q  - esci");
        System.out.println("  help    - mostra questa guida");
    }
}
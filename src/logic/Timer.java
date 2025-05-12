package logic;

import java.awt.Label;

import ui.DialogManager;
import ui.Game;

public class Timer extends Thread {

    private int min;
    private int sec;
    private Label time;
    private boolean work = false;

    public Timer (Label label, int minute, int second) {
        this.min = minute;
        this.sec = second;
        this.time = label;
        start();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                synchronized (this) {
                    while(!work) {
                        wait();
                    }
                }

                sec--;

                if (min == 0 && sec == 0) {
                    time.setText("00:00");
                    DialogManager.showWinDialog((Game)Game.getTerrain().getParent(), Game.getTerrain(), false);
                    stopTime(); break;
                }

                if (sec < 0) {
                    if (min > 0) min--;
                    sec = 59;
                }

                time.setText(String.format("%02d:%02d", min, sec));

                sleep(1000);
            }
        } catch (InterruptedException e) {}

    }

    public synchronized void startTime() {
        work = true;
        notify();
    }

    public synchronized void resetTime(int minutes) {
        min = minutes;
        sec = 0;
    }

    public synchronized void pauseTime() {
        work = false;
    }

    public synchronized void stopTime() {
        interrupt();
    }

}

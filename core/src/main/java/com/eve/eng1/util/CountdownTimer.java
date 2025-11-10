package com.eve.eng1.util;

public class CountdownTimer {
    private float timeRemaining;
    private boolean running;

    public CountdownTimer(float startTime) {
        this.timeRemaining = startTime;
        this.running = false;
    }
    public void start() {
        running = true;
    }

    public void stop(){
        running = false;
    }

    public void reset(float newTime){
        timeRemaining = newTime;
        running = false;
    }

    public void update(float delta){
        if (!running) return;
        timeRemaining -= delta;
        if (timeRemaining < 0) timeRemaining = 0;
    }

    public int getSeconds(){
        return (int) Math.ceil(timeRemaining);
    }

    public boolean isFinished(){
        return timeRemaining  <= 0;
    }
}

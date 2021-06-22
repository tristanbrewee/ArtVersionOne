package com.tristanbrewee.model;

import javafx.scene.paint.Color;

public class ToneHeight {

    private int tone;
    private Color color;
    private CurrentLocation currentLocation;

    public ToneHeight(int tone, Color color, CurrentLocation currentLocation) {
        this.tone = tone;
        this.color = color;
        this.currentLocation = currentLocation;
    }

    public void setTone(int tone) {
        this.tone = tone;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getTone() {
        return tone;
    }

    public Color getColor() {
        return color;
    }

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }
}

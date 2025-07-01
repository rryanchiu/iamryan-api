package me.rryan.tinyurl.model;

import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class RgbColor {
    private int red;
    private int green;
    private int blue;

    public RgbColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String toHex() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }


    @Override
    public String toString() {
        return toHex();
    }

    // Getters
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RgbColor)) return false;
        RgbColor c = (RgbColor) o;
        return red == c.red && green == c.green && blue == c.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}
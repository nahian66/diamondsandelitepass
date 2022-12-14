package com.rowdystudio.fffdiamondsandelitespass.models;

public class Redeem {
    private String numberOrUpi;
    private String points;
    private String type;

    public Redeem() {
    }

    public Redeem(String numberOrUpi, String points,String type) {
        this.numberOrUpi = numberOrUpi;
        this.points = points;
        this.type=type;
    }

    public String getNumberOrUpi() {
        return numberOrUpi;
    }

    public void setNumberOrUpi(String numberOrUpi) {
        this.numberOrUpi = numberOrUpi;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.evgeniy.recipesapp.pojo;

import java.util.ArrayList;

public class WinePairing {
    ArrayList<Object> pairedWines = new ArrayList<Object>();
    private String pairingText;
    ArrayList<Object> productMatches = new ArrayList<Object>();


    // Getter Methods

    public String getPairingText() {
        return pairingText;
    }

    // Setter Methods

    public void setPairingText(String pairingText) {
        this.pairingText = pairingText;
    }
}

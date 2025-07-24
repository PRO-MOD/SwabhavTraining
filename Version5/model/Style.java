package com.aurionpro.OOAD.Guitar.Version5.model;

public enum Style {
    A, F;

    @Override
    public String toString() {
        switch (this) {
            case A: return "A-style";
            case F: return "F-style";
            default: return "Unspecified";
        }
    }
}


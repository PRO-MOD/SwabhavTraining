package com.aurionpro.OOAD.Guitar.Version5.model;

public class InstrumentSpec {
    private Builder builder;
    private String model;
    private Type type;
    private Wood backWood;
    private Wood topWood;

    public InstrumentSpec(Builder builder, String model, Type type,
                          Wood backWood, Wood topWood) {
        this.builder = builder;
        this.model = model;
        this.type = type;
        this.backWood = backWood;
        this.topWood = topWood;
    }

    public Builder getBuilder() { return builder; }
    public String getModel() { return model; }
    public Type getType() { return type; }
    public Wood getBackWood() { return backWood; }
    public Wood getTopWood() { return topWood; }

    public boolean matches(InstrumentSpec otherSpec) {
        if (otherSpec.getBuilder() != null && builder != otherSpec.getBuilder())
            return false;

        if (otherSpec.getModel() != null && !otherSpec.getModel().isEmpty()) {
            if (model == null || !model.toLowerCase().contains(otherSpec.getModel().toLowerCase()))
                return false;
        }

        if (otherSpec.getType() != null && type != otherSpec.getType())
            return false;

        if (otherSpec.getBackWood() != null && backWood != otherSpec.getBackWood())
            return false;

        if (otherSpec.getTopWood() != null && topWood != otherSpec.getTopWood())
            return false;

        return true;
    }



}

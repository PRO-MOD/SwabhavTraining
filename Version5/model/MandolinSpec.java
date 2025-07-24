package com.aurionpro.OOAD.Guitar.Version5.model;

public class MandolinSpec extends InstrumentSpec{
	
	private Style style;

	public MandolinSpec(Builder builder, String model, Type type, Wood backWood, Wood topWood, Style style) {
		super(builder, model, type, backWood, topWood);
		this.style = style;
	}

	public Style getStyle() { 
		
		return style; 
		
	}
	
	@Override
	public boolean matches(InstrumentSpec otherSpec) {
	    if (!super.matches(otherSpec)) return false;
	    if (!(otherSpec instanceof MandolinSpec)) return true;

	    MandolinSpec mandolinSpec = (MandolinSpec) otherSpec;
	    return this.getStyle() == null || this.getStyle() == mandolinSpec.getStyle();
	}

}

package entiies;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1,0,0); // Grad av oppløsning/nedbrytning av lyset i forhold til avstand
	
	// Farger
	public static final Vector3f HVIT = new Vector3f(1, 1, 1);
	public static final Vector3f ROD = new Vector3f(1, 0, 0);
	public static final Vector3f GRONN = new Vector3f(0, 1, 0);
	public static final Vector3f BLAA = new Vector3f(0, 0, 1);
	public static final Vector3f GUL = new Vector3f(1, 1, 0);
	public static final Vector3f ROSA = new Vector3f(1, 0, 1);
	public static final Vector3f TURKIS = new Vector3f(0, 1, 1);
	public static final Vector3f ORANGE = new Vector3f(1, 0.6f, 0);
	
	
	// Styrker
	public static final Vector3f SVAK = new Vector3f(2f, 0.01f, 0.002f);
	public static final Vector3f MIDDELS = new Vector3f(1f, 0.01f, 0.002f);
	public static final Vector3f STERK = new Vector3f(0.2f, 0.005f, 0.001f);

	
	
	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}
	

	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		super();
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}


	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColour() {
		return colour;
	}
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}
	
	
	
	
}

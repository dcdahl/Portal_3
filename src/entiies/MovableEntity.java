package entiies;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/*
 * 
 * Du kan se bort i fra denne klassen, da det var et fors�k p� � implementere bevegelse for objekter basert p� tid og ikke frames.
 * Planen var � sp�rre deg om dette p� onsdag den 27. sept, men du ble syk, s� da var jeg stuck.. :(
 * 
 * 
 * 

 * 
 * */

public class MovableEntity extends Entity {

	private float xSpeed = 0;
	private float ySpeed = 0;
	private float zSpeed = 0;
	private float rotateLeftSpeed = 0;
	private float rotateRightSpeed = 0;

	public MovableEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			float xSpeed, float ySpeed, float zSpeed) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;

	}

	public void move() {

	}

	public void stop() {
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.zSpeed = 0;
	}

	public float getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public float getySpeed() {
		return ySpeed;
	}

	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public float getzSpeed() {
		return zSpeed;
	}

	public void setzSpeed(float zSpeed) {
		this.zSpeed = zSpeed;
	}

}

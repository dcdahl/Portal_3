package entiies;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import render.DisplayManager;

public class Player extends Entity{
	
	
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}

	

	public void move(){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0 );
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		
		// Jump
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		if(super.getPosition().y<TERRAIN_HEIGHT){
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = TERRAIN_HEIGHT;
		}
		//System.out.println(super.getPosition());
	}
	
	private void jump(){
		if(!isInAir){
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
			
		
	}
	
	
	
	
	// Spiller bevegelse og keyboard input
	private void checkInputs() {
		
		// Frem og tilbake
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) 
			this.currentSpeed = RUN_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			this.currentSpeed = -RUN_SPEED;
		else
			this.currentSpeed = 0;
			
		// Høyre og venstre
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			this.currentTurnSpeed = -TURN_SPEED;
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;
		
		// JUMP
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
		
		
	}
	
	
	
	
	
}

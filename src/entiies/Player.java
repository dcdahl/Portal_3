package entiies;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import animation.Animation;
import animation.Animator;
import models.AnimatedModel;
import models.BoundingBox;
import render.DisplayManager;
import render.MasterRenderer;
import terrains.Terrain;

public class Player extends AnimatedEntity{
	
	
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	private BoundingBox boundingBox;
	
	
	Animation animation;
	Animator animator;
	
	
	
	public Player(AnimatedModel model, Animator animator, Animation animation, Vector3f position, float rotX, float rotY, float rotZ, float scale,BoundingBox boundingBox) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.animator = animator;
		this.animation = animation;
		this.boundingBox = boundingBox;
	}
	public Player(AnimatedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	

	public void move(Terrain terrain){
		checkInputs();
		
		boundingBox.setCenterPosition(super.getPosition());
		
		// Finner rotasjonen som er foretatt. 
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0 );
		
		// Finner hastigheten i Y-aksen om vi hopper. Om vi ikke hopper blir upwardsspeed = 0;
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		
		// Distansen vi skal bevege oss
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		// Regner ut distansen i de forskjellige aksene
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		float dy = upwardsSpeed * DisplayManager.getFrameTimeSeconds();
		
		// Finner terrenghøyden til popsisjonen vi er i nå. 
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		// Lager en ny vektor av bevegelsesdistansene. 
		Vector3f movingDistance = new Vector3f(dx, dy, dz);
		
		// Legger de til den nåværende posisjonen for å finne punktet vi ønsker å gå til.
		Vector3f newPosition = Vector3f.add(this.getPosition(), movingDistance, null);
		boundingBox.setCenterPosition(newPosition);
		
		// Sjekker om vi krysser med noen av objektene i verden
		BoundingBox intersectingBox = null;
		boolean isIntersecting = false;
		for (BoundingBox box : BoundingBox.getAABBList()) {
			if(this.boundingBox.intersects(box)){
				isIntersecting = true;
				intersectingBox = box;
			}
		}
			
		
						
		// Hvis vi ikke kommer til å krysse noe
		if(!isIntersecting){
			super.increasePosition(dx, dy, dz);
			
			if(super.getPosition().y < terrainHeight){
				upwardsSpeed = 0;
				isInAir = false;
				super.getPosition().y = terrainHeight;
			}
		
		// Hvis vi kommer til å krysse ovenfra etter et hopp (er i lufta)
		}else if(newPosition.y < intersectingBox.getMax().y && this.getPosition().y > intersectingBox.getMax().y){

				super.increasePosition(dx, 0, dz);
				super.getPosition().y = intersectingBox.getMax().y; // Flytt spillern til toppen av boksen, så den ikke krysser med er akkurat på toppen.
				isInAir = false;
				upwardsSpeed = 0;
		
		// Hvis vi kommer til å krysse og er i lufta, men ikke er over objektet, da er vi på siden.
		}else if(isInAir){
			 if(super.getPosition().y < terrainHeight){
					
					upwardsSpeed = 0;
					isInAir = false;
					super.getPosition().y = terrainHeight;
			}
			super.increasePosition(0, dy, 0);
		
		// Hvis vi kommer til å krysse, men kommer ikke oppå objektet
		}else if(this.getPosition().y < intersectingBox.getMax().y){
			// Gjør ingenting
			
		// Ellers er vi oppå objektet, og skal kun bevege oss i z og x aksen for ikke å krysse med objektet. 
		// Dette hindrer spillern fra å falle mot bakken og vi kan bevege oss på objektet.
		}else{
			super.increasePosition(dx, 0, dz);
		}

	}
	
	
	
	
	
	
	private void jump(){
		if(!isInAir){
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	
	
	
	// Spillerbevegelse og keyboard input
	private void checkInputs() {
		
		// Frem og tilbake
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
			this.currentSpeed = RUN_SPEED * 2;
		else if(Keyboard.isKeyDown(Keyboard.KEY_W)) 
			this.currentSpeed = RUN_SPEED;
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			this.currentSpeed = -RUN_SPEED;
		else
			this.currentSpeed = 0;
			
		// Hï¿½yre og venstre
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			this.currentTurnSpeed = -TURN_SPEED;
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;
		
		// Hopp
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
			MasterRenderer.setPolygonMode(true);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_C))
			MasterRenderer.setPolygonMode(false);
		
		
	}
	
	
	public Animator getAnimator() {
		return animator;
	}
	
	public boolean isMoving(){
		return currentSpeed > 0 || currentTurnSpeed > 0;
	}
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
}

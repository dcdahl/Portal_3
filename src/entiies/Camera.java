package entiies;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private static final int MOUSE_BUTTON_LEFT = 0;
	private static final int MOUSE_BUTTON_RIGHT = 1;
	private static final int MOUSE_BUTTON_MIDDLE = 2;
	private static final float MOUSE_SENSETIVITY = 0.2f;
	private static final float MOUSE_SCROLL_SENSETIVITY = 0.01f;
	private static final int TERRAIN_HEIGHT = 0;
	private static final int DISTANCE_TO_PLAYER_MINIMUM = 2;
	
	private float distanceFromPlayer = 10;
	private float angleAroundPlayer = 0;
	
	
	private Vector3f position = new Vector3f(0,5,0);
	private float pitch = 10, yaw = 0, roll; 

	private Player player;
	private Matrix4f rotationMatrix = new Matrix4f();
	
	public Camera(Player player){
		this.player = player;
		
	}
	
	public Camera() {}
	
	public void invertPitch() {
		this.pitch  = -pitch;
	}
	
	public void move(){
		calculateZoom();
		//calculatePitch();
		//calculateAngleAroundPlayer();
		checkCameraReset();
		
		calculatePitchAndAngleAroundPlayer();
		
		
		float horizontalDistance = calculateHorizontalDistance();
		float vertivalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance,vertivalDistance);
		
		
		
		this.yaw = 180 - (player.getRotX() + angleAroundPlayer);
		rotationMatrix = Matrix4f.rotate(angleAroundPlayer, new Vector3f(0,roll,0), rotationMatrix, null);
	}
	
	public Matrix4f getRotationMatrix()
	{
		return rotationMatrix;	
	}
	


	private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
		
		
		
		float theta = player.getRotX() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		
		
		//position.y = player.getPosition().y + verticalDistance;
		position.y = getFaceView() + verticalDistance;
		

		
	}

	
	
	private float calculateHorizontalDistance(){
		float horDis = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		if(horDis < 0)
			horDis = 0;
		
		return horDis;
	}
	
	private float calculateVerticalDistance(){
		float vertDis = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		if(vertDis < 0)
			vertDis = 0;
		return vertDis;
	}
	
	

	
	
	
	private void calculatePitchAndAngleAroundPlayer(){
		if(Mouse.isButtonDown(2)){
			float pitchChange = Mouse.getDY() * MOUSE_SENSETIVITY;
			float angleChange = Mouse.getDX() * MOUSE_SENSETIVITY;	
		
			pitch -= pitchChange;
			angleAroundPlayer -= angleChange;
		
				
			if(pitch < 0)
				pitch = 0;
			else if(pitch > 90)
				pitch = 90;
			
		}
		
	}
	
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * MOUSE_SCROLL_SENSETIVITY;
		float zoom = distanceFromPlayer -= zoomLevel;
		if(zoom < DISTANCE_TO_PLAYER_MINIMUM)
			distanceFromPlayer = DISTANCE_TO_PLAYER_MINIMUM;
		else if(zoom > 50)
			distanceFromPlayer = 50;
		else
			distanceFromPlayer -= zoomLevel;
	}
		
	private void resetCameraToDefaultSettings(){
		distanceFromPlayer = 50;
		angleAroundPlayer = 0;
		pitch = 10;
		yaw = 0;
		
	}
	
	
	
	private void checkCameraReset() {
		if(Mouse.isButtonDown(MOUSE_BUTTON_RIGHT))
			resetCameraToDefaultSettings();
	}
	
	/*

	
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(MOUSE_BUTTON_RIGHT)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
		private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(MOUSE_BUTTON_LEFT)){
			float angleChange = Mouse.getDX() * MOUSE_SENSETIVITY;
			angleAroundPlayer -= angleChange;
			
		}
	}
	
	*/
	

	
	
	
	public float getFaceView(){
		return player.getPosition().y + 7f;
	}

	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f vector){
		this.position = vector;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	
	
	
	

	
}

package entiies;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Quaternion;
import portal.PortalTile;

public class PortalCamera extends Camera{

	private Vector3f position = new Vector3f(0, 5, 0);
	private Vector3f cameraNormal;
	private float pitch = 10, yaw = 0, roll;

	public PortalCamera(Vector3f position) {
		this.position = position;
	}
	
	
	public PortalCamera(PortalTile tile)
	{
		cameraNormal = tile.getNormalVector();
	}
	
	public PortalCamera(PortalTile tile,Player player)
	{
		super(player);
		cameraNormal = tile.getNormalVector();
	}

	public void rotate(float angle) {
	/*
		Vector3f newPosition = new Vector3f();
		// Get the vVector from our position to the center we are rotating around-
	 
	    // Calculate the sine and cosine of the angle once
	    float cosTheta = (float)Math.cos(angle);
	    float sinTheta = (float)Math.sin(angle);
	 
	    // Find the new x position for the new rotated point
	    newPosition.x  = (cosTheta + (1 - cosTheta) * position.x * position.x)        * position.x;
	    newPosition.x += ((1 - cosTheta) * position.x * position.y - position.z * sinTheta)    * position.y;
	    newPosition.x += ((1 - cosTheta) * position.x* position.z + position.y * sinTheta)    * getPosition().z;
	 
	    // Find the new y position for the new rotated point
	    newPosition.y  = ((1 - cosTheta) * position.x * position.y + position.z * sinTheta)    * position.x;
	    newPosition.y += (cosTheta + (1 - cosTheta) * position.y * position.y)        * position.y;
	    newPosition.y += ((1 - cosTheta) * position.y * position.z - position.x * sinTheta)    * position.z;
	  // Find the new z position for the new rotated point
	    newPosition.z  = ((1 - cosTheta) * position.x * position.z - position.y * sinTheta)    * getPosition().x;
	    newPosition.z += ((1 - cosTheta) * position.y * position.z + position.x * sinTheta)    * getPosition().y;
	    newPosition.z += (cosTheta + (1 - cosTheta) * position.z * position.z)        * getPosition().z;
	 
	    
		// Now we just add the newly rotated vector to our position to set
	    // our new rotated position of our camera.
	    setPosition(Vector3f.add(getPosition(), newPosition, null));
	    */
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getCameraNormal()
	{
		return cameraNormal;
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

	public void setPosition(Vector3f vector) {
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

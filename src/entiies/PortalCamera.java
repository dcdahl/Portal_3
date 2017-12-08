package entiies;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Quaternion;
import portal.PortalTile;

/**
 * Klasse for å håndtere et portal kamera
 * @author Jonatan Bårdsen
 *
 */
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
	
	}
	
	/*
	 * Get set
	 */

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

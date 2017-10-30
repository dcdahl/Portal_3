package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entiies.Camera;

public class MousePicker {

	
	private Vector3f currentRay;
	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Camera camera;
	
	
	public MousePicker(Matrix4f projectionMatrix, Camera camera) {
		
		this.projectionMatrix = projectionMatrix;
		this.camera = camera;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	
	public Vector3f	getCurrentRay(){
		return currentRay;
	}
	
	public void update(){
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}


	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords){
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY){
		float x = (2f*mouseX) / Display.getWidth() -1;
		float y = (2f*mouseY) / Display.getHeight() -1;
		return new Vector2f(x,y);
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords){
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y,-1f, 0f);
		
		
	}
	///////////////////////////////////////////////////////////////////
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	/*
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}
	*/
}

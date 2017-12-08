package toolbox;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entiies.Camera;
/**
 * Denne klassen er hentet nærmest direkte fra ThinMatrix
 * @author DCDah
 *
 */
public class Maths
{
	public static Matrix4f createTransformationMatric(Vector3f translation, float rx, float ry, float rz, float scale){

		Matrix4f matrix = new Matrix4f();

		matrix.setIdentity();

		// Forflytte modellen ( translation )
		Matrix4f.translate(translation, matrix, matrix);

		// Rotere modellen
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

		// Skalere modellen
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	// Kopiert rett fra ThinMatrix
	// Kalkulerer nøyaktig posisjon innenfor en trekant
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos){
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix3f createMatrix3fFromVector3f(Vector3f vector){
		Matrix3f newMatrix = new Matrix3f();
		
		newMatrix.m00 = 0;
		newMatrix.m01 = -vector.z;
		newMatrix.m02 = vector.y;
		newMatrix.m10 = vector.z;
		newMatrix.m11 = 0;
		newMatrix.m12 = -vector.x;
		newMatrix.m20 = -vector.y;
		newMatrix.m21 = vector.x;
		newMatrix.m22 = 0;
		
		return newMatrix;
	}

	public static Matrix4f createViewMatrix(Camera camera, Matrix4f rotationMatrix) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		Matrix4f.negate(rotationMatrix, rotationMatrix);
		Matrix4f.mul(viewMatrix, rotationMatrix, viewMatrix);
		return viewMatrix;
	}

}

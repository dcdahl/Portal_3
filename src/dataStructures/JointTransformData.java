package dataStructures;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * This contains the data for a transformation of one joint, at a certain time
 * in an animation. It has the name of the joint that it refers to, and the
 * local transform of the joint in the pose position.
 * 
 * @author Karl
 *
 */
public class JointTransformData {

	public final String jointNameId;
	public final Matrix4f jointLocalTransform;

	public JointTransformData(String jointNameId, Matrix4f jointLocalTransform) {
		this.jointNameId = jointNameId;
		this.jointLocalTransform = jointLocalTransform;
	}
}

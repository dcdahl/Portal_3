package models;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Animation;
import animation.Animator;
import animation.Joint;
import textures.ModelTexture;

/**
 * Modell som er animert og har struktur.
 * Sterkt inspirert av ThinMatrix
 *
 */
public class AnimatedModel extends TexturedModel
{
	private final Joint rootJoint;
	private final int jointCount;
	private Vector3f vecMin;
	private Vector3f vecMax;
	
	private final Animator animator;
	
	public AnimatedModel( TexturedModel model,Joint rootJoint, int jointCount)
	{
		super(model.getRawModel(), model.getTexture());
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		this.animator = new Animator(this);
		rootJoint.calculateInverseBindTransform(new Matrix4f());
	}
	
	public Joint getRootJoint()
	{
		return rootJoint;
	}

	public void doAnimation(Animation animation)
	{
		animator.doAnimation(animation);
	}
	
	public void update()
	{
		animator.update();
	}
	
	public Matrix4f[] getJointTransforms()
	{
		
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
		
	}
	
	private void addJointsToArray(Joint parentJoint,Matrix4f[] jointMatrices)
	{
		jointMatrices[parentJoint.index] = parentJoint.getAnimatedTransform();
		for(Joint childJoint : parentJoint.children)
			addJointsToArray(childJoint, jointMatrices);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

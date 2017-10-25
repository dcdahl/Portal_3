package models;

import org.lwjgl.util.vector.Matrix4f;

import animation.Animation;
import animation.Animator;
import animation.Joint;
import textures.ModelTexture;

public class AnimatedModel
{
	private final RawModel model;
	
	private final ModelTexture texure;
	
	private final Joint rootJoint;
	private final int jointCount;

	
	private final Animator animator;
	


	public AnimatedModel(RawModel model, ModelTexture texure, Joint rootJoint, int jointCount)
	{
		this.model = model;
		this.texure = texure;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		this.animator = new Animator();
		rootJoint.calculateInverseBindTransform(new Matrix4f());
	}
	public RawModel getModel()
	{
		return model;
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

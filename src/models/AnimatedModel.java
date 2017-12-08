package models;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Animation;
import animation.Animator;
import animation.Joint;
import animation.JointTransform;
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

	/**
	 * Setter hvilken animasjon modellen skal gjøre
	 * @param animation Se {@link Animation}
	 */
	public void doAnimation(Animation animation)
	{
		animator.doAnimation(animation);
	}
	
	/**
	 * Oppdaterer posen til modellen
	 * Se {@link Animator}
	 */
	public void update()
	{
		animator.update();
	}
	/**
	 * Se {@link JointTransform}
	 * @return
	 */
	public Matrix4f[] getJointTransforms()
	{
		
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
		
	}
	
	/**
	 * Legger til nede i hiarkiet for hvert enkelt ledd
	 * @param parentJoint Se {@link Joint}
	 * @param jointMatrices Liste med animasjon matriser
	 */
	private void addJointsToArray(Joint parentJoint,Matrix4f[] jointMatrices)
	{
		jointMatrices[parentJoint.index] = parentJoint.getAnimatedTransform();
		for(Joint childJoint : parentJoint.children)
			addJointsToArray(childJoint, jointMatrices);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

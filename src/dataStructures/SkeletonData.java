package dataStructures;

/*
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * Inneholder data om hvor mange ledd et skjelett har, og hvilket ledd som er rot-leddet
 */
public class SkeletonData {
	
	public final int jointCount;
	public final JointData headJoint;
	
	public SkeletonData(int jointCount, JointData headJoint){
		this.jointCount = jointCount;
		this.headJoint = headJoint;
	}

}

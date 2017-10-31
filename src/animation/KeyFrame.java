package animation;

import java.util.Map;

public class KeyFrame
{
	// Når animasjonen skal skje
	private final float timeStamp;
	// Hvilken posisjon hver enkelt ledd er i
	private final Map<String, JointTransform> pose;

	public KeyFrame(float timeStamp, Map<String, JointTransform> pose)
	{
		super();
		this.timeStamp = timeStamp;
		this.pose = pose;
	}

	
	public float getTimeStamp()
	{
		return timeStamp;
	}

	public Map<String, JointTransform> getJointKeyFrames()
	{
		return pose;
	}

}

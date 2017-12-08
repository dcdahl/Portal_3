package animation;

import java.util.Map;

public class KeyFrame
{
	// Når animasjonen skal skje
	private final float timeStamp;
	/**
	 *  Hvilken posisjon hver enkelt ledd er i. {@link JointTransform}*/ 
	private final Map<String, JointTransform> pose;

	/**
	 * Konstruktør som setter hvilken pose som den animerte figuren skal ha på et gitt tidspunkt
	 * @param timeStamp Tidspunkt hvor figuren skal ha en pose
	 * @param pose Posen som figuren skal ha som en map av navn på leddet og hvilken pose som en {@link JointTransform}
	 */
	public KeyFrame(float timeStamp, Map<String, JointTransform> pose)
	{
		super();
		this.timeStamp = timeStamp;
		this.pose = pose;
	}

	/**
	 * Returnerer timestampen
	 * @return En timestamp som er satt i konstruktøren
	 */
	public float getTimeStamp()
	{
		return timeStamp;
	}
	
	
/**
 * Henter listen med posisjonen hvert enkelt ledd skal flyttes og roteres til 
 * @return Se {@link pose}
 */
	public Map<String, JointTransform> getJointKeyFrames()
	{
		return pose;
	}

}

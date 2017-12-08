package dataStructures;

/**
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * Contains the extracted data for an animation, which includes the length of
 * the entire animation and the data for all the keyframes of the animation.
 * 
 * @author Karl
 *
 */
public class AnimationData {

	public final float lengthSeconds;
	public final KeyFrameData[] keyFrames;

	public AnimationData(float lengthSeconds, KeyFrameData[] keyFrames) {
		this.lengthSeconds = lengthSeconds;
		this.keyFrames = keyFrames;
	}

}

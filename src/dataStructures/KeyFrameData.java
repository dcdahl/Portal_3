package dataStructures;

import java.util.ArrayList;
import java.util.List;
/**
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * Inneholder data om en {@link KeyFrame}
 *
 */
public class KeyFrameData {

	public final float time;
	public final List<JointTransformData> jointTransforms = new ArrayList<JointTransformData>();
	
	public KeyFrameData(float time){
		this.time = time;
	}
	
	public void addJointTransform(JointTransformData transform){
		jointTransforms.add(transform);
	}
	
}

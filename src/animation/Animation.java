package animation;

public class Animation
{
private final float lengthInSeconds;

private final KeyFrame[] keyFrames;

public Animation(KeyFrame[] keyFrames,float lengthInSeconds)
{
	super();
	this.keyFrames = keyFrames;
	this.lengthInSeconds = lengthInSeconds;
}


public float getLengthinseconds()
{
	return lengthInSeconds;
}
public KeyFrame[] getKeyFrames()
{
	return keyFrames;
}

}

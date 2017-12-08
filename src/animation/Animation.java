package animation;

/**
 * 
 * Klasse laget av ThinMatrix (see <a href="https://www.youtube.com/watch?v=F-kcaonjHf8">ThinMatrix Skeleton Animation video 2</a>).
 * Denne klassen er ikke endret på, men skrevet nesten rett av.
 * Håndterer KeyFrames for animasjonen og hvor lenge den varer i sekunder 
 *
 */
public class Animation
{
private final float lengthInSeconds;

private final KeyFrame[] keyFrames;

/**
 * Konstruktor for klassen 
 * @param keyFrames Liste med keyframes som animasjonen inneholder, @see KeyFrame
 * @param lengthInSeconds Lengde i sekunder som animasjonen skal vare
 */
public Animation(KeyFrame[] keyFrames,float lengthInSeconds)
{
	super();
	this.keyFrames = keyFrames;
	this.lengthInSeconds = lengthInSeconds;
}

/**
 * Gir lengden på animasjonen i sekunder
 * @return lengden på en animasjon i sekunder
 */
public float getLengthinseconds()
{
	return lengthInSeconds;
}

/**
 * Returnerer alle keyframes for en animasjon
 * @see KeyFrame
 * @return Array av alle keyframes for animasjonen
 */
public KeyFrame[] getKeyFrames()
{
	return keyFrames;
}

}

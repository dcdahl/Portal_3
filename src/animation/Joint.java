package animation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

/**
 * 
 * Klasse laget av ThinMatrix (see
 * <a href="https://www.youtube.com/watch?v=cieheqt7eqc">ThinMatrix Skeleton
 * Animation video 3</a>). Håndterer et ledd i skjelettet (Engelsk: Joint) og
 * den lokale posen i forhold til foreldre-leddet (leddet som leddet henger fast
 * i).
 */

public class Joint {
	public final int index;
	public final String name;

	// Liste med alle ledd som dette leddet er forelder til
	public final List<Joint> children = new ArrayList<Joint>();
	// Posen som leddet har i animasjonen som en matrise, @see Matrix4f
	private Matrix4f animatedTransform = new Matrix4f();
	// Posen som er utgangsposen som objektet har før animering som en matrise
	private final Matrix4f localBindTransform;
	// Den inverse matyrisen av utgangsposisjonen. Brukes for å regne ut
	// animatedTransform
	private Matrix4f inverseBindTransform = new Matrix4f();

	/**
	 * Konstruktør for klassen
	 * 
	 * @param index
	 *            Indeks for leddet
	 * @param name
	 *            Navn på leddet; f.eks. "Right forearm"
	 * @param localBindTransform
	 *            Se locallBindTransform
	 */
	public Joint(int index, String name, Matrix4f localBindTransform) {
		this.index = index;
		this.name = name;
		this.localBindTransform = localBindTransform;
	}

	/**
	 * Legger til et barn av leddet
	 * 
	 * @param child
	 *            Den nye barnet
	 */
	public void addChild(Joint child) {
		children.add(child);
	}

	/**
	 * Returnerer animatedTransform
	 * 
	 * @return Se animatedTransform
	 */
	public Matrix4f getAnimatedTransform() {
		return animatedTransform;
	}

	/**
	 * Setter animatedTransform
	 * 
	 * @param animationTransform
	 *            Se animatedTransform
	 */
	public void setAnimationTransform(Matrix4f animationTransform) {
		this.animatedTransform = animationTransform;
	}

	/**
	 * Returnerer inverseBindTransform
	 * @return Se inverseBindTransform
	 */
	public Matrix4f getInverseBindTransform()
	{
		return inverseBindTransform;
	}

	/**
	 * kalkulerer inverseBindTransform rekursivt
	 * @param parentBindTransfrom localBindTransform til foreldre-leddet
	 */
	public void calculateInverseBindTransform(Matrix4f parentBindTransfrom) {
		Matrix4f bindTransform = Matrix4f.mul(parentBindTransfrom, localBindTransform, null);
		Matrix4f.invert(bindTransform, inverseBindTransform);
		for (Joint child : children)
			child.calculateInverseBindTransform(bindTransform);
	}

}

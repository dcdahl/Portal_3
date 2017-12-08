package entiies;

import org.lwjgl.util.vector.Vector3f;

import models.AnimatedModel;

/**
 * Entitet som er animert. Flere entiteter kan baseres på samme modell
 * @author Jonatan Bårdsen
 *
 */
public class AnimatedEntity extends Entity
{
	private AnimatedModel model;

	public AnimatedEntity(AnimatedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale)
	{
		super(model, position, rotX, rotY, rotZ, scale);
		this.model = model;
	}
	
	@Override
	public AnimatedModel getModel()
	{
		return model;
	}

}

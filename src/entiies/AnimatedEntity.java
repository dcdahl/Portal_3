package entiies;

import org.lwjgl.util.vector.Vector3f;

import models.AnimatedModel;

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

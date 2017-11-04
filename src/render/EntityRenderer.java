package render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entiies.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {
	

	private StaticShader shader;
	

	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	
	public void render(Map<TexturedModel,List<Entity>> entities){
		
		// G�r gjennom listen med entiteter som skal rendres
		for(TexturedModel model : entities.keySet()){
			prepareTexturedModel(model);
			
			// Henter ut modellen til entiteten
			List<Entity> batch = entities.get(model);
			
			for(Entity entity : batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertextCount(),GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		
		// Aktiverer arrayene p� GPu
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		//GL20.glEnableVertexAttribArray(3);
	//	GL20.glEnableVertexAttribArray(4);
		
		
		// henter texturen og 
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
	}
	
	// Unbinder alle VBOene
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		//GL20.glDisableVertexAttribArray(3);
		//GL20.glDisableVertexAttribArray(4);
		GL30.glBindVertexArray(0);
	}
	
	// Forbereder hver entitet
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatric(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}

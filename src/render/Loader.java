package render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

public class Loader {

	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions,int[] indices, float[] textureCoords, float[] normals){
		// lage en vao
		int vaoID = createVAO();
		bindIndiciesBuffer(indices);
		storeDataInAttributeList(0,3, positions);
		storeDataInAttributeList(1,2, textureCoords);
		storeDataInAttributeList(2,3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadTexture(String fileName){
		Texture tex = null;
		try {
			tex = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(fileName);
			e.printStackTrace();
		}
		int textureID = tex.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	private int createVAO(){
		// Lager en tom VAO
		int vaoID = GL30.glGenVertexArrays();
		
		// aktivere VAOen
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		// Lager en tom VBO
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID); // legger til i listen
		
		// Binder VBOen for å kunne gjøre noe med den. (aktivere den)
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		// Konverterer floatarray til en floatBuffer
		FloatBuffer buffer = storedataInFloatBuffer(data);
		
		// Lagrer buffern i VBOen. 
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		// Legger VBOen inn i VAOen. Attributtnummer hvor du vil lagre det - lengde på hver vertex - type data - normalized? - distanse mellom vertexene (noen data i mellom)
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		
		// unbind buffern
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	// Binder elementdataene til en buffer
	private void bindIndiciesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.addAll(vbos);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDatainInBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	// Lagerer element data inn i en intBuffer
	private IntBuffer storeDatainInBuffer(int [] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	
	public void cleanUp(){
		// Rydder opp vertexarrayene i VAOene
		for(int vao:vaos)
			GL30.glDeleteVertexArrays(vao);
		
		// Rydder opp bufferne i VBOene
		for (int vbo : vaos) 
			GL15.glDeleteBuffers(vbo);
		
		// Rydder opp texturene
		for (int texture : textures) 
			GL11.glDeleteTextures(texture);

	}
	
	// Konverterer floatarray til en floatBuffer
	private FloatBuffer storedataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); // Klargjør buffer til å bli lest fra
		return buffer;
	}


	private void unbindVAO(){
		// Unbinder VAOen
		GL30.glBindVertexArray(0);
	}
	
	
	
	
	
	
}

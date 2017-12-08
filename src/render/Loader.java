package render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.CubemapTexture;

public class Loader
{

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	
    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions,null,null);
    }
	
	public RawModel loadToVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals)
	{
		
		return loadToVAO(positions, indices, textureCoords, normals, null, null);
	}
	
	public RawModel loadToVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals, int[] jointIds,
			float[] vertexWeights)
	{
		// lage en vao
		int vaoID = createVAO();
		bindIndiciesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		
		//Hvis modellen har ledd (skeleton) legg til dette i VAO
		if(jointIds != null && vertexWeights != null)
		{
			//Ledd-id må lagres som intArray
		storeDataInAttributeList(3, 3, intArrayToFloatArray(jointIds), true);
		storeDataInAttributeList(4, 3, vertexWeights);
		}
		unbindVAO();
		return new RawModel(vaoID, indices.length,null,null);
	}

	public int loadTexture(String fileName)
	{
		Texture tex = null;
		try
		{
			tex = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println(fileName);
			e.printStackTrace();
		}
		int textureID = tex.getTextureID();
		textures.add(textureID);
		return textureID;
	}

	private int createVAO()
	{
		// Lager en tom VAO
		int vaoID = GL30.glGenVertexArrays();

		// aktivere VAOen
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
	{
		storeDataInAttributeList(attributeNumber,coordinateSize,data,false);
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data,boolean storeInIntBuffer){
		FloatBuffer floatBuffer = null;
		IntBuffer intBuffer = null;
		
		
		// Lager en tom VBO
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID); // legger til i listen
		
		
		// Binder VBOen for � kunne gj�re noe med den. (aktivere den)
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		
		
		// Konverterer floatarray til en floatBuffer eller en intArray
		if(!storeInIntBuffer)
		floatBuffer = storedataInFloatBuffer(data);
		else
		intBuffer = storeDataInIntBuffer(floatArrayToIntArray(data));
		
		
		
		if(floatBuffer != null)
		{
			// Lagrer buffern i VBOen. 
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
			
		// Legger VBOen inn i VAOen. Attributtnummer hvor du vil lagre det - lengde p� hver vertex - type data - normalized? - distanse mellom vertexene (noen data i mellom)
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		}
		else
		{
			//Lagrer bufferen i VBOen
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
			
			//Samme som glVertexAttribPointer, men for intArrays (ledd-id)
			GL30.glVertexAttribIPointer(attributeNumber, coordinateSize, GL11.GL_INT, coordinateSize*Integer.BYTES, 0);
			//TODO Store joint-ids in IntBuffer
		}
			
			
		// unbind buffern
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	public int loadCubemap(String[] textureFiles){
		int textID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			CubemapTexture data = decodeTextureFile("res/skybox/"+ textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, 
					GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, 
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(textID);
		return textID;
	}
	
	
	// Laster inn et bilde til e byte buffer, og returnerer en CubemapTekstur
	private CubemapTexture decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new CubemapTexture(buffer, width, height);
	}
	

	// Binder elementdataene til en buffer
	private void bindIndiciesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.addAll(vbos);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	// Lagerer element data inn i en intBuffer
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void cleanUp()
	{
		// Rydder opp vertexarrayene i VAOene
		for (int vao : vaos)
			GL30.glDeleteVertexArrays(vao);

		// Rydder opp bufferne i VBOene
		for (int vbo : vaos)
			GL15.glDeleteBuffers(vbo);

		// Rydder opp texturene
		for (int texture : textures)
			GL11.glDeleteTextures(texture);

	}

	// Konverterer floatarray til en floatBuffer
	private FloatBuffer storedataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); // Klargj�r buffer til � bli lest fra
		return buffer;
	}

	private void unbindVAO()
	{
		// Unbinder VAOen
		GL30.glBindVertexArray(0);
	}
	
	
	//Konverterer fra floatarray til intarray
	private int[] floatArrayToIntArray(float[] array)
	{
		int[] newArray = new int[array.length];
		for(int i = 0; i < array.length; i++)
		{
			int temp = (int) array[i];
			newArray[i] = temp;
		}
		return newArray;
	}
	
	//Konverterer fra intArray til floatarray
	private float[] intArrayToFloatArray(int[] array)
	{
		//System.out.println(array.length);
		float[] newArray = new float[array.length];
		for(int i = 0; i < array.length; i++)
		{
			float temp = array[i];
			newArray[i] = temp;
		}
		return newArray;
	}
	
	


}

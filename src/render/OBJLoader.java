package render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import dataStructures.Vertex;
import models.RawModel;
/**
 * 
 * OBJLoadern leser .obj filene 
 * 
 * 
 */
public class OBJLoader {
	
	
	public static RawModel loadObjModel(String filename, Loader loader){
		FileReader fr = null;
		List<String[]> faces = new ArrayList<String[]>(); 
		
		try {
			fr = new FileReader(new File("res/"+filename+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Kunne ikke loade .obj filen");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		Vector3f vecMax = null;
		Vector3f vecMin = null;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {
			line = reader.readLine();
			
			// Leser inn filen, linje for linje, og deler vertexene, teksturkoordinatene og normalene inn i respektive lister. 
			while(line !=null){
				//line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")){
					// Lager en vektor basert på verdiene som leses inn på linjen
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					
					// Hvis det er første gjennomkjøring, lager vi nye vektorer.
					if(vecMax == null){
						vecMax = new Vector3f(vertex.x, vertex.y, vertex.z);
						vecMin = new Vector3f(vertex.x, vertex.y, vertex.z);
					}
					else{
						// Sjekker om verdiene er høyere enn den høyeste sålangt.
						if(vertex.x > vecMax.x)
							vecMax.x = vertex.x;
						if(vertex.y > vecMax.y)
							vecMax.y = vertex.y;
						if(vertex.z > vecMax.z)
							vecMax.z = vertex.z;
						
						// Sjekker om verdiene er lavere enn den laveste sålangt.
						if(vertex.x < vecMin.x)
							vecMin.x = vertex.x;
						if(vertex.y < vecMin.y)
							vecMin.y = vertex.y;
						if(vertex.z < vecMin.z)
							vecMin.z = vertex.z;
					}
					vertices.add(vertex);
				}else if(line.startsWith("vt ")){
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")){
					faces.add(currentLine);
				}
				line = reader.readLine();
			} 
			
			textureArray = new float[vertices.size()*2];
			normalsArray = new float[vertices.size()*3];
			
			for (String[] s : faces) {
				
				String[] vertex1 = s[1].split("/");
				String[] vertex2 = s[2].split("/");
				String[] vertex3 = s[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

			}
			System.out.println(filename + ": Max-X: " + vecMax.x + " Max-Y: " + vecMax.y + " Max-Z: " + vecMax.z);
			System.out.println(filename + ": Min-X: " + vecMin.x + " Min-Y: " + vecMin.y + " Min-Z: " + vecMin.z);

			reader.close();
			
			
		} catch (Exception e) {
			System.out.println("Noe rart med .OBJ filen til " + filename + ".obj. Feil: " + e.getMessage());
			e.printStackTrace();
			
		}
		
		// Konverterer vertexlisten til array
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;			
		}
		
		for(int i= 0;i<indices.size();i++)
			indicesArray[i] = indices.get(i);
		
		RawModel rm = loader.loadToVAO(verticesArray, indicesArray, textureArray, normalsArray);
		rm.setVecMax(vecMax);
		rm.setVecMin(vecMin);
		return rm;

	}
	
	// Denne metoden prosesserer listene med vertexer, texturkoordinater og normaler. 
	// vertexene, texkordinatene og normalene kommer ikke sï¿½rlig strukturert i .obj filene, sï¿½ de mï¿½ omstruktureres sï¿½ de er tilpasset til loadern.
	// Vertex pï¿½ plass nr. [1] skal ha tekstur pï¿½ plass nr [1] og normal pï¿½ [1].. pï¿½ denne mï¿½ten er det mer struktur pï¿½ det. 
	// Dette gjï¿½res med faces. Et face forteller hvilke vertexer som hï¿½rer til hvilke texturekoordinater og normaler. 
	private static void processVertex(String[] vertexData, List<Integer> indices, 
			List<Vector2f> textures, List<Vector3f> normals, 
			float[] textureArray,float[] normalsArray){
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
	}
	
}

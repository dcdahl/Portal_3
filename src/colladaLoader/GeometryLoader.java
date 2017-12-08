package colladaLoader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import dataStructures.MeshData;
import dataStructures.Vertex;
import dataStructures.VertexSkinData;
import xmlParser.XmlNode;

/**
 * 
 * 
 * Loader som henter all informasjon om gemoetrien (meshen) til en model fra en COLLADA-fil
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 *
 *
 */
public class GeometryLoader {
//På grunn av at Blender har z-aksen oppover
	private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0,0));
	//Data om meshen i en XML-node
	private final XmlNode meshData;

	//Liste med data om "huden" til modellen
	private final List<VertexSkinData> vertexWeights;
	
	//arrayer med data om modellen
	private float[] verticesArray;
	private float[] normalsArray;
	private float[] texturesArray;
	private int[] indicesArray;
	private int[] jointIdsArray;
	private float[] weightsArray;

	//Lister for å håndtere vertekser, tekstur, normaler og indekser  
	List<Vertex> vertices = new ArrayList<Vertex>();
	List<Vector2f> textures = new ArrayList<Vector2f>();
	List<Vector3f> normals = new ArrayList<Vector3f>();
	List<Integer> indices = new ArrayList<Integer>();
	
	
	/**
	 * Konstruktør for klassen
	 * @param geometryNode XML-node med all informasjon om geometrien til modellen
	 * @param vertexWeights Liste med data om hvor mye meshene påvirkes av bevegelser til andre ledd
	 */
	public GeometryLoader(XmlNode geometryNode, List<VertexSkinData> vertexWeights) {
		this.vertexWeights = vertexWeights;
		this.meshData = geometryNode.getChild("geometry").getChild("mesh");
	}
	
	/**
	 * Henter data om en enkelt mesh fra en XML-struktur
	 * @return Data om meshen. Se {@link MeshData}
	 */
	public MeshData extractModelData(){
		readRawData();
		assembleVertices();
		removeUnusedVertices();
		initArrays();
		convertDataToArrays();
		convertIndicesListToArray();
		return new MeshData(verticesArray, texturesArray, normalsArray, indicesArray, jointIdsArray, weightsArray);
	}

	/**
	 * Leser rådata fra XML-struktur
	 */
	private void readRawData() {
		readPositions();
		readNormals();
		readTextureCoords();
	}

	/**
	 * Leser verteks-posisjoner for en modell fra  en XML-struktur
	 */
	private void readPositions() {
		String positionsId = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		XmlNode positionsData = meshData.getChildWithAttribute("source", "id", positionsId).getChild("float_array");
		int count = Integer.parseInt(positionsData.getAttribute("count"));
		String[] posData = positionsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			float x = Float.parseFloat(posData[i * 3]);
			float y = Float.parseFloat(posData[i * 3 + 1]);
			float z = Float.parseFloat(posData[i * 3 + 2]);
			Vector4f position = new Vector4f(x, y, z, 1);
			Matrix4f.transform(CORRECTION, position, position);
			vertices.add(new Vertex(vertices.size(), new Vector3f(position.x, position.y, position.z), vertexWeights.get(vertices.size())));
		}
	}

	/**
	 * Leser verteks-normaler for en modell fra en XML-struktur
	 */
	private void readNormals() {
		String normalsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "NORMAL")
				.getAttribute("source").substring(1);
		XmlNode normalsData = meshData.getChildWithAttribute("source", "id", normalsId).getChild("float_array");
		int count = Integer.parseInt(normalsData.getAttribute("count"));
		String[] normData = normalsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			float x = Float.parseFloat(normData[i * 3]);
			float y = Float.parseFloat(normData[i * 3 + 1]);
			float z = Float.parseFloat(normData[i * 3 + 2]);
			Vector4f norm = new Vector4f(x, y, z, 0f);
			Matrix4f.transform(CORRECTION, norm, norm);
			normals.add(new Vector3f(norm.x, norm.y, norm.z));
		}
	}

	/**
	 * Leser tekstur-koordinater for en modell fra en XML-struktur
	 */
	private void readTextureCoords() {
		String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "TEXCOORD")
				.getAttribute("source").substring(1);
		XmlNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
		int count = Integer.parseInt(texCoordsData.getAttribute("count"));
		String[] texData = texCoordsData.getData().split(" ");
		for (int i = 0; i < count/2; i++) {
			float s = Float.parseFloat(texData[i * 2]);
			float t = Float.parseFloat(texData[i * 2 + 1]);
			textures.add(new Vector2f(s, t));
		}
	}
	
	/**
	 * setter sammen verteksene med indeksene, normalene og tekstur-koordinatene
	 */
	private void assembleVertices(){
		XmlNode poly = meshData.getChild("polylist");
		int typeCount = poly.getChildren("input").size();
		String[] indexData = poly.getChild("p").getData().split(" ");
		for(int i=0;i<indexData.length/typeCount;i++){
			int positionIndex = Integer.parseInt(indexData[i * typeCount]);
			int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
			int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
			processVertex(positionIndex, normalIndex, texCoordIndex);
		}
	}
	

	/**
	 * Setter dataene fra assembleVertices sammen
	 * @param posIndex Indeksen på posisjonsdata på verteksen
	 * @param normIndex Indeksen på normalen på verteksen
	 * @param texIndex Indeksen på tekstur-koordinatet på verteksen
	 * @return
	 */
	private Vertex processVertex(int posIndex, int normIndex, int texIndex) {
		Vertex currentVertex = vertices.get(posIndex);
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(texIndex);
			currentVertex.setNormalIndex(normIndex);
			indices.add(posIndex);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, texIndex, normIndex);
		}
	}

	/**
	 * Konverterer en liste med verteks-indekser til en array 
	 * @return En array med verteks-indekser
	 */
	private int[] convertIndicesListToArray() {
		this.indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	/**
	 * Konverter listene med data om meshen til arrayer 
	 * @return Punktet som er lengst vekk av verteksene
	 */
	private float convertDataToArrays() {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			//Brukes for å beregne AABB-bokser
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			VertexSkinData weights = currentVertex.getWeightsData();
			jointIdsArray[i * 3] = weights.jointIds.get(0);
			jointIdsArray[i * 3 + 1] = weights.jointIds.get(1);
			jointIdsArray[i * 3 + 2] = weights.jointIds.get(2);
			weightsArray[i * 3] = weights.weights.get(0);
			weightsArray[i * 3 + 1] = weights.weights.get(1);
			weightsArray[i * 3 + 2] = weights.weights.get(2);

		}
		return furthestPoint;
	}

	/**
	 * Metode som samler like vertekser i samme datastruktur.
	 * @param previousVertex Forrige verteks. Se {@link Vertex}
	 * @param newTextureIndex Indeks hvor tekstur-koordinatet til verteksen er lagret
	 * @param newNormalIndex Indeks hvor tekstur-koordinatet til verteksen er lagret
	 * @return En 
	 */
	private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}
	
	/**
	 * Metode for å intitialisere arrayer
	 */
	private void initArrays(){
		this.verticesArray = new float[vertices.size() * 3];
		this.texturesArray = new float[vertices.size() * 2];
		this.normalsArray = new float[vertices.size() * 3];
		this.jointIdsArray = new int[vertices.size() * 3];
		this.weightsArray = new float[vertices.size() * 3];
	}

	/**
	 * Fjerner vertekser som ikke brukes i meshen
	 */
	private void removeUnusedVertices() {
		for (Vertex vertex : vertices) {
			vertex.averageTangents();
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
	
}
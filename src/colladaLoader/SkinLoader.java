package colladaLoader;

import java.util.ArrayList;
import java.util.List;

import animation.Joint;
import dataStructures.SkinningData;
import dataStructures.VertexSkinData;
import xmlParser.XmlNode;

/**
 * 
 * 
 * Loader som henter all informasjon om "huden" til en model fra en XML-struktur
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 *
 *
 */
public class SkinLoader {

	private final XmlNode skinningData;
	private final int maxWeights;

	/**
	 * Konstruktør
	 * @param controllersNode XML-node med data om hvordan meshen legges rundt leddene
	 * @param maxWeights Maks antall ledd som bevegelse i et ledd påvirker 
	 */
	public SkinLoader(XmlNode controllersNode, int maxWeights) {
		this.skinningData = controllersNode.getChild("controller").getChild("skin");
		this.maxWeights = maxWeights;
	}

	/**
	 * Henter data om "huden" fra XML-noden
	 * @return Data om "huden" ({@link SkinningData}).
	 */
	public SkinningData extractSkinData() {
		List<String> jointsList = loadJointsList();
		float[] weights = loadWeights();
		XmlNode weightsDataNode = skinningData.getChild("vertex_weights");
		int[] effectorJointCounts = getEffectiveJointsCounts(weightsDataNode);
		List<VertexSkinData> vertexWeights = getSkinData(weightsDataNode, effectorJointCounts, weights);
		return new SkinningData(jointsList, vertexWeights);
	}

	/**
	 * Henter liste med ledd fra XML-noden
	 * @return Liste med ledd (Se {@link Joint}).
	 */
	private List<String> loadJointsList() {
		XmlNode inputNode = skinningData.getChild("vertex_weights");
		String jointDataId = inputNode.getChildWithAttribute("input", "semantic", "JOINT").getAttribute("source")
				.substring(1);
		XmlNode jointsNode = skinningData.getChildWithAttribute("source", "id", jointDataId).getChild("Name_array");
		String[] names = jointsNode.getData().split(" ");
		List<String> jointsList = new ArrayList<String>();
		for (String name : names) {
			jointsList.add(name);
		}
		return jointsList;
	}

	/**
	 * Henter hvor mye hver verteks påvirkes av andre vertekser
	 * @return Array med flyttall
	 */
	private float[] loadWeights() {
		XmlNode inputNode = skinningData.getChild("vertex_weights");
		String weightsDataId = inputNode.getChildWithAttribute("input", "semantic", "WEIGHT").getAttribute("source")
				.substring(1);
		XmlNode weightsNode = skinningData.getChildWithAttribute("source", "id", weightsDataId).getChild("float_array");
		String[] rawData = weightsNode.getData().split(" ");
		float[] weights = new float[rawData.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Float.parseFloat(rawData[i]);
		}
		return weights;
	}

	/**
	 * Henter hvor mange vertekser som påvirkes og i hvilken grad
	 * @param weightsDataNode XML-node med data om hvor mye hver verteks påvirkes av andre vertekser
	 * @return 
	 */
	private int[] getEffectiveJointsCounts(XmlNode weightsDataNode) {
		String[] rawData = weightsDataNode.getChild("vcount").getData().split(" ");
		int[] counts = new int[rawData.length];
		for (int i = 0; i < rawData.length; i++) {
			counts[i] = Integer.parseInt(rawData[i]);
		}
		return counts;
	}
/**
 * Setter sammen data om hvor mye hver verteks påvirkes til en liste med {@link VertexSkinData}
 * @param weightsDataNode Data om hvor mye hver verteks påvirkes lagret i en XML-node
 * @param counts Hvor mange vertekser som blir påvirket
 * @param weights Hvor mye verteksene blir påvirket
 * @return Liste med {@link VertexSkinData}
 */
	private List<VertexSkinData> getSkinData(XmlNode weightsDataNode, int[] counts, float[] weights) {
		String[] rawData = weightsDataNode.getChild("v").getData().split(" ");
		List<VertexSkinData> skinningData = new ArrayList<VertexSkinData>();
		int pointer = 0;
		for (int count : counts) {
			VertexSkinData skinData = new VertexSkinData();
			for (int i = 0; i < count; i++) {
				int jointId = Integer.parseInt(rawData[pointer++]);
				int weightId = Integer.parseInt(rawData[pointer++]);
				skinData.addJointEffect(jointId, weights[weightId]);
			}
			skinData.limitJointNumber(maxWeights);
			skinningData.add(skinData);
		}
		return skinningData;
	}

}

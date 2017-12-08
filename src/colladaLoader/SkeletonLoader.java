package colladaLoader;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Joint;
import dataStructures.JointData;
import dataStructures.SkeletonData;
import xmlParser.XmlNode;


/**
 * 
 * 
 * Loader som henter all informasjon om skjelettet til en model fra en XML-struktur
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 *
 *
 */
public class SkeletonLoader {

	private XmlNode armatureData;
	
	/**
	 * Liste med hvilken rekkefølge leddene (se {@link Joint}) skal legges til i skjelettet. Inneholder navnene på leddene
	 */
	private List<String> boneOrder;
	
	private int jointCount = 0;
	
	/**
	 * På grunn av at Blender har z-aksen rett oppover
	 */
	private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));

	/**
	 * Konstruktør
	 * @param visualSceneNode Data om leddene som en XML-node
	 * @param boneOrder Se boneOrder
	 */
	public SkeletonLoader(XmlNode visualSceneNode, List<String> boneOrder) {
		this.armatureData = visualSceneNode.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		this.boneOrder = boneOrder;
	}
	
	/**
	 * Henter data om skjelettet fra en XML-node
	 * @return Data om skjelettet. Se {@link SkeletonData}.
	 */
	public SkeletonData extractBoneData(){
		XmlNode headNode = armatureData.getChild("node");
		JointData headJoint = loadJointData(headNode, true);
		return new SkeletonData(jointCount, headJoint);
	}
	
	/**
	 * Loader data om hvert ledd og alle ledd "barne-ledd" rekursivt
	 * @param jointNode XML-node med data om leddet
	 * @param isRoot True hvis leddet er rot-leddet (har ingen forelder-ledd) 
	 * @return Data om leddet (Se {@link JointData})
	 */ 
	private JointData loadJointData(XmlNode jointNode, boolean isRoot){
		JointData joint = extractMainJointData(jointNode, isRoot);
		for(XmlNode childNode : jointNode.getChildren("node")){
			joint.addChild(loadJointData(childNode, false));
		}
		return joint;
	}
	
	/**
	 * Henter data om et ledd
	 * @param jointNode XML-node med data om leddet
	 * @param isRoot True hvis jointNode er data om rot-leddet 
	 * @return Data om leddet
	 */
	private JointData extractMainJointData(XmlNode jointNode, boolean isRoot){
		String nameId = jointNode.getAttribute("id");
		int index = boneOrder.indexOf(nameId);
		String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		Matrix4f matrix = new Matrix4f();
		matrix.load(convertData(matrixData));
		matrix.transpose();
		if(isRoot){
			//because in Blender z is up, but in our game y is up.
			Matrix4f.mul(CORRECTION, matrix, matrix);
		}
		jointCount++;
		return new JointData(index, nameId, matrix);
	}
	
	/**
	 * Henter rådata fra array, og legger den i en buffer
	 * @param rawData Rådata om ett ledd
	 * @return En flyttallsbuffer (Se {@link FloatBuffer}).
	 */
	private FloatBuffer convertData(String[] rawData){
		float[] matrixData = new float[16];
		for(int i=0;i<matrixData.length;i++){
			matrixData[i] = Float.parseFloat(rawData[i]);
		}
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrixData);
		buffer.flip();
		return buffer;
	}

}

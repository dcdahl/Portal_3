package colladaLoader;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.JointTransform;
import dataStructures.AnimationData;
import dataStructures.JointTransformData;
import dataStructures.KeyFrameData;
import xmlParser.XmlNode;
/**
 * Loader som henter all informasjon om en animasjon fra en XML-node
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 */
public class AnimationLoader {
	
	/**
	 * Korrigerer at z-indeksen peker opp i Blender
	 */
	private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
	
	/**
	 * XML-node som inneholder all informasjon om en animasjon. Se {@link XmlNode}}
	 */
	private XmlNode animationData;
	/**
	 * XML-node som inneholder all informasjon om hvem ledd som er foreldre til hvilke ledd. Se {@link XmlNode}}
	 */
	private XmlNode jointHierarchy;
	
	/**
	 * Konstruktør for klassen
	 * @param animationData Se animationData
	 * @param jointHierarchy Se jointHiarchy
	 */
	public AnimationLoader(XmlNode animationData, XmlNode jointHierarchy){
		this.animationData = animationData;
		this.jointHierarchy = jointHierarchy;
	}
	
	/**
	 * Henter data om en animasjon fra en XML-struktur
	 * @return Data om animasjonen. Se {@link AnimationData}
	 */
	public AnimationData extractAnimation(){
		String rootNode = findRootJointName();
		float[] times = getKeyTimes();
		float duration = times[times.length-1];
		KeyFrameData[] keyFrames = initKeyFrames(times);
		List<XmlNode> animationNodes = animationData.getChildren("animation");
		for(XmlNode jointNode : animationNodes){
			loadJointTransforms(keyFrames, jointNode, rootNode);
		}
		return new AnimationData(duration, keyFrames);
	}
	
	/**
	 * Henter når hver del av animasjonen som en liste av flyttall fra en XML-struktur
	 * @return Array av flyttall
	 */
	private float[] getKeyTimes(){
		XmlNode timeData = animationData.getChild("animation").getChild("source").getChild("float_array");
		String[] rawTimes = timeData.getData().split(" ");
		float[] times = new float[rawTimes.length];
		for(int i=0;i<times.length;i++){
			times[i] = Float.parseFloat(rawTimes[i]);
		}
		return times;
	}
	

	/**
	 * Henter alle KeyFrameData utifra hvilken tid i animasjonen den skal vises
	 * @param times Array av flyttall
	 * @return Array av {@link KeyFrameData}}
	 */
	private KeyFrameData[] initKeyFrames(float[] times){
		KeyFrameData[] frames = new KeyFrameData[times.length];
		for(int i=0;i<frames.length;i++){
			frames[i] = new KeyFrameData(times[i]);
		}
		return frames;
	}
	
	/**
	 * Henter data om en {@link JointTransform} utifra en XML-struktur}
	 * @param frames Array med data om KeyFrames
	 * @param jointData XML-node med data om en {@link Joint}.
	 * @param rootNodeId ID til rootnoden i XML-dataen
	 */
	private void loadJointTransforms(KeyFrameData[] frames, XmlNode jointData, String rootNodeId){
		String jointNameId = getJointName(jointData);
		String dataId = getDataId(jointData);
		XmlNode transformData = jointData.getChildWithAttribute("source", "id", dataId);
		String[] rawData = transformData.getChild("float_array").getData().split(" ");
		processTransforms(jointNameId, rawData, frames, jointNameId.equals(rootNodeId));
	}
	
	/**
	 * Henter ID for en ledd basert på XML-node
	 * @param jointData XML-noden
	 * @return ID som String
	 */
	private String getDataId(XmlNode jointData){
		XmlNode node = jointData.getChild("sampler").getChildWithAttribute("input", "semantic", "OUTPUT");
		return node.getAttribute("source").substring(1);
	}
	
	/**
	 * Henter navnet på et ledd 
	 * @param jointData XML-node med data om leddet
	 * @return Navnet på leddet som String
	 */
	private String getJointName(XmlNode jointData){
		XmlNode channelNode = jointData.getChild("channel");
		String data = channelNode.getAttribute("target");
		return data.split("/")[0];
	}
	
	/**
	 * Legger til JointTransforms til hver enkelt KeyFrame
	 * @param jointName Navn på leddet
	 * @param rawData Array med timestamps
	 * @param keyFrames Array med data om keyframes
	 * @param root True hvis gjeldende node er rot-noden
	 */
	private void processTransforms(String jointName, String[] rawData, KeyFrameData[] keyFrames, boolean root){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		float[] matrixData = new float[16];
		for(int i=0;i<keyFrames.length;i++){
			for(int j=0;j<16;j++){
				matrixData[j] = Float.parseFloat(rawData[i*16 + j]);
			}
			buffer.clear();
			buffer.put(matrixData);
			buffer.flip();
			Matrix4f transform = new Matrix4f();
			transform.load(buffer);
			transform.transpose();
			if(root){
				//because up axis in Blender is different to up axis in game
				Matrix4f.mul(CORRECTION, transform, transform);
			}
			keyFrames[i].addJointTransform(new JointTransformData(jointName, transform));
		}
	}
	
	/**
	 * Finner navnet på rot-leddet i figuren fra en XML-struktur
	 * @return Navnet på leddet som en String
	 */
	private String findRootJointName(){
		XmlNode skeleton = jointHierarchy.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		return skeleton.getChild("node").getAttribute("id");
	}


}

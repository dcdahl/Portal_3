package colladaLoader;

import dataStructures.AnimatedModelData;

import dataStructures.AnimationData;
import dataStructures.MeshData;
import dataStructures.SkeletonData;
import dataStructures.SkinningData;
import xmlParser.XmlNode;
import xmlParser.XmlParser;

/**
 * Loader som henter all informasjon om en animasjon og en model fra en COLLADA-fil
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 *Kommentarer skrevet selv.
 */
public class ColladaLoader {

	/**
	 * Metode for å hente data om meshen fra en model pg tilhørende ledd fra en COLLADA-fil
	 * @param colladaFile COLLADA-filen. Se {@link MyFile}
	 * @param maxWeights Maks antall ledd som et ledd kan påvirkes av ved transformering
	 * @return Data om modellen og alle ledd tyilhørende modellen. Se {@link AnimatedModelData}
	 */
	public static AnimatedModelData loadColladaModel(MyFile colladaFile, int maxWeights) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}

	/**
	 * Metode for å hente all data om en animasjon fra en COLLADA-fil
	 * @param colladaFile COLLADA-filen
	 * @return Data om animasjonen. Se {@link AnimationData}}
	 */
	public static AnimationData loadColladaAnimation(MyFile colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}

}

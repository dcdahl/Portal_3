
package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.TextureLoader;

import colladaLoader.AnimationLoader;
import colladaLoader.ColladaLoader;
import colladaLoader.MyFile;
import dataStructures.AnimatedModelData;
import dataStructures.AnimationData;
import dataStructures.JointData;
import dataStructures.JointTransformData;
import dataStructures.KeyFrameData;
import dataStructures.MeshData;
import dataStructures.SkeletonData;
import entiies.AnimatedEntity;
import entiies.Camera;
import entiies.Entity;
import entiies.Light;
import entiies.Player;
import models.AnimatedModel;
import models.RawModel;
import models.TexturedModel;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import render.OBJLoader;
import render.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.MousePicker;
import animation.*;

public class MainSpillLoop
{

	private static final int MAX_WEIGHTS = 3;

	public static void main(String[] args)
	{

		DisplayManager.createDisplay();

		MasterRenderer renderer = new MasterRenderer();
		Loader loader = new Loader();
		List<Entity> staticObjects = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<AnimatedEntity> animatedObjects = new ArrayList<AnimatedEntity>();

		Vector3f lys = new Vector3f(100, 10, 120);
		Light light = new Light(lys, new Vector3f(1, 1, 1));

		// Terreng
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		terrains.add(terrain);
		// terrains.add(terrain2);

		// Building
		ModelTexture buildingTex = new ModelTexture(loader.loadTexture("wall_texture"));
		RawModel buildingRawModel = OBJLoader.loadObjModel("cube_uvmapped", loader);
		TexturedModel buildingtexturedModel = new TexturedModel(buildingRawModel, buildingTex);
		Entity building = new Entity(buildingtexturedModel, new Vector3f(95, -2, 82), 0, 0, 0, 10f);
		ModelTexture buildingspec = buildingtexturedModel.getTexture();
		buildingspec.setShineDamper(500);
		buildingspec.setReflectivity(0.4f);
		staticObjects.add(building);

		// Building
		ModelTexture cubeTex = new ModelTexture(loader.loadTexture("wall_texture"));
		RawModel cubeRawModel = OBJLoader.loadObjModel("cube", loader);
		TexturedModel bcubetexturedModel = new TexturedModel(cubeRawModel, cubeTex);
		Entity cube = new Entity(bcubetexturedModel, new Vector3f(50, 5, 82), 0, 0, 0, 2f);
		ModelTexture cubespec = bcubetexturedModel.getTexture();
		cubespec.setShineDamper(500);
		cubespec.setReflectivity(0.4f);
		staticObjects.add(cube);

		// Supermann
		ModelTexture superTex = new ModelTexture(loader.loadTexture("Superman_Diff"));
		RawModel superRawModel = OBJLoader.loadObjModel("superman", loader);
		TexturedModel superMantexturedModel = new TexturedModel(superRawModel, superTex);
		Entity superMan = new Entity(superMantexturedModel, new Vector3f(0, 0, 10), 0, 0, 0, 10f);
		ModelTexture superManspec = superMantexturedModel.getTexture();
		superManspec.setShineDamper(500);
		superManspec.setReflectivity(0.4f);
		// staticObjects.add(superMan);

		Player player = new Player(superMantexturedModel, new Vector3f(100, 0, 100), 0, 0, 0, 1);
		staticObjects.add(player);

		Camera camera = new Camera(player);

		MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera);

		
		
		// Animatert figur
		ModelTexture animatedTex = new ModelTexture(loader.loadTexture("diffuse"));

		File model = new File("res/model.dae");

		MyFile modelFile = new MyFile(model);
		AnimatedModelData animatedModelData = ColladaLoader.loadColladaModel(modelFile, MAX_WEIGHTS);
		AnimationData animationData = ColladaLoader.loadColladaAnimation(modelFile);
		MeshData mesh = animatedModelData.getMeshData();
		RawModel rawModel = loader.loadToVAO(mesh.getVertices(), mesh.getIndices(), mesh.getTextureCoords(),
				mesh.getNormals(), mesh.getJointIds(), mesh.getVertexWeights());
		TexturedModel texModel = new TexturedModel(rawModel, animatedTex);
		SkeletonData skeletonData = animatedModelData.getJointsData();
		AnimatedModel animatedModel = loadEntity(animatedModelData, texModel);
		Animation animation = loadAnimation(animationData);
		AnimatedEntity newAnimatedEntity = new AnimatedEntity(animatedModel, new Vector3f(0, 0, 10), 0, 0, 0, 0.2f);
		
		animatedObjects.add(newAnimatedEntity);
		
		Animator animator = new Animator(animatedModel);
		animator.doAnimation(animation);
		while (!Display.isCloseRequested())
		{
			// Aktiverer bevegelse av kamera
			camera.move();

			// Spillerbevegelse
			player.move();

			picker.update();
			System.out.println(picker.getCurrentRay());
			animator.update();
			
			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);

			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);

			// Lys, flytter lyset litt for � vise at normalene er implementert
			// riktig
			// lys.x = lys.x - 0.05f;
			// renderer lyset
			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

	}

	/*
	 * Fors�k p� � f� jorden til � g� i bane rundt solen Work in progress :-)
	 */
	private static Vector3f calcOrbit(Vector3f posOrigin, Vector3f posOrbiter, float degree, int radius)
	{

		Double x = Math.cos(degree) * radius;
		Double z = Math.sin(degree) * radius;

		posOrbiter.x = posOrigin.x + x.floatValue();
		posOrbiter.z = posOrigin.z + z.floatValue();

		return new Vector3f(posOrbiter);
	}

	private static float i = 2f;

	private static float exp()
	{
		return i * i;

	}
	
	public static AnimatedModel loadEntity(AnimatedModelData entityData,TexturedModel texture) {
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(texture, headJoint, skeletonData.jointCount);
	}
	
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}

	
	/**
	 * Creates a keyframe from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data about the keyframe that was extracted from the
	 *            collada file.
	 * @return The keyframe.
	 */
	private static KeyFrame createKeyFrame(KeyFrameData data) {
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jointData : data.jointTransforms) {
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}
	
	/**
	 * Creates a joint transform from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data from the collada file.
	 * @return The joint transform.
	 */
	private static JointTransform createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}
	
	/**
	 * Loads up a collada animation file, and returns and animation created from
	 * the extracted animation data from the file.
	 * 
	 * @param colladaFile
	 *            - the collada file containing data about the desired
	 *            animation.
	 * @return The animation made from the data in the file.
	 */
	public static Animation loadAnimation(AnimationData animationData) {
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(frames, animationData.lengthSeconds);
	}
	


}

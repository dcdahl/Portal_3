package main;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.TextureLoader;

import entiies.Camera;
import entiies.Entity;
import entiies.Light;
import entiies.Player;
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

public class MainSpillLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		
		
		MasterRenderer renderer = new MasterRenderer();
		Loader loader = new Loader();
		List<Entity> staticObjects = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		
		
		Vector3f lys = new Vector3f(100,10,120);
		Light light = new Light(lys,new Vector3f(1,1,1));
		
		
		
		// Terreng
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		terrains.add(terrain);
		//terrains.add(terrain2);
		
		
		// Building
		ModelTexture buildingTex = new ModelTexture(loader.loadTexture("solid_grey"));
		RawModel buildingRawModel = OBJLoader.loadObjModel("cube_uvmapped2", loader);
		TexturedModel buildingtexturedModel = new TexturedModel(buildingRawModel, buildingTex);
		Entity building = new Entity(buildingtexturedModel, new Vector3f(95,-2,82),0,0,0,10f);
		ModelTexture buildingspec = buildingtexturedModel.getTexture();
		buildingspec.setShineDamper(500);
		buildingspec.setReflectivity(0.4f);
		staticObjects.add(building);

		
		// Supermann
		ModelTexture superTex = new ModelTexture(loader.loadTexture("Superman_Diff"));
		RawModel superRawModel = OBJLoader.loadObjModel("superman", loader);
		TexturedModel superMantexturedModel = new TexturedModel(superRawModel, superTex);
		Entity superMan = new Entity(superMantexturedModel, new Vector3f(0,0,10),0,0,0,10f);
		ModelTexture superManspec = superMantexturedModel.getTexture();
		superManspec.setShineDamper(500);
		superManspec.setReflectivity(0.4f);
		//staticObjects.add(superMan);
		

		Player player = new Player(superMantexturedModel, new Vector3f(100,0, 100), 0, 0, 0, 1);
        staticObjects.add(player);
        
        Camera camera = new Camera(player);
		
		while (!Display.isCloseRequested()) {
			// Aktiverer bevegelse av kamera
			camera.move();
			
			// Spillerbevegelse
			player.move();
    
			

			// Rendrer objektene
			for(Entity entitys:staticObjects)
                renderer.processEntity(entitys);
			
			// Render terreng
			for(Terrain terr:terrains)
                renderer.processTerrain(terr);
			

			

			// Lys, flytter lyset litt for å vise at normalene er implementert riktig
			//lys.x = lys.x - 0.05f;
			// renderer lyset
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
		

	}
	
	
	
	
	/*
	 * Forsøk på å få jorden til å gå i bane rundt solen
	 * Work in progress :-)
	 * */
	private static Vector3f calcOrbit(Vector3f posOrigin, Vector3f posOrbiter, float degree, int radius){
		
		Double x = Math.cos(degree) * radius;
		Double z = Math.sin(degree) * radius;
		
		posOrbiter.x = posOrigin.x + x.floatValue();
		posOrbiter.z = posOrigin.z + z.floatValue();
		
		return new Vector3f(posOrbiter);
	}
	
	private static float i = 2f;
	
	private static float exp(){
		return i*i;
		
	}
	

	
	

}

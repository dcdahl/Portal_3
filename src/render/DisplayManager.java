package render;



import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	//private static final int WIDTH = 1366;
	//private static final int HEIGHT = 768;
	
	//private static final int WIDTH = 2560;
	//private static final int HEIGHT = 1440;
	
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	private static final int FPS = 60;
	
	// Tid beregning for bevegelser
	private static long lastFrameTime;
	private static float delta;
	

	
	
	
	// Lager displayet og setter innstillingene til det. 
	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(),attribs);
			Display.setTitle("Mitt første spill :-D");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Hvor i spillet det kan bli rendret
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	public static void updateDisplay(){
		Display.sync(FPS);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	public static void closeDisplay(){
		Display.destroy();
	}
	
	
	private static long getCurrentTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}

}

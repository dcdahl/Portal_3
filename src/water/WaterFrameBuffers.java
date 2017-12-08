package water;
 
import java.nio.ByteBuffer;
 
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL42;
 
/**
 * Denne klassen gjør det mulig å rendre til egendefinerte frame buffer objekts
 * i stedet for den defaulte frame bufferen
 * @author Marius
 * Skrevet fra ThinMatrix water tutorial 
 */
public class WaterFrameBuffers {
 
	
    protected static final int REFLECTION_WIDTH = 1280;
    private static final int REFLECTION_HEIGHT = 720;

    //Bestemmer oppløsningen på refraksjonen
    protected static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;
    
    private int reflectionFBO;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
     
    private int refractionFBO;
    private int refractionTexture;
    private int refractionDepthTexture;
 
    public WaterFrameBuffers() {
        initReflectionFBO();
        initRefractionFBO();
    }
 
    //Sletter alle bundet FBOs, textures og renderbuffers
    //Blir kjørt når man lukker spillet
    public void cleanUp() {
        GL30.glDeleteFramebuffers(reflectionFBO);
        GL11.glDeleteTextures(reflectionTexture);
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
        GL30.glDeleteFramebuffers(refractionFBO);
        GL11.glDeleteTextures(refractionTexture);
        GL11.glDeleteTextures(refractionDepthTexture);
    }
 
    //Binder FBOen for refleksjon slik at vi kan rendre til FBO i stedet for default frame buffer 
    public void bindReflectionFBO() {
        bindFBO(reflectionFBO,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }
     
    //Binder FBOen for refraksjon slik at vi kan rendre til denne FBOen
    public void bindRefractionFBO() {
        bindFBO(refractionFBO,REFRACTION_WIDTH,REFRACTION_HEIGHT);
    }
     
    //Kjør før man bytter FBO eller vil tilbake til default frame buffer
    public void unbindFBO() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
 
    public int getReflectionTexture() {
        return reflectionTexture;
    }
     
    public int getRefractionTexture() {
        return refractionTexture;
    }
 
    //Trenger IKKE å teksturere dybdeinformasjonen, men trengr det fortsatt for ikke å ha for "flatt" bildet på refleksjon
    private void initReflectionFBO() {
        reflectionFBO = createFBO();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindFBO();
    }
     
    //I refraksjon vil vi også teksturere forskjellige dybde i scenen så vi må teksturere med en depth buffer
    private void initRefractionFBO() {
        refractionFBO = createFBO();
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        unbindFBO();
    }
     
    private void bindFBO(int frameBuffer, int width, int height){
        //Forsørge at vi ikke har bundet noen textures før vi binder FBOen 
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //Endrer oppløsningen på viewporten til oppløsningen på FBOen
        GL11.glViewport(0, 0, width, height);
    }
 
    private int createFBO() {
    	//Lager FBO og lagrer IDen til FBOen
    	int frameBuffer = GL30.glGenFramebuffers();
    	//Binder FBOen så vi kan bruke den 
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //Indikerer at vi alltid skal rendrere til COLOR attachment 0 når vi rendrer FBOene. 
        //Vi må velge en color attachment å rendre til når vi skal bruke FBO, hvilken color buffer tilhørende FBOen som vi skal bruke
        // I dette tilfellet nr 0
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        return frameBuffer;
    }
 
    //Lager en color buffer texture attachment og legger den til FBOen som er bundet
    private int createTextureAttachment( int width, int height) {
    	//Lager navnet til texturen 
        int texture = GL11.glGenTextures();
        //Binder texturen 
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        //Binder texturen 
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        //legger til texture attachment til FBOen
        //Vi bruker ikke noe mipmap så vi setter 0 på mipmap level
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                texture, 0);
        return texture;
    }
  //Lager en depth buffer texture attachment og legger den til FBOen som er bundet
    private int createDepthTextureAttachment(int width, int height){
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
                0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        //Indikerer at vi skal legge til en depth attachment og ikke en color attachment
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                texture, 0);
        return texture;
    }
 
    //Legge til en depth buffer som IKKE er en texture AKA render buffers
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        //Legger til i frame bufferen som depth-attachment
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        //Legger til i frame bufferen som depth-attachment
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
 
}
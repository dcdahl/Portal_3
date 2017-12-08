package portal;
 
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
 
public class PortalFrameBuffers {
 
	//Set the resolution of the refelction texture
    protected static final int REFLECTION_WIDTH = 1280;
    private static final int REFLECTION_HEIGHT = 720;
 
    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
 
    public PortalFrameBuffers() {//call when loading the game
        initialiseReflectionFrameBuffer();
    }
 
    public void cleanUp() {//call when closing the game
        GL30.glDeleteFramebuffers(reflectionFrameBuffer);
        GL11.glDeleteTextures(reflectionTexture);
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
    }
 
    public void bindReflectionFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }
     
     
    //Call this to unbind the currently bound FBO, will then start rendering to the default frame buffer again
    public void unbindCurrentFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
 
    public int getReflectionTexture() {//get the resulting texture
        return reflectionTexture;
    }

    private void initialiseReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }
     
    /**
     * This will tell OpenGL to render to a FBO instead of the default frame buffer.
     * @param frameBuffer Which FBO to render to 
     * @param width
     * @param height
     */
    private void bindFrameBuffer(int frameBuffer, int width, int height){
    	//Making sure the texture is NOT bound
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //Change the resolution of the viewport to the resolution of the FBO
        GL11.glViewport(0, 0, width, height);
    }
 
    /**
     * Creates a new frame buffer
     * @return The id of the created frame buffer
     */
    private int createFrameBuffer() {
    	//Id of a new frame buffer
        int frameBuffer = GL30.glGenFramebuffers();
        //Bind the frame buffer so that we can use it
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //Which color buffer attachement to render to, in this case we will use attchment 0
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        return frameBuffer;
    }
 
    /**
     * Adds a color buffer texture attachment to the currently bound FBO
     * @param width Texture width
     * @param height Texture height
     * @return Id of the bound texture
     */
    private int createTextureAttachment( int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
       //Adds the textureAttachment to the currently bound FBO
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                texture, 0);
        return texture;
    }
 
    /**
     * Creates a depthbuffer attachment that is NOT a texture
     * @param width Texture width
     * @param height Texture Height
     * @return Id of the renderbuffer
     */
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        //Adds it to the FBO as a depth attachment
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
 
}
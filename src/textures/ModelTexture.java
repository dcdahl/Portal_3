package textures;

//Denne klassen er hentet direkte fra ThinMatrix
public class ModelTexture {

	private int textureID;
	
	private float shineDamper =1;
	private float reflectivity = 0;
	

	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}


	
	
	public float getShineDamper() {
		return shineDamper;
	}


	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}


	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	
	public int getTextureID() {
		return textureID;
	}
	
	
}

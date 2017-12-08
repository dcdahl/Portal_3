package dataStructures;

/**
 * 
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * Contains the extracted data for an animated model, which includes the mesh data, and skeleton (joints heirarchy) data.
 * @author Karl
 *
 */
public class AnimatedModelData {

	private final SkeletonData joints;
	private final MeshData mesh;
	
	public AnimatedModelData(MeshData mesh, SkeletonData joints){
		this.joints = joints;
		this.mesh = mesh;
	}
	
	public SkeletonData getJointsData(){
		return joints;
	}
	
	public MeshData getMeshData(){
		return mesh;
	}
	
}

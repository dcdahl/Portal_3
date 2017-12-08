package dataStructures;

import java.util.List;


/**
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * Inneholder data om hvilken rekkefølge leddene skal legges til i skjelettet og hvordan meshen skal legges utenpå leddet
 *
 */
public class SkinningData {
	
	public final List<String> jointOrder;
	public final List<VertexSkinData> verticesSkinData;
	
	public SkinningData(List<String> jointOrder, List<VertexSkinData> verticesSkinData){
		this.jointOrder = jointOrder;
		this.verticesSkinData = verticesSkinData;
	}


}

package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class Options implements Serializable {

	private boolean thumbMaps;

	private int maxResults;

	private boolean ignoreLatLngInput;

	public void setThumbMaps(boolean thumbMaps){
		this.thumbMaps = thumbMaps;
	}

	public boolean isThumbMaps(){
		return thumbMaps;
	}

	public void setMaxResults(int maxResults){
		this.maxResults = maxResults;
	}

	public int getMaxResults(){
		return maxResults;
	}

	public void setIgnoreLatLngInput(boolean ignoreLatLngInput){
		this.ignoreLatLngInput = ignoreLatLngInput;
	}

	public boolean isIgnoreLatLngInput(){
		return ignoreLatLngInput;
	}

	@Override
 	public String toString(){
		return 
			"Options{" + 
			"thumbMaps = '" + thumbMaps + '\'' + 
			",maxResults = '" + maxResults + '\'' + 
			",ignoreLatLngInput = '" + ignoreLatLngInput + '\'' + 
			"}";
		}
}
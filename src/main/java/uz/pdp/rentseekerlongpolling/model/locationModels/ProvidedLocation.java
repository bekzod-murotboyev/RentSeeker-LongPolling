package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class ProvidedLocation implements Serializable {

	private LatLng latLng;

	public void setLatLng(LatLng latLng){
		this.latLng = latLng;
	}

	public LatLng getLatLng(){
		return latLng;
	}

	@Override
 	public String toString(){
		return 
			"ProvidedLocation{" + 
			"latLng = '" + latLng + '\'' + 
			"}";
		}
}
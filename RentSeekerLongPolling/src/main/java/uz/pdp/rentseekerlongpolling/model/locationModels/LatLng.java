package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.asif.gsonpojogenerator")
public class LatLng implements Serializable {

	private double lng;

	private double lat;

	public void setLng(double lng){
		this.lng = lng;
	}

	public double getLng(){
		return lng;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"LatLng{" + 
			"lng = '" + lng + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}
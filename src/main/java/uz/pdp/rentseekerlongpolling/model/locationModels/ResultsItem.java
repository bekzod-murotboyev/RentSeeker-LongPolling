package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;

@Generated("com.asif.gsonpojogenerator")
public class ResultsItem implements Serializable {

	private List<LocationsItem> locations;

	private ProvidedLocation providedLocation;

	public void setLocations(List<LocationsItem> locations){
		this.locations = locations;
	}

	public List<LocationsItem> getLocations(){
		return locations;
	}

	public void setProvidedLocation(ProvidedLocation providedLocation){
		this.providedLocation = providedLocation;
	}

	public ProvidedLocation getProvidedLocation(){
		return providedLocation;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"locations = '" + locations + '\'' + 
			",providedLocation = '" + providedLocation + '\'' + 
			"}";
		}
}
package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;

@Generated("com.asif.gsonpojogenerator")
public class MapQuest implements Serializable {

	private Options options;

	private List<ResultsItem> results;

	private Info info;

	public void setOptions(Options options){
		this.options = options;
	}

	public Options getOptions(){
		return options;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setInfo(Info info){
		this.info = info;
	}

	public Info getInfo(){
		return info;
	}

	@Override
 	public String toString(){
		return 
			"MapQuest{" + 
			"options = '" + options + '\'' + 
			",results = '" + results + '\'' + 
			",info = '" + info + '\'' + 
			"}";
		}
}
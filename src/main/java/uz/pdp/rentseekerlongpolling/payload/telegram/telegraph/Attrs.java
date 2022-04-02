package uz.pdp.rentseekerlongpolling.payload.telegram.telegraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attrs{

	@JsonProperty("src")
	private String src;

	public String getSrc(){
		return src;
	}
}
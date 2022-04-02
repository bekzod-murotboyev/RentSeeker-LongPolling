package uz.pdp.rentseekerlongpolling.payload.telegram.telegraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentItem{

	@JsonProperty("tag")
	private String tag;

	@JsonProperty("attrs")
	private Attrs attrs;

	public String getTag(){
		return tag;
	}

	public Attrs getAttrs(){
		return attrs;
	}
}
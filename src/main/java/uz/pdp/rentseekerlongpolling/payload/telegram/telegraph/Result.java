package uz.pdp.rentseekerlongpolling.payload.telegram.telegraph;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Result{

	@JsonProperty("path")
	private String path;

	@JsonProperty("description")
	private String description;

	@JsonProperty("can_edit")
	private boolean canEdit;

	@JsonProperty("title")
	private String title;

	@JsonProperty("url")
	private String url;

	@JsonProperty("content")
	private List<ContentItem> content;

	@JsonProperty("views")
	private int views;

	public String getPath(){
		return path;
	}

	public String getDescription(){
		return description;
	}

	public boolean isCanEdit(){
		return canEdit;
	}

	public String getTitle(){
		return title;
	}

	public String getUrl(){
		return url;
	}

	public List<ContentItem> getContent(){
		return content;
	}

	public int getViews(){
		return views;
	}
}
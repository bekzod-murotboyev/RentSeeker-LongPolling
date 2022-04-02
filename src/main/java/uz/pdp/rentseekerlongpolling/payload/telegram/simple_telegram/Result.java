package uz.pdp.rentseekerlongpolling.payload.telegram.simple_telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result{

	@JsonProperty("file_path")
	private String filePath;

	@JsonProperty("file_unique_id")
	private String fileUniqueId;

	@JsonProperty("file_id")
	private String fileId;

	@JsonProperty("file_size")
	private int fileSize;

	public String getFilePath(){
		return filePath;
	}

	public String getFileUniqueId(){
		return fileUniqueId;
	}

	public String getFileId(){
		return fileId;
	}

	public int getFileSize(){
		return fileSize;
	}
}
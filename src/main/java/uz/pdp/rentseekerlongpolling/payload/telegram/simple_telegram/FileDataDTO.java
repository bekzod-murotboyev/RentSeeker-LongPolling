package uz.pdp.rentseekerlongpolling.payload.telegram.simple_telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDataDTO{

	@JsonProperty("result")
	private Result result;

	@JsonProperty("ok")
	private boolean ok;

	public Result getResult(){
		return result;
	}

	public boolean isOk(){
		return ok;
	}
}
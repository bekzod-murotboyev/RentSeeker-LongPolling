package uz.pdp.rentseekerlongpolling.payload.telegram.telegraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatedPageDTO{

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
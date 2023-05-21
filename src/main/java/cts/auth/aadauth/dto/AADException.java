package cts.auth.aadauth.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AADException{

	@JsonProperty("trace_id")
	private String traceId;

	@JsonProperty("error_description")
	private String errorDescription;

	@JsonProperty("correlation_id")
	private String correlationId;

	@JsonProperty("error_codes")
	private List<Integer> errorCodes;

	@JsonProperty("error")
	private String error;

	@JsonProperty("error_uri")
	private String errorUri;

	@JsonProperty("timestamp")
	private String timestamp;

}
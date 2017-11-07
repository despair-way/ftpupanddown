package com.jiurong.autotransfer.config;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransferConfig {

	private SourceConfig source;
	private SourceConfig target;
	public SourceConfig getSource() {
		return source;
	}
	public void setSource(SourceConfig source) {
		this.source = source;
	}
	public SourceConfig getTarget() {
		return target;
	}
	public void setTarget(SourceConfig target) {
		this.target = target;
	}
	
	@Test
	public void getJsonFile() throws JsonParseException, JsonMappingException, IOException {
		InputStream content = TransferConfig.class.getClassLoader().getResourceAsStream("app.json");
		ObjectMapper objectMapper = new ObjectMapper();
		TransferConfig config = objectMapper.readValue(content, TransferConfig.class);
		System.out.println(config.getSource().getFilename());
	}
}

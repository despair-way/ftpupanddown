package com.jiurong.autotransfer;


import java.io.File;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiurong.autotransfer.config.TransferConfig;
public class test {

	
	public static void main(String[] args) throws Exception {
//		File file = new File("C:/download/upload/test.txt");
//		System.out.println(file.getName());
//		System.out.println(file.getParent());
//
//		file.getName();
//		file.getParent();
//		
//		
//		
		InputStream content = TransferConfig.class.getClassLoader().getResourceAsStream("app.json");
		ObjectMapper objectMapper = new ObjectMapper();
		TransferConfig config = objectMapper.readValue(content, TransferConfig.class);
//		System.out.println(config.getSource());
		File localfile = null;
		FTPUpAndDown.ftpUp(config.getSource().getUsername(),config.getSource().getPassword(),config.getSource().getIp(),config.getSource().getPort(),config.getSource().getFilepath(),localfile);
	}
}


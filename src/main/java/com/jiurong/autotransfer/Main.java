/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;

import java.io.File;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiurong.autotransfer.config.TransferConfig;

public class Main {

	public static void main(String[] args) throws Exception {
		InputStream content = TransferConfig.class.getClassLoader().getResourceAsStream("app.json");
		ObjectMapper objectMapper = new ObjectMapper();
		TransferConfig config = objectMapper.readValue(content, TransferConfig.class);
		File localfile = null;
		if (config.getSource().getType().equals("FTP")) {
			localfile = FTPUpAndDown.ftpDown(config.getTarget().getUsername(), config.getTarget().getPassword(),
					config.getTarget().getIp(), config.getTarget().getPort(), config.getTarget().getFilepath());
		} else {
			localfile = FTPUpAndDown.sftpDown(config.getTarget().getUsername(), config.getTarget().getPassword(),
					config.getTarget().getIp(), config.getTarget().getPort(), config.getTarget().getFilepath());
		}
		if (config.getSource().getType().equals("FTP")) {
			FTPUpAndDown.ftpUp(config.getSource().getUsername(), config.getSource().getPassword(),
					config.getSource().getIp(), config.getSource().getPort(), config.getSource().getFilepath(),
					localfile);
		} else {
			FTPUpAndDown.sftpUp(config.getSource().getUsername(), config.getSource().getPassword(),
					config.getSource().getIp(), config.getSource().getPort(), config.getSource().getFilepath(),
					localfile);
		}
	}
}

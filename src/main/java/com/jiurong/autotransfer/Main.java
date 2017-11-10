/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiurong.autotransfer.config.SourceConfig;
import com.jiurong.autotransfer.config.TargetConfig;
import com.jiurong.autotransfer.config.TransferConfig;

public class Main {

	public static void main(String[] args) throws Exception {
		InputStream content = TransferConfig.class.getClassLoader().getResourceAsStream("app.json");
		ObjectMapper objectMapper = new ObjectMapper();
		List<TransferConfig> list = objectMapper.readValue(content, new TypeReference<List<TransferConfig>>() {
		});
		File localfile = null;
		for (TransferConfig transferConfig : list) {
			SourceConfig source = transferConfig.getSource();
			TargetConfig target = transferConfig.getTarget();

			if (source.getType().equals("FTP")) {
				localfile = FTPUpAndDown.ftpDown(source.getUsername(), source.getPassword(), source.getIp(),
						source.getPort(), source.getFilepath());
			} else {
				localfile = FTPUpAndDown.sftpDown(source.getUsername(), source.getPassword(), source.getIp(),
						source.getPort(), source.getFilepath());
			}
			if (target.getType().equals("FTP")) {
				FTPUpAndDown.ftpUp(target.getUsername(), target.getPassword(), target.getIp(), target.getPort(),
						target.getFilepath(), localfile);
			} else {
				FTPUpAndDown.sftpUp(target.getUsername(), target.getPassword(), target.getIp(), target.getPort(),
						target.getFilepath(), localfile);

				localfile.deleteOnExit();

			}
		}
	}
}

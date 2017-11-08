/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiurong.autotransfer.config.TransferConfig;

public class Main {

	public static void main(String[] args) throws Exception {
		InputStream content = TransferConfig.class.getClassLoader().getResourceAsStream("app.json");
		ObjectMapper objectMapper = new ObjectMapper();
		TransferConfig config = objectMapper.readValue(content, TransferConfig.class);
		if (config.getSource().getType().equals("FTP")) {
			FTPUpAndDown.ftpDown(FTPUpAndDown.downInfo(config));
		}else {
			FTPUpAndDown.sftpDown(FTPUpAndDown.downInfo(config));
		}
		if (config.getSource().getType().equals("FTP")) {
			FTPUpAndDown.ftpUp(FTPUpAndDown.upInfo(config));
		}else {
			FTPUpAndDown.sftpUp(FTPUpAndDown.upInfo(config));		
		}
		
		
		
		
		FTPUpAndDown.ftpDown("remote", "Jiurong20151009", "101.200.242.103", 21, "Downloadtest.txt", "F:/test/DownLoad.txt");
		FTPUpAndDown.sftpUp("jiurong", "Jr88362624", "192.168.1.20", 32022, "test","upload.txt", "F:/test/DownLoad.txt");
		System.out.println("success");
	}
}

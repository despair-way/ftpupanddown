/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;

public class Main {

	public static void main(String[] args) throws Exception {
		FTPUpAndDown.ftpDown("remote", "Jiurong20151009", "101.200.242.103", 21, "Downloadtest.txt", "F:/test/DownLoad.txt");
		FTPUpAndDown.sftpUp("jiurong", "Jr88362624", "192.168.1.20", 32022, "test","upload.txt", "F:/test/DownLoad.txt");
		System.out.println("success");
	}
}

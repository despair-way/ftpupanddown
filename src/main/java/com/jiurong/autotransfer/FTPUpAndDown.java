/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 提供ftp和sfpt上传下载功能
 * 
 * @author zyz
 * @since 1.0
 */
public class FTPUpAndDown {

	/**
	 * 
	 * @param username
	 * @param password
	 * @param ip
	 * @param port
	 * @param uploadurl
	 * @param localurl
	 * @param filename
	 * @throws Exception
	 */
	public static void sftpUp(String username, String password, String ip, int port, String uploadurl, String localurl,
			String filename) throws Exception {
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(username, ip, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect(30000);
		try {
			channel = session.openChannel("sftp");
			channel.connect(30000);
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.cd(uploadurl);
			sftp.put(new FileInputStream(new File(localurl)), filename);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}

	public static void sftpDown(String username, String password, String ip, int port, String downloadurl,
			String localurl, String filename) {
		Channel channel = null;
		Session session = null;
		JSch jsch = new JSch();

		try {
			session = jsch.getSession(username, ip, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StricHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect(30000);
			channel = session.openChannel("sftp");
			channel.connect(30000);
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.get(downloadurl, new FileOutputStream(localurl));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}

	public static void ftpUp(String username, String password, String ip, int port, String filename, String loaclfile,
			String path) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileInputStream input = null;

		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			input = new FileInputStream(new File(loaclfile));
			ftpClient.changeWorkingDirectory(path);
			ftpClient.storeFile(filename, input);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(filename, input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void ftpDown(String username, String password, String ip, int port, String filename, String loaclfile)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;

		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			String remoteFlieName = filename;
			File localFile = new File(loaclfile);
			if (localFile.exists()) {
				localFile.delete();
			}
			localFile.createNewFile();
			fos = new FileOutputStream(loaclfile);

			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remoteFlieName, fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

	}

}

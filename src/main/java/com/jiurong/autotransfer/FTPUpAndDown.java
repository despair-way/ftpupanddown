/*
 * Copyright 2016-2018 the original author or authors.
 *
 * All right reserved by JiurongTech
 */

package com.jiurong.autotransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
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

	/*
	 * @param username sftp服务器的用户名
	 * 
	 * @param password sftp服务器的密码
	 * 
	 * @param ip sftp服务器的访问ip
	 * 
	 * @param port sftp服务的端口号
	 * 
	 * @param filepath 文件上传之后的FullPath
	 * 
	 * @param localfile 本地文件的File对象
	 * 
	 * @throws FileNotFoundException，IOException，JSchException 用户名、密码、ip、端口号不正确
	 */

	public static void sftpUp(String username, String password, String ip, int port, String filepath, File localfile)
			throws Exception {
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		FileInputStream inStream = null;
		try {
			session = jsch.getSession(username, ip, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
			File file = new File(filepath);
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.cd(file.getParent());
			inStream = new FileInputStream(localfile);
			;
			sftp.put(inStream, FilenameUtils.getName(filepath));
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (session != null) {
				session.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}

		}
	}

	/*
	 * 
	 * @param username sftp服务器的用户名
	 * 
	 * @param password sftp服务器的密码
	 * 
	 * @param ip sftp服务器的访问ip
	 * 
	 * @param port sftp服务的端口号
	 * 
	 * @param filepath 文件上传之后的FullPath
	 * 
	 * @return file 返回下载到本地的File对象
	 */

	public static File sftpDown(String username, String password, String ip, int port, String filepath)
			throws Exception {
		Channel channel = null;
		Session session = null;
		JSch jsch = new JSch();
		FileOutputStream outStream = null;
		File localfile = null;
		try {
			session = jsch.getSession(username, ip, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			File file = new File(filepath);
			String tempfile = file.getName();
			String extension = tempfile.substring(tempfile.indexOf('.')); // 文件后缀名
			String basename = FilenameUtils.getBaseName(tempfile); // 文件名（不加后缀）
			localfile = File.createTempFile(basename, extension);
			outStream = new FileOutputStream(localfile);
			sftp.get(filepath, outStream);

		} finally {
			if (outStream != null) {
				outStream.close();
			}

		}
		if (session != null) {
			session.disconnect();
		}
		if (channel != null) {
			channel.disconnect();
		}
		return localfile;

	}

	/*
	 * 
	 * @param username sftp服务器的用户名
	 * 
	 * @param password sftp服务器的密码
	 * 
	 * @param ip sftp服务器的访问ip
	 * 
	 * @param port sftp服务的端口号
	 * 
	 * @param filepath 文件上传之后的FullPath
	 * 
	 * @param localfile 本地文件的File对象
	 * 
	 * @throws SocketException,IOException ，FileNotFoundException
	 */
	public static void ftpUp(String username, String password, String ip, int port, String filepath, File localfile)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileInputStream inStream = null;
		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			inStream = new FileInputStream(localfile);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(filepath, inStream);
		} finally {

			if (inStream != null) {
				inStream.close();
			}
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
		}
	}

	/*
	 * 
	 * @param username sftp服务器的用户名
	 * 
	 * @param password sftp服务器的密码
	 * 
	 * @param ip sftp服务器的访问ip
	 * 
	 * @param port sftp服务的端口号
	 * 
	 * @param filepath 文件上传之后的FullPath
	 * 
	 * @return file 返回下载到本地的File对象
	 * 
	 * @throws SocketException,IOException ，FileNotFoundException
	 */
	public static File ftpDown(String username, String password, String ip, int port, String filepath)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream outSteam = null;
		File localfile = null;
		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			File file = new File(filepath);
			String tempfile = file.getName();
			String extension = tempfile.substring(tempfile.indexOf('.'));// 文件后缀名
			String basename = FilenameUtils.getBaseName(tempfile); // 文件名（不加后缀）
			localfile = File.createTempFile(basename, extension);
			outSteam = new FileOutputStream(localfile);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(filepath, outSteam);
			outSteam.flush();
		} finally {
			if (outSteam != null) {
				outSteam.close();
			}
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
		}
		return localfile;
	}
}

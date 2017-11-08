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

import org.apache.commons.net.ftp.FTPClient;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jiurong.autotransfer.config.TransferConfig;


/**
 * 提供ftp和sfpt上传下载功能
 * 
 * @author zyz
 * @since 1.0
 */
public class FTPUpAndDown {

	public static  UpInfo UpInfo(TransferConfig transferconfig) {
		UpInfo upinfo = new UpInfo();
		upinfo.setUsername(transferconfig.getTarget().getUsername());
		upinfo.setPassword(transferconfig.getTarget().getPassword());
		upinfo.setIp(transferconfig.getTarget().getIp());
		upinfo.setPort(transferconfig.getTarget().getPort());
		upinfo.setFilepath(transferconfig.getTarget().getFilepath());
		return upinfo;
	}


	public static  DownInfo DownInfo(TransferConfig transferconfig) {
		DownInfo downinfo = new DownInfo();
		downinfo.setUsername(transferconfig.getSource().getUsername());
		downinfo.setPassword(transferconfig.getSource().getPassword());
		downinfo.setIp(transferconfig.getSource().getIp());
		downinfo.setPort(transferconfig.getSource().getPort());
		downinfo.setFilepath(transferconfig.getSource().getFilepath());
		return downinfo;
	}

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
	public static void sftpUp(String username, String password, String ip, int port, String uploadurl, String filename,
			String localurl) throws Exception {
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(username, ip, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect();
		try {
			channel = session.openChannel("sftp");
			channel.connect();
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
	
	public static void sftpUp(UpInfo upinfo) throws Exception {
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(upinfo.getUsername(), upinfo.getIp(), upinfo.getPort());
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(upinfo.getPassword());
		session.connect();
		File file = new File(upinfo.getFilepath());
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.cd(file.getParent());
			sftp.put(new FileInputStream(new File("F:/test/"+file.getName())), file.getName());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}


	public static void sftpDown(DownInfo downinfo) {
		Channel channel = null;
		Session session = null;
		JSch jsch = new JSch();
		File file = new File(downinfo.getFilepath());
		try {
			session = jsch.getSession(downinfo.getUsername(), downinfo.getIp(), downinfo.getPort());
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(downinfo.getPassword());
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.get(downinfo.getFilepath(), new FileOutputStream("F:/test/"+file.getName()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param ip
	 * @param port
	 * @param filename
	 * @param loaclfile
	 * @param path
	 * @throws Exception
	 */
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
	public static void ftpUp(UpInfo upinfo) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileInputStream input = null;
		File file = new File(upinfo.getFilepath());
		try {
			ftpClient.connect(upinfo.getIp(), upinfo.getPort());
			ftpClient.login(upinfo.getUsername(), upinfo.getPassword());
			input = new FileInputStream(new File("F:/test/"+file.getName()));
			ftpClient.changeWorkingDirectory(file.getParent());
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(upinfo.getFilepath(), input);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		    try {
		        if (input != null) {
		        	input.close();
		        }
		        ftpClient.disconnect();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}              
	/**
	 * 
	 * @param username
	 * @param password
	 * @param ip
	 * @param port
	 * @param filename
	 * @param loaclfile
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @param info
	 * @throws Exception
	 */
	public static void ftpDown(DownInfo downinfo) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;

		try {
			ftpClient.connect(downinfo.getIp(), downinfo.getPort());
			ftpClient.login(downinfo.getUsername(), downinfo.getPassword());
			String remoteFlieName = downinfo.getFilepath();
			File file = new File(downinfo.getFilepath());
			File localFile = new File("F:/test/"+file.getName());
			if (localFile.exists()) {
				localFile.delete();
			}
			localFile.createNewFile();
			fos = new FileOutputStream("F:/test/"+file.getName());

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

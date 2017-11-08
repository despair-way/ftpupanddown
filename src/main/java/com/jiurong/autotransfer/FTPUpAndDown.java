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
import com.jiurong.autotransfer.config.TransferConfig;


/**
 * 提供ftp和sfpt上传下载功能
 * 
 * @author zyz
 * @since 1.0
 */
public class FTPUpAndDown {

	public static  UpInfo upInfo(TransferConfig transferconfig) {
		UpInfo upinfo = new UpInfo();
		upinfo.setUsername(transferconfig.getTarget().getUsername());
		upinfo.setPassword(transferconfig.getTarget().getPassword());
		upinfo.setIp(transferconfig.getTarget().getIp());
		upinfo.setPort(transferconfig.getTarget().getPort());
		upinfo.setFilepath(transferconfig.getTarget().getFilepath());
		return upinfo;
	}


	public static  DownInfo downInfo(TransferConfig transferconfig) {
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
 * @param filepath
 * @param localfile
 * @throws Exception
 */
	public static void sftpUp(String username, String password, String ip, int port, String filepath,
			File localfile) throws Exception {
			Session session = null;
			Channel channel = null;
			JSch jsch = new JSch();
			session = jsch.getSession(username, ip,port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
			File file = new File(filepath);
			try {
				channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;
				sftp.cd(file.getParent());
				sftp.put(new FileInputStream(localfile), localfile.getName());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.disconnect();
				channel.disconnect();
		    	localfile.deleteOnExit();
			}
		}
	
	public static void sftpUp(UpInfo upinfo,File localfile) throws Exception {
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
			sftp.put(new FileInputStream(localfile), localfile.getName());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
	    	localfile.deleteOnExit();
		}
	}


	public static File sftpDown(String username,String password,String ip,int port,String filepath) {
		Channel channel = null;
		Session session = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(username,ip,port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			File file =  new File(filepath);
			String tempfile = file.getName();
			String extension = FilenameUtils.getExtension(tempfile); //文件后缀名
			String basename = FilenameUtils.getBaseName(tempfile);  //文件名（不加后缀）
			File localfile = File.createTempFile(basename, extension);
			sftp.get(filepath, new FileOutputStream(localfile));
			return localfile;
		} catch (Exception e) {
			e.printStackTrace();
			File file = null;
			return file;
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
	public static void ftpUp(String username,String password,String ip,int port,String filepath,File localfile) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileInputStream input = null;
		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			input = new FileInputStream(localfile);
		//	ftpClient.changeWorkingDirectory(localfile.getParent());
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(filepath, input);
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
		    } finally {
		    	localfile.deleteOnExit();
			}
		}
	}  
	public static void ftpUp(UpInfo upinfo,File localfile) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileInputStream input = null;
		try {
			ftpClient.connect(upinfo.getIp(), upinfo.getPort());
			ftpClient.login(upinfo.getUsername(), upinfo.getPassword());
			input = new FileInputStream(localfile);
		//	ftpClient.changeWorkingDirectory(localfile.getParent());
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
		    } finally {
		    	localfile.deleteOnExit();
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
	public static File ftpDown(String username,String password,String ip,int port,String filepath) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;

		try {
			ftpClient.connect(ip, port);
			ftpClient.login(username, password);
			File file =  new File(filepath);
			String tempfile = file.getName();
			String extension = FilenameUtils.getExtension(tempfile); //文件后缀名
			String basename = FilenameUtils.getBaseName(tempfile);  //文件名（不加后缀）
			File localfile = File.createTempFile(basename, extension);
			//判断本地是否有同名文件进行删除处理
			if (localfile.exists()) {
				localfile.delete();
			}
			localfile.createNewFile();
			fos = new FileOutputStream(localfile);

			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(filepath, fos);
			fos.flush();
			return localfile;
		}  finally {
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
	public static File ftpDown(DownInfo downinfo) throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;

		try {
			ftpClient.connect(downinfo.getIp(), downinfo.getPort());
			ftpClient.login(downinfo.getUsername(), downinfo.getPassword());
			File file =  new File(downinfo.getFilepath());
			String tempfile = file.getName();
			String extension = FilenameUtils.getExtension(tempfile); //文件后缀名
			String basename = FilenameUtils.getBaseName(tempfile);  //文件名（不加后缀）
			File localfile = File.createTempFile(basename, extension);
			//判断本地是否有同名文件进行删除处理
			if (localfile.exists()) {
				localfile.delete();
			}
			localfile.createNewFile();
			fos = new FileOutputStream(localfile);

			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(downinfo.getFilepath(), fos);
			fos.flush();
			return localfile;
		}  finally {
			if (fos != null) {
				fos.close();
			}	
		}

	}
}

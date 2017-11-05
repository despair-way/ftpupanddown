package com.jiurong.autotransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.commons.net.ftp.FTPClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FTPUpAndDown{
	public static void sftpUp(String username,String password,String ip,int port,String uploadurl,String localurl,String filename) throws Exception{
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(username, ip, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StricHostKeyChecking","no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect(30000);
		
		try {
			channel = (Channel) session.openChannel("sftp");
			channel.connect(30000);
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.cd(uploadurl);
			sftp.put(new FileInputStream(new File(localurl)),filename);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			session.disconnect();
			channel.disconnect();
		}
	}
	
	public static void sftpDown(String username,String password,String ip,int port,String downloadurl,String localurl,String filename) throws Exception{
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		session = jsch.getSession(username, ip, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StricHostKeyChecking","no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect(30000);
		
		try {
			channel = (Channel) session.openChannel("sftp");
			channel.connect(30000);
			ChannelSftp sftp = (ChannelSftp) channel;
			sftp.get(downloadurl,new FileOutputStream(localurl));

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			session.disconnect();
			channel.disconnect();
		}
	}
	
	public static void ftpDown(String username,String password,String ip,int port,String filename,String loaclfile) throws Exception{
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;
		
		try {
			ftpClient.connect(ip,port);
			ftpClient.login(username, password);
			String remoteFlieName = "/"+filename;
			File localFile = new File(loaclfile);
			localFile.createNewFile();
			fos = new FileOutputStream(loaclfile);
			
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.retrieveFile(remoteFlieName, fos);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}

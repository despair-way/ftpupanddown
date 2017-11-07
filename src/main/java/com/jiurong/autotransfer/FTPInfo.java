package com.jiurong.autotransfer;

public class FTPInfo {
	private String username;
	private String password;
	private String ip;
	private int port;
	private String filename;
	private String loaclfile;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getLoaclfile() {
		return loaclfile;
	}

	public void setLoaclfile(String loaclfile) {
		this.loaclfile = loaclfile;
	}
}

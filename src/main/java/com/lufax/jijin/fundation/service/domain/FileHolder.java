package com.lufax.jijin.fundation.service.domain;

/**
 * 基金对账文件
 * @author xuneng
 *
 */
public class FileHolder {

	private String fileAbsolutePath;
	private String fileName;
	
	
	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}
	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

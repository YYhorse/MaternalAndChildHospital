package com.example.maternalandchildhospital.bean;

public class SystemInfo {

	String newSystemVersion = "";
	int forceUpgrade = 0;
	String downloadUrl = "";
	String description = "";
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNewSystemVersion() {
		return newSystemVersion;
	}
	public void setNewSystemVersion(String newSystemVersion) {
		this.newSystemVersion = newSystemVersion;
	}
	public int getForceUpgrade() {
		return forceUpgrade;
	}
	public void setForceUpgrade(int forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
	}

	
}

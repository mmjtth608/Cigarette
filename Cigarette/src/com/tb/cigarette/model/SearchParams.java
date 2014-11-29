package com.tb.cigarette.model;

import java.io.Serializable;

public class SearchParams implements Serializable {
	private String key = null;
	private String pinpai = null;
	private String dangci = null;
	private String chandi = null;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPinpai() {
		return pinpai;
	}

	public void setPinpai(String pinpai) {
		this.pinpai = pinpai;
	}

	public String getDangci() {
		return dangci;
	}

	public void setDangci(String dangci) {
		this.dangci = dangci;
	}

	public String getChandi() {
		return chandi;
	}

	public void setChandi(String chandi) {
		this.chandi = chandi;
	}

	public void clearParams() {
		key = null;
		pinpai = null;
		chandi = null;
		dangci = null;
	}

}
package com.lufax.jijin.fundation.remote.gson.request;

import java.util.List;

public class UploadResultGson {
	
	private String channelId;
	private String instructionNo;
	private List<DetailResultList> detailResultList;


	public UploadResultGson(String channelId, String instructionNo, List<DetailResultList> detailResultList){
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.detailResultList = detailResultList;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getInstructionNo() {
		return instructionNo;
	}
	public void setInstructionNo(String instructionNo) {
		this.instructionNo = instructionNo;
	}
	
	public List<DetailResultList> getDetailResultList() {
		return detailResultList;
	}

	public void setDetailResultList(List<DetailResultList> detailResultList) {
		this.detailResultList = detailResultList;
	}
	

}

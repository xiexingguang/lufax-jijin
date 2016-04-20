package com.lufax.jijin.daixiao.gson;

import java.util.List;

import com.lufax.jijin.daixiao.dto.JijinExMaPerfDTO;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;


public class JijinExManagerGson {

    private String fundCode; //基金代码
    private String manager;//基金经理
    private String resume;// 基金经理简介
    
    private String maId;//基金经理ID
    
    private String infoPubDate; //基金经理个人信息公告日期
    private String gender; //性别
    private String education; //学历
    private String appointDate; //任职日期
    
    private List<JijinExMaPerfGson> managerPerfs;
    

    public JijinExManagerGson(JijinExManagerDTO jijinExManagerDTO) {
        this.fundCode = jijinExManagerDTO.getFundCode();
        this.manager = jijinExManagerDTO.getManager();
        this.resume = jijinExManagerDTO.getResume();
        this.maId = jijinExManagerDTO.getManagerId();
        this.infoPubDate = jijinExManagerDTO.getPubDate();
        this.gender= jijinExManagerDTO.getGender();
        this.education = jijinExManagerDTO.getEducation();
        this.appointDate = jijinExManagerDTO.getAppointDate();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

	public String getMaId() {
		return maId;
	}

	public String getInfoPubDate() {
		return infoPubDate;
	}

	public String getGender() {
		return gender;
	}

	public String getEducation() {
		return education;
	}

	public String getAppointDate() {
		return appointDate;
	}

	public void setMaId(String maId) {
		this.maId = maId;
	}

	public void setInfoPubDate(String infoPubDate) {
		this.infoPubDate = infoPubDate;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setAppointDate(String appointDate) {
		this.appointDate = appointDate;
	}

	public List<JijinExMaPerfGson> getManagerPerfs() {
		return managerPerfs;
	}

	public void setManagerPerfs(List<JijinExMaPerfGson> managerPerfs) {
		this.managerPerfs = managerPerfs;
	}


    
}






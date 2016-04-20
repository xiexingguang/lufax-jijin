package com.lufax.jijin.ylx.remote;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.YLXGWInterfaceCaller;
import com.lufax.jijin.sysFacade.gson.YLXResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class YlxGatewayRemoteService {
	private static final String PULL_URL = "/service/ylx/op-confirm";
	private static final String SEND_URL = "/service/ylx/request";
	@Autowired
	private YLXGWInterfaceCaller gwCaller;
	@Resource
	private JijinAppProperties jijinAppProperties;

	public YLXResponse pullFile(String fileName) {
		Logger.info(this, "pull file:" + fileName);
		checkAndMkdir(jijinAppProperties.getYlxResponseRootDir());
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(PULL_URL).addFormValue("fileName", fileName);
		YLXResponse resp = gwCaller.post(interfaceCallObject, YLXResponse.class);
		Logger.info(this, "pull file " + fileName + " result:" + resp.getReturnCode());
		return resp;
	}

	public YLXResponse sendFile(String fileName) {
		Logger.info(this, "send file:" + fileName);
		checkAndMkdir(jijinAppProperties.getYlxRequestRootDir());
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(SEND_URL).addFormValue("fileName", fileName);
		YLXResponse resp = gwCaller.post(interfaceCallObject, YLXResponse.class);
		Logger.info(this, "send file " + fileName + " result:" + resp.getReturnCode());
		return resp;

	}

	private void checkAndMkdir(String dir) {
		File pullDir = new File(dir);
		if (!pullDir.exists() || !pullDir.isDirectory()) {
			pullDir.mkdirs();
		}
	}
}

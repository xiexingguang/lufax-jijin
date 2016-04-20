package com.lufax.jijin.daixiao.remote;

import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.remote.caller.JijinExGWInterfaceCaller;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
@Service
public class JijinExGatewayRemoteService {

    private static final String PULL_FILE_URL = "/service/file/wind/pull";

    @Resource
    private JijinAppProperties appProperties;
    @Autowired
    private JijinExGWInterfaceCaller gwCaller;

    public GWResponseGson pullFile(String fileName) {
        GWResponseGson resp = null;
        try {
            Logger.info(this, "pull file:" + fileName);
            checkAndMkdir(appProperties.getJijinNasRootDir());
            InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(PULL_FILE_URL).addFormValue("fileName", fileName);
            resp = gwCaller.post(interfaceCallObject, GWResponseGson.class);
            Logger.info(this, "pull file " + fileName + " result:" + resp.getRetCode());
        } catch (Exception e) {
            resp.setRetCode("999");
            resp.setRetMessage("pullFile exception");
            Logger.error(this, String.format("pullFile [%s] exception is [%s]", fileName, e.getMessage()));
            e.printStackTrace();
        }
        return resp;
    }

    private void checkAndMkdir(String dir) {
        File pullDir = new File(dir);
        if (!pullDir.exists() || !pullDir.isDirectory()) {
            pullDir.mkdirs();
        }
    }
}

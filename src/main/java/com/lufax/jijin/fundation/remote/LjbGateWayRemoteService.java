/**
 *
 */
package com.lufax.jijin.fundation.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.PAFGWInterfaceCaller;
import com.sun.jersey.api.representation.Form;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 2:04:02 PM
 * 
 * this is LJB gateway
 */
@Service
public class LjbGateWayRemoteService {

    private static final String REGISTER_URL = "/service/paf/fundation/regaccount";
    private static final String PAY_URL = "/service/paf/fundation/pay";
    private static final String CANCEL_URL = "/service/paf/fundation/cancel";

    @Autowired
    private PAFGWInterfaceCaller pafCaller;

    public GWResponseGson register(String json) {

        Form form = new Form();
        form.add("json", json);

        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(REGISTER_URL).withForm(form);
        GWResponseGson resp = pafCaller.post(interfaceCallObject, GWResponseGson.class);
        Logger.info(this, "call paf register, response:" + JsonHelper.toJson(resp));
        return resp;
    }

    public GWResponseGson pay(String json) {
        Form form = new Form();
        form.add("json", json);

        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(PAY_URL).withForm(form);
        GWResponseGson resp = pafCaller.post(interfaceCallObject, GWResponseGson.class);
        return resp;
    }

    public GWResponseGson cancel(String json) {
        Form form = new Form();
        form.add("json", json);

        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(CANCEL_URL).withForm(form);
        GWResponseGson resp = pafCaller.post(interfaceCallObject, GWResponseGson.class);
        return resp;
    }

}

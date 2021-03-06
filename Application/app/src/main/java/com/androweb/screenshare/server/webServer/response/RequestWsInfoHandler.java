package com.androweb.screenshare.server.webServer.response;

import com.yanzhenjie.andserver.RequestHandler;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import com.androweb.screenshare.ApplicationContext;

/**
 * Created by OddCN on 2017/11/18.
 */

public class RequestWsInfoHandler implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        StringEntity stringEntity = new StringEntity("" + ApplicationContext.getLocalIp() + ":" + ApplicationContext.getWsServerPort(), "utf-8");
        response.setEntity(stringEntity);
    }
}

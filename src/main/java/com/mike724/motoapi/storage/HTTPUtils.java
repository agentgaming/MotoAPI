package com.mike724.motoapi.storage;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class HTTPUtils {
    /**
     * @param url    the url of the HTTP server
     * @param params the post parameters
     * @param creds  credentials for basic auth
     * @return the HTTP response
     */
    public static String basicAuthPost(String url, List<NameValuePair> params, Credentials creds) throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();

        HttpParams param = client.getParams();
        HttpConnectionParams.setConnectionTimeout(param, 120000);
        HttpConnectionParams.setSoTimeout(param, 120000);

        HttpPost post = new HttpPost(url);
        Header authHeader = new BasicScheme().authenticate(creds, post, new BasicHttpContext());
        post.addHeader(authHeader);
        post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        HttpResponse resp = client.execute(post);
        String respString = IOUtils.toString(resp.getEntity().getContent());
        return respString;
    }

    /**
     * @param url   the url of the HTTP server
     * @param creds credentials for basic auth
     * @return the HTTP response
     */
    public static String basicAuth(String url, Credentials creds) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        Header authHeader = new BasicScheme().authenticate(creds, get, new BasicHttpContext());
        get.addHeader(authHeader);
        HttpResponse resp = client.execute(get);
        String respString = EntityUtils.toString(resp.getEntity()).trim();
        return respString;
    }
}

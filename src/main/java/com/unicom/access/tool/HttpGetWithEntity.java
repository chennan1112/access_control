package com.unicom.access.tool;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * @author mrChen
 * @date 2021/2/8 10:20
 */
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase{
    private final static String METHOD_NAME = "GET";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpGetWithEntity() {
        super();
    }

    public HttpGetWithEntity(final URI uri) {
        super();
        setURI(uri);
    }

    HttpGetWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}

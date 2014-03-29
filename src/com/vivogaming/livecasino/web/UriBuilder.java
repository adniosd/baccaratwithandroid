package com.vivogaming.livecasino.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class UriBuilder {

    public static final URI buildURIfromString(final String _urlStr) throws MalformedURLException, URISyntaxException {
        final URL url = new URL(_urlStr);
        final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return uri;
    }
}

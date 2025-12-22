package com.example.activitiesfragmentsjava.network;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class SimpleRequestCallback extends UrlRequest.Callback {
    private final StringBuilder responseBuffer = new StringBuilder();

    public abstract void onCompleted(String responseBody);

    public abstract void onFailed(Exception e);

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
        request.read(ByteBuffer.allocateDirect(32768));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        responseBuffer.append(new String(bytes, StandardCharsets.UTF_8));
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        if (info.getHttpStatusCode() >= 200 && info.getHttpStatusCode() < 300) {
            onCompleted(responseBuffer.toString());
        } else {
            onFailed(new Exception("HTTP Error: " + info.getHttpStatusCode() + " " + responseBuffer.toString()));
        }
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        onFailed(error);
    }
}

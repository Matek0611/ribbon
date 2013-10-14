package com.netflix.ribbon.examples;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.netflix.client.AsyncBackupRequestsExecutor;
import com.netflix.client.AsyncBackupRequestsExecutor.ExecutionResult;
import com.netflix.client.http.AsyncHttpClient;
import com.netflix.client.http.AsyncHttpClientBuilder;
import com.netflix.client.http.BufferedHttpResponseCallback;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;

public class ExecutionWithBackupRequestExample {
    public static void main(String[] args) throws Exception {
        AsyncHttpClient<ByteBuffer> client = AsyncHttpClientBuilder.withApacheAsyncClient().buildClient();
        List<HttpRequest> requests = Lists.newArrayList(HttpRequest.newBuilder().uri("http://www.microsoft.com/").build(),
                HttpRequest.newBuilder().uri("http://www.google.com/").build());
        ExecutionResult<HttpResponse> results = AsyncBackupRequestsExecutor.executeWithBackupRequests(client, requests, 2, 100, TimeUnit.MILLISECONDS, new BufferedHttpResponseCallback() {
            
            @Override
            public void failed(Throwable e) {
            }
            
            @Override
            public void completed(HttpResponse response) {
                System.out.println("Get first response from server: " + response.getRequestedURI().getHost());
            }
            
            @Override
            public void cancelled() {
            }
        });
        Thread.sleep(2000);
        System.out.println("URIs tried: " + results.getAllAttempts().keySet());
        System.out.println("Callback invoked on URI: " + results.getExecutedURI());
        client.close();
    }
}

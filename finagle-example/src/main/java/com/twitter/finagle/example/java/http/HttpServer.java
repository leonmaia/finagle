package com.twitter.finagle.example.java.http;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.HttpMuxer;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Await;
import com.twitter.util.Future;
import org.jboss.netty.buffer.ChannelBuffers;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class HttpServer {
    public static void main(String[] args) throws Exception {
        runServer();
    }

    private static HttpMuxer router() {
        return new HttpMuxer()
                .withHandler("/echo", echoService());
    }
    
    private static Service<Request, Response> echoService() {
        return new Service<Request, Response>() {
            public Future<Response> apply(Request request) {
                Response response = Response.apply();
                response.setContent(ChannelBuffers.wrappedBuffer("WORKING".getBytes()));

                return Future.value(response);
            }
        };
    }

    private static void runServer() throws InterruptedException, com.twitter.util.TimeoutException {
        ListeningServer server = com.twitter.finagle.Http.server()
                .serve(new InetSocketAddress(InetAddress.getLoopbackAddress(), 8888), router());

        Await.ready(server);
    }
}


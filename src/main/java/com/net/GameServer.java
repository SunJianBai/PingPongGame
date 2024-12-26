package com.net;

import java.net.*;
import java.io.*;

public class GameServer {

    private static final int PORT = 6789;  // 服务器监听端口

    // 启动服务器并监听客户端连接
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("服务器已启动，等待玩家加入...");

            // 等待客户端连接
            while (true) {
                Socket clientSocket = serverSocket.accept();  // 阻塞，直到客户端连接
                System.out.println("客户端连接：" + clientSocket.getInetAddress());

                // 创建处理客户端请求的线程
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理每个客户端连接
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // 接收客户端消息
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("收到消息：" + message);
                    // 发送响应（例如：确认连接）
                    writer.println("欢迎进入房间！");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (writer != null) writer.close();
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

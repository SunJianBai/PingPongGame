package com.net;

import java.net.*;
import java.io.*;

public class GameClient {

    private static final String SERVER_IP = "localhost";  // 服务器 IP 地址
    private static final int SERVER_PORT = 6789;  // 服务器端口

    // 连接到服务器
    public void connect() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // 向服务器发送消息
            writer.println("玩家已连接");

            // 接收服务器消息
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("服务器响应：" + message);
                if (message.equals("游戏开始")) {
                    // 开始游戏
                    startGame();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 游戏开始后的逻辑
    private void startGame() {
        System.out.println("游戏开始了！");
        // 在这里实现客户端游戏的逻辑
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        client.connect();
    }
}

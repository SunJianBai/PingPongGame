package com.net;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LanScanner {

    private static final int BROADCAST_PORT = 12345;  // 广播端口
    private static final String BROADCAST_MESSAGE = "LAN_ROOM_DISCOVERY";  // 广播消息

    // 扫描局域网中的所有房间
    public String[] scan() {
        List<String> rooms = new ArrayList<>();
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);

            // 发送广播请求
            DatagramPacket packet = new DatagramPacket(BROADCAST_MESSAGE.getBytes(), BROADCAST_MESSAGE.length(),
                                                        new InetSocketAddress("255.255.255.255", BROADCAST_PORT));
            socket.send(packet);

            // 等待响应
            byte[] buffer = new byte[256];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(2000);  // 等待 2 秒

            // 接收响应
            while (true) {
                try {
                    socket.receive(responsePacket);
                    String roomInfo = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    rooms.add(roomInfo);
                } catch (SocketTimeoutException e) {
                    // 超时退出
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        return rooms.toArray(new String[0]);  // 返回房间列表
    }
}

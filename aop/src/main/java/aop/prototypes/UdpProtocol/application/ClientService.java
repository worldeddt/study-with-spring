package aop.prototypes.UdpProtocol.application;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

@Service
public class ClientService {
    private final String serverHost = "localhost";
    private final int serverPort = 20006;

    public void transfer() throws IOException {
        // UDP 소켓 생성
        DatagramSocket socket = new DatagramSocket();

        // 서버의 주소 생성
        InetAddress serverAddress = InetAddress.getByName(serverHost);

        // 보낼 데이터 생성
        byte[] sendData = "Hello, UDP Server!".getBytes();

        System.out.println("==== client transferring ===");

        // 보낼 데이터를 포함한 DatagramPacket 생성
        DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                serverAddress,
                serverPort
        );
// 서버로 데이터 전송
        System.out.println("데이터를 서버로 전송했습니다.");
        socket.send(sendPacket);

        // 소켓 닫기
        socket.close();
    }
}

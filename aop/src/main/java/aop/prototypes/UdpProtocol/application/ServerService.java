package aop.prototypes.UdpProtocol.application;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@Service
public class ServerService {

    private final int serverPort = 20006;

    public void receive() throws IOException {
        DatagramSocket socket = new DatagramSocket(serverPort);

        // 데이터를 받기 위한 DatagramPacket 생성
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        System.out.println("서버가 시작되었습니다. 클라이언트 데이터 기다리는 중");

        // 데이터 수신 대기
        socket.receive(receivePacket);

        // 수신한 데이터 출력
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("클라이언트로부터 받은 메시지: " + receivedMessage);

        // 소켓 닫기
        socket.close();
    }
}

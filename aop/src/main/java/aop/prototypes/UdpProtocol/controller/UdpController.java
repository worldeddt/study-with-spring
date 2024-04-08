package aop.prototypes.UdpProtocol.controller;


import aop.prototypes.UdpProtocol.application.ClientService;
import aop.prototypes.UdpProtocol.application.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

@RestController
@RequiredArgsConstructor
public class UdpController {

    private final ClientService clientService;
    private final ServerService serverService;

    @GetMapping("/udp/receive")
    public void receive() throws IOException {
        serverService.receive();
    }

    @GetMapping("/udp/request")
    public void transfer() throws IOException {
        clientService.transfer();
    }
}

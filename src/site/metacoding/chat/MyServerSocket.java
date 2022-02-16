package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    ServerSocket serverSocket; // 시스템콜 OS의 라이브러리를 땡겨씀, 리스너 (연결 = 세션)
    Socket socket; // 메세지 통신
    BufferedReader reader;

    // 추가(클라이언트에 메세지 보내기)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {
        try {
            // 1.서버소켓 생성 (리스너)
            // well known port : 0~1023
            serverSocket = new ServerSocket(1077);
            System.out.println("서버 소켓 생성");
            socket = serverSocket.accept(); // while을 돌면서 대기 (랜덤포트)
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 메세지 보내는 소켓
            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                while (true) {
                    try {
                        System.err.println("보낼 메세지 입력");
                        String sendData = sc.nextLine();
                        writer.write(sendData + "\n");
                        writer.flush();
                        if (sendData.equals("종료"))
                            break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 메세지 받는 소켓
            while (true) {
                String inputData = reader.readLine();
                System.out.println("받은 데이터 : " + inputData);
                System.out.println("클라이언트 연결됨");
                if (inputData.equals("종료"))
                    break;
            }
        } catch (Exception e) {
            System.out.println("통신오류 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인종료");
    }
}

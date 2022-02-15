package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;
    Scanner sc;
    String sendData;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);

            // 메세지 받는 소켓
            new Thread(() -> {
                String inputData;
                while (true) {
                    try {
                        inputData = reader.readLine();
                        System.out.println("받은 데이터 : " + inputData);
                        System.out.println("서버 연결됨");
                        if (inputData.equals("종료"))
                            break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

            // 메세지 보내는 소켓
            while (true) {
                System.err.println("보낼 메세지 입력");
                sendData = sc.nextLine();
                writer.write(sendData + "\n");
                writer.flush();
                if (sendData.equals("종료"))
                    break;
            }

        } catch (

        IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}

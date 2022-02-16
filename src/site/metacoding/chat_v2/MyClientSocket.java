package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    // 스레드
    Scanner sc;
    BufferedWriter writer;
    // 스레드
    BufferedReader reader;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 2000);

            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 새로운 스레드(Reader)
            new Thread(new 읽기전담스레드()).start();

            // 메인 스레드(Writer)
            while (true) {
                String keyboardInputData = sc.nextLine();
                writer.write(keyboardInputData + "\n");
                writer.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class 읽기전담스레드 implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메세지 : " + inputData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
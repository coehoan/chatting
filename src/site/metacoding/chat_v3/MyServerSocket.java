package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

// JWP = 재원프로토콜
// 1. 최초 메세지는 username으로 체킹
// 2. 구분자
// 3. ALL:메세지
// 4. CHAT:username:메세지

public class MyServerSocket {

    // 리스너 (연결받기) - 메인스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;

    // 서버는 메세지 받기

    // 서버는 메세지 보내기

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new Vector<>(); // 동기화가 처리된 ArrayList - 동시접근을 막기위함
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t);
                System.out.println("고객리스트 크기 : " + 고객리스트.size());
                new Thread(t).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스
    class 고객전담스레드 implements Runnable {

        String username;
        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ALL:메세지
        public void chatPublic(String msg) {
            try {
                for (고객전담스레드 t : 고객리스트) {
                    if (t != this) {
                        t.writer.write(t.socket.getRemoteSocketAddress() + username + " : " + msg + "\n");
                        t.writer.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // CHAT:username:메세지
        public void chatPrivate(String receiver, String msg) {
            try {
                for (고객전담스레드 t : 고객리스트) {
                    if (t.username.equals(receiver)) {
                        t.writer.write(t.socket.getRemoteSocketAddress() + "[귓속말]" + username + ": " + msg + "\n");
                        t.writer.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 재원프로토콜 (chat Public과 Private을 구분)
        // 1. ALL:안녕
        // 2. CHAT:재원:안녕
        public void jwp(String inputData) {
            // 1. 프로토콜 분리
            String[] token = inputData.split(":");
            String protocol = token[0];
            if (protocol.equals("ALL")) {
                String msg = token[1];
                chatPublic(msg);
            } else if (protocol.equals("CHAT")) {
                String receiver = token[1];
                String msg = token[2];
                chatPrivate(receiver, msg);
            } else { // 프로토콜 통과못함
                System.out.println("프로토콜 없음");
            }
        }

        @Override
        public void run() {

            // 최초 메세지는 username이다.
            try {
                username = reader.readLine();
                isLogin = true;
            } catch (Exception e) {
                isLogin = false;
                System.out.println("username을 받지 못했습니다.");
            }

            while (isLogin) {
                try {
                    String inputData = reader.readLine();

                    // List<고객전담스레드> 고객리스트에 담긴
                    // 클라이언트에게 메세지 전송(for문 이용)
                    // for each - 컬렉션의 크기만큼만 돈다.
                    // 왼쪽 - 컬렉션타입, 오른쪽 - 컬렉션
                    jwp(inputData);

                } catch (Exception e) {
                    try {
                        System.out.println("통신실패 : " + e.getMessage());
                        isLogin = false;
                        고객리스트.remove(this);
                        reader.close();
                        writer.close();
                        socket.close();
                    } catch (Exception e1) {
                        System.out.println("연결해제 프로세스 실패 : " + e1.getMessage());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}

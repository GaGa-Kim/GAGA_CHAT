import javax.imageio.IIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServerMain {
    /*통신*/
    ServerSocket serverSocket; //서버용 소켓  
    Socket socket; //클라이언트 통신용 소켓
    ArrayList<Thread> clientList; //클라이언트 통신용 스레드 리스트

    public MyServerMain() {
       clientList = new ArrayList<Thread>();
        System.out.println("채팅을 위한 서버 시작!\n");
    }

    /*메인 함수*/
    public static void main(String[] args) throws Exception {
        MyServerMain myServerMain = new MyServerMain();
        myServerMain.ClientConnect();
    }
    
    /*클라이언트 연결*/
    public void ClientConnect() throws Exception {
        try {
            serverSocket = new ServerSocket(8888); //서버 소켓 생성 및 포트 바인딩

            while (true) {
                socket = serverSocket.accept(); //클라이언트 연결 수락
                ClientThread clientThread = new ClientThread(this, socket); //클라이언트 전용 스레드 생성 및 할당
                clientList.add(clientThread); //클라이언트 통신용 스레드 리스트에 추가
                System.out.println("클라이언트가 1명 입장했습니다. 총 클라이언트의 수 : "+clientList.size()+"명\n");
                clientThread.start(); //스레드 시작
            }
        } catch (IIOException e) {
            e.printStackTrace();
        }
    }
}

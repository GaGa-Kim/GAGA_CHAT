import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    MyServerMain myServerMain;
    Socket socket;

    /*통신*/
    InputStream istream;
    OutputStream ostream;
    BufferedReader receiveRead;
    PrintWriter pwrite;

    String receiveMessage; //클라이언트로부터 받은 메시지

    public ClientThread(MyServerMain myServerMain, Socket socket) {
        this.myServerMain = myServerMain;
        this.socket = socket;
        System.out.println("IP 주소 "+socket.getInetAddress()+" 님이 입장했습니다.");
    }

    @Override
    public void run() {
        try {
            /*socket을 통해 데이터 송수신*/
            istream = socket.getInputStream(); //input
            receiveRead = new BufferedReader(new InputStreamReader(istream));

            ostream = socket.getOutputStream(); //output
            pwrite = new PrintWriter(ostream, true);

            /*클라이언트로부터 받은 메시지를 대화 중인 모든 클라이언트에게 전달*/
            while (true) {
                if ((receiveMessage = receiveRead.readLine()) != null) { //받은 메세지가 있을 경우
                    for (int i = 0; i < myServerMain.clientList.size(); i++) {
                        ClientThread clientThread = (ClientThread) myServerMain.clientList.get(i); //리스트에서 스레드를 하나씩 가져와서
                        clientThread.pwrite.println(receiveMessage); //모든 클라이언트에게 메시지 뿌려줌
                        pwrite.flush(); //버퍼 안에 있는 값 전부 비움
                    }
                } 
            }
        } catch (IOException e) { //스윙에서 클라이언트 채팅 종료 시
            System.out.println("IP 주소 "+socket.getInetAddress()+" 님이 퇴장했습니다."); //퇴장 메시지 전송
            myServerMain.clientList.remove(this); //clientList에서 해당 클라이언트 제거
            System.out.println("클라이언트가 1명 퇴장했습니다. 총 클라이언트의 수 : "+ myServerMain.clientList.size()+"명\n");
        } finally {
            try {
                socket.close();  // 소켓 종료
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

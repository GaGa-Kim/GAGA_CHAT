import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient extends JFrame {

    /*화면 구성*/
    Container container = getContentPane();
    JPanel panel = new JPanel();
    JButton exitButton = new JButton("채팅 종료");
    JTextArea chattingDisplay = new JTextArea();
    JScrollPane jScrollPane = new JScrollPane(chattingDisplay);
    JTextField chattingInput = new JTextField();

    /*통신*/
    Socket socket;
    InputStream istream;
    OutputStream ostream;
    PrintWriter pwrite;
    BufferedReader receiveRead;

    String myNickName;
    String sendMessage;
    String receiveMessage;

    boolean connection = false; //이미 연결되어 있는 상태인지 확인

    actionEvent event = new actionEvent(); //메시지 전송 이벤트

    Font font = new Font("Gothic", Font.PLAIN, 15);

    public MyClient(JFrame frame, String nickName) {
        myNickName = nickName;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("GAGA CHAT");
        setSize(500, 400);

        exitButton.setFont(font);
        chattingDisplay.setFont(font);

        chattingDisplay.setEditable(false);
        panel.setLayout(new BorderLayout());
        panel.add("Center", chattingInput);
        panel.add("South", exitButton);

        container.setLayout(new BorderLayout());
        container.add("Center", jScrollPane);
        container.add("South", panel);

        exitButton.addActionListener(event); //채팅 종료 이벤트
        chattingInput.addActionListener(event); //메시지 전송 이벤트

        new Thread() {
            @Override
            public void run() {
                try {
                    //소켓 생성 및 접속 - 서버 프로그램으로 연결 요청 및 데이터 전송
                    socket = new Socket("58.224.221.83", 8888);

                    /*socket을 통해 데이터 송수신*/
                    istream = socket.getInputStream(); //input
                    receiveRead = new BufferedReader(new InputStreamReader(istream));

                    ostream = socket.getOutputStream(); //output
                    pwrite = new PrintWriter(ostream, true);

                    if (connection == false) { //처음 입장 시에만 입장 메시지를 띄워줌
                        pwrite.println("[" + myNickName + "] 님이 입장했습니다.");
                        connection = true;
                    }

                    /*클라이언트로부터 받은 메시지 출력*/
                    while(true) {
                        try {
                            receiveMessage = receiveRead.readLine();
                            chattingDisplay.append(receiveMessage + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*클라이언트의 메시지 전송 및 채팅 종료 이벤트*/
    class actionEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == chattingInput) { //엔터(입력) 이벤트 발생 시
                sendMessage = chattingInput.getText(); //현재 클라이언트가 입력창에 입력한 메시지 가져옴
                pwrite.println("[" + myNickName + "] "+sendMessage);  //메시지 전송
                pwrite.flush(); //버퍼 안에 있는 값 전부 비움
                chattingInput.setText("");  //전송 후 입력 창 비워주기
            }
            if(e.getSource() == exitButton) { //채팅 종료 이벤트 발생 시
                pwrite.println("[" + myNickName + "] 님이 퇴장했습니다.");  //퇴장 메시지 전송
                pwrite.flush(); //버퍼 안에 있는 값 전부 비움
                System.exit(0); //채팅 프로그램 전체 종료
            }
        }
    }
}

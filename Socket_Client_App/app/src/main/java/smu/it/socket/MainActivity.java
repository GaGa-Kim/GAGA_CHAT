package smu.it.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private Handler mHandler; //메시지 전달을 위한 핸들러
    Socket socket; //통신용 소켓
    PrintWriter pwrite;
    BufferedReader receiveRead;
    InputStream istream;
    OutputStream ostream;

    TextView showNickName; //클라이언트의 닉네임
    Button sendButton;  //보내기 버튼
    Button exitButton;  //종료 버튼
    TextView chatView;  //채팅 내역
    EditText sendText;  //보낼 내용 입력

    String nickName;  //닉네임
    String sendMessage;  //보낼 메시지
    String receiveMessage;  //받는 메시지

    private String serverIp = "58.224.221.83"; //서버 IP

    boolean connection = false; //이미 연결되어 있는 상태인지 확인

    /*메인 액티비티가 화면에서 보이지 않으면 소켓 종료*/
    @Override
    protected void onStop() {
        super.onStop();
        try {
            pwrite.close();
            socket.close();
            System.out.println("종료되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        /*로그인 액티비티에서 닉네임 정보 받아옴*/
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickName");
        showNickName = (TextView) findViewById(R.id.showNickName); //클라이언트의 닉네임
        showNickName.setText(nickName);

        chatView = (TextView) findViewById(R.id.chatView); //채팅 내역을 띄울 뷰
        sendText = (EditText) findViewById(R.id.sendText); //채팅 입력 창
        sendButton = (Button) findViewById(R.id.sendButton); //전송 버튼
        exitButton = findViewById(R.id.exitButton); //채팅 종료 버튼

        /*클라이언트가 보낸 메시지를 받음*/
        new Thread() {
            public void run() {
                try {
                    //Socket 생성 및 접속 - 서버 프로그램으로 연결 요청 및 데이터 전송
                    socket = new Socket(serverIp, 8888);

                    /*socket을 통해 데이터 송수신*/
                    istream = socket.getInputStream();  //input
                    receiveRead = new BufferedReader(new InputStreamReader(istream));

                    ostream = socket.getOutputStream();  //output
                    pwrite = new PrintWriter(ostream, true);


                    if (connection == false) {  //처음 입장 시에만 입장 메시지를 띄워줌
                        pwrite.println("[" + nickName + "] 님이 입장했습니다.");
                        connection = true;
                    }

                    /*클라이언트로부터 받은 메시지 출력*/
                    while(true){
                        receiveMessage = receiveRead.readLine();
                        if(receiveMessage != null) {  //받은 메세지가 있을 경우
                            mHandler.post(new chattingUpdate(receiveMessage));  //채팅 내역 갱신
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        /*클라이언트에게 메시지를 보냄*/
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage = sendText.getText().toString();  //현재 클라이언트가 입력창에 입력한 메시지 가져옴

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            pwrite.println("[" + nickName + "] " + sendMessage);  //메시지 전송
                            pwrite.flush();  //버퍼 안에 있는 값 전부 비움
                            sendText.setText(null);  //전송 후 입력 창 비워주기
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        /*채팅 종료 버튼 구현*/
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            pwrite.println("[" + nickName + "] 님이 퇴장했습니다.");  //퇴장 메시지 전송
                            pwrite.flush();  //버퍼 안에 있는 값 전부 비움
                            sendText.setText(null);  //전송 후 입력 창 비워주기

                            System.exit(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }

    /* 메시지 수신 후 채팅 내역 업데이트 */
    class chattingUpdate implements Runnable{
        private String message;
        public chattingUpdate(String str) {
            this.message = str;
        }

        @Override
        public void run() {
            //새로 입력된 메세지를 기존 채팅 내역 아래에 추가해 줌
            chatView.setText(chatView.getText().toString() + message + "\n");
        }
    }
}
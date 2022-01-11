import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyClientMain extends JFrame implements ActionListener {

    /*화면 구성*/
    Container container = getContentPane();
    JTextField nickNameInput = new JTextField(10);
    JPanel emptyPanel = new JPanel();
    JPanel nickNamePanel = new JPanel();
    JLabel emptyLabel = new JLabel(" ");
    JLabel label = new JLabel("닉네임 : ");
    JButton enterButton = new JButton("확인");

    Font font = new Font("Gothic", Font.BOLD, 18);

    public static void main(String[] args) {

        MyClientMain myClientMain = new MyClientMain();
    }

    public MyClientMain() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("GAGA CHAT");
        setSize(400, 200);

        label.setFont(font);
        enterButton.setFont(font);

        emptyPanel.add(emptyLabel);
        nickNamePanel.add(label);
        nickNamePanel.add(nickNameInput);

        container.setLayout(new BorderLayout());
        container.add("North", emptyPanel);
        container.add("Center", nickNamePanel);
        container.add("South", enterButton);

        enterButton.addActionListener(this); //채팅 프레임 입장

        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String nickName = nickNameInput.getText();
        MyClient myClient = new MyClient(this, nickName); //닉네임 전송
        this.setVisible(false); //현재 프레임 비활성화
        myClient.setVisible(true); //채팅 프레임 활성화
    }
}
package smu.it.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    EditText enterNickname; //닉네임 입력창
    Button enterButton; //입장 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enterNickname = findViewById(R.id.enterNickname);

        enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //버튼을 누르면 메인 액티비티로 넘어감
                Intent intent = new Intent(getApplication(), MainActivity.class);
                String nickName = enterNickname.getText().toString();

                intent.putExtra("nickName", nickName);
                enterNickname.setText(null); //입장 후 닉네임 입력창 비워줌
                startActivity(intent);
            }
        });
    }
}
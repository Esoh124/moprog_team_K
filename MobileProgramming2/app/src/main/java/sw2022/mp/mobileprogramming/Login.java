package sw2022.mp.mobileprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText metName, metId, metPw; // 로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        metId = findViewById(R.id.etId);
        metPw = findViewById(R.id.etPw);

        Button loginbutton= findViewById(R.id.loginbtn);
        Button signupbutton=findViewById(R.id.signupbtn);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 요청
                String strId = metId.getText().toString();
                String strPw = metPw.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strId, strPw).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 파괴
                        } else {
                            Toast.makeText(Login.this, "로그인 실패..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }
}
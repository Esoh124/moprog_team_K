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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUp extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText metRealName, metName, metId, metPw; // 회원가입 입력필드
    int idCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        metRealName = findViewById(R.id.etRealName);
        metName = findViewById(R.id.etName);
        metId = findViewById(R.id.etId);
        metPw = findViewById(R.id.etPw);

        Button accountbutton = findViewById(R.id.accountBtn);
        Button backbutton = findViewById(R.id.backBtn);
        Button duplicateButton = findViewById(R.id.duplicateId);

        duplicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.child("UserAccount").child("IdList").child(metName.getText().toString()).child("idToken").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        if (value!=null){
                            Toast.makeText(SignUp.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (metName.getText().toString().length()==0){
                                Toast.makeText(SignUp.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();

                                metName.setClickable(false);
                                metName.setFocusable(false);

                                idCheck = 1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        accountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (metId.getText().toString().length()==0){
                    Toast.makeText(SignUp.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (metPw.getText().toString().length()==0){
                    Toast.makeText(SignUp.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (idCheck==1){
                    // 회원가입 처리 시작
                    String strRealName = metRealName.getText().toString();
                    String strName = metName.getText().toString();
                    String strId = metId.getText().toString();
                    String strPw = metPw.getText().toString();

                    // Firebase Auth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strId, strPw).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setRealName(strRealName);
                                account.setName(strName);
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPw);

                                UserAccount IdTokenList = new UserAccount();
                                IdTokenList.setIdToken(firebaseUser.getUid());

                                // setValue : database에 insert 행위
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                mDatabaseRef.child("UserAccount").child("IdList").child(account.getName()).setValue(IdTokenList);

                                Toast.makeText(SignUp.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                idCheck = 0;
                            } else {
                                Toast.makeText(SignUp.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                idCheck = 0;
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "중복확인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
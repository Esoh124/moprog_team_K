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

public class friend_add extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText setFriendName, findFriendId; // 회원가입 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        setFriendName = findViewById(R.id.set_friendname);
        findFriendId = findViewById(R.id.find_friend_id);

        Button button = findViewById(R.id.add_newfriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구추가 시작
                String strFName = setFriendName.getText().toString();
                String strFId = findFriendId.getText().toString();

                addFriend(strFName, strFId);
            }
        });

        Button backbutton = findViewById(R.id.backbtn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void addFriend(String friendName, String friendId)
    {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef.child("UserAccount").child("IdList").child(friendId).child("idToken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);

                if (value!=null){
                    if(value.compareTo(firebaseUser.getUid())==0){
                        Toast.makeText(friend_add.this, "본인 아이디는 친구 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        saveFriendList(value, friendName, friendId);
                    }
                } else {
                    Toast.makeText(friend_add.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveFriendList(String idToken, String friendName, String friendId) {
        mDatabaseRef.child("UserAccount").child(idToken).child("realName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);

                if (value.compareTo(friendName)!=0){
                    Toast.makeText(friend_add.this, "이름과 아이디 정보가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount account = new UserAccount();

                    account.setFriendName(friendName);
                    account.setFriendEmailId(friendId);
                    account.setFriendIdToken(idToken);

                    String userUid = firebaseUser.getUid();

                    mDatabaseRef.child("UserAccount").child(userUid).child("friendList").child(friendId).setValue(account);
                    Toast.makeText(friend_add.this, "친구추가에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
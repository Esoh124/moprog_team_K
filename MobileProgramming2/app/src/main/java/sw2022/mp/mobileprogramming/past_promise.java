package sw2022.mp.mobileprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class past_promise extends AppCompatActivity {
    ArrayList<PromiseData> promiseDatalist;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText setFriendName, findFriendId; // 회원가입 입력필드

    PromiseAdapter promiseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_promise);

        promiseDatalist = new ArrayList<PromiseData>();

//        ListView listView = (ListView) v.findViewById(R.id.listView);
//        promiseAdapter = new promiseAdapter(this, promiseDatalist);
//        listView.setAdapter(promiseAdapter);
//
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");
//        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//
//        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("promiseList").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                promiseDatalist.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    promiseDatalist.add(new FriendData(R.drawable.person, ds.child("friendName").getValue().toString(), ds.child("friendEmailId").getValue().toString()));
//                    Log.d("태그", "태그" + ds.child("friendName").getValue().toString() + ds.child("friendEmailId").getValue().toString());
//                }
//                promiseAdapter.notifyDataSetChanged();
//            }
//
//
//            @Override      public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
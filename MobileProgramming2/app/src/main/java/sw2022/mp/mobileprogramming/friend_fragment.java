package sw2022.mp.mobileprogramming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class friend_fragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<FriendData> friendDatalist;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText setFriendName, findFriendId; // 회원가입 입력필드

    MyAdapter myAdapter;

    public friend_fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friend_fragment, container, false);

        friendDatalist = new ArrayList<FriendData>();

        ListView listView = (ListView) v.findViewById(R.id.listView);
        myAdapter = new MyAdapter(this.getContext(), friendDatalist);
        listView.setAdapter(myAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileUri = snapshot.child("Person").child("profileUri").getValue().toString();
                friendDatalist.clear();

                for (DataSnapshot ds : snapshot.child("UserAccount").child(firebaseUser.getUid()).child("friendList").getChildren()) {
                    if (ds.child("profileUri").getValue() != null)
                    {
                        friendDatalist.add(new FriendData(ds.child("profileUri").getValue().toString(), ds.child("friendName").getValue().toString(), ds.child("friendEmailId").getValue().toString()));
                    } else {
                        friendDatalist.add(new FriendData(profileUri, ds.child("friendName").getValue().toString(), ds.child("friendEmailId").getValue().toString()));
                    }
                    Log.d("태그", "태그" + ds.child("friendName").getValue().toString() + ds.child("friendEmailId").getValue().toString());
                }
                myAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button button = (Button) v.findViewById(R.id.friend_add);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friend_add:
                Intent intent = new Intent(view.getContext(), friend_add.class);
                startActivity(intent);
                break;
        }
    }
}
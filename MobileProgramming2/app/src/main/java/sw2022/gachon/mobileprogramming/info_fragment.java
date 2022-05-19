package sw2022.gachon.mobileprogramming;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class info_fragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_fragment, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView userId = (TextView) view.findViewById(R.id.userId);

        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount account = snapshot.getValue(UserAccount.class);
                userName.setText(account.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userId.setText(firebaseUser.getEmail());

        Button btn_logout = (Button) view.findViewById(R.id.logoutBtn);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.logoutBtn:
                        mFirebaseAuth.signOut();

                        Intent intent = new Intent(view.getContext(), Login.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // 탈퇴처리
        // mFirebaseAuth.getCurrentUser().delete();

        // Inflate the layout for this fragment
        return view;
    }
}
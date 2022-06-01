package sw2022.mp.mobileprogramming;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sw2022.mp.mobileprogramming.camera.CameraActivity;

public class promise_progress extends Fragment {

    ArrayList<PromiseList> promiselist;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스

    PromiseAdapter promiseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_promise_progress, container, false);

        promiselist = new ArrayList<PromiseList>();

        ListView listView = (ListView) v.findViewById(R.id.promiseListView);
        promiseAdapter = new PromiseAdapter(this.getContext(), promiselist);
        listView.setAdapter(promiseAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        // 오늘날짜
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        int getToday = Integer.parseInt(today.format(date));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promiselist.clear();

                for (DataSnapshot ds : snapshot.child("UserAccount").child(firebaseUser.getUid()).child("PromiseList").getChildren()) {
                    String promiseDate = ds.child("mDate").getValue().toString();
                    promiseDate = promiseDate.replaceAll("-", "");
                    int calDate = Integer.parseInt(promiseDate);

                    if (calDate >= getToday)
                    promiselist.add(new PromiseList(ds.child("mPromiseName").getValue().toString(), ds.child("mDate").getValue().toString(), ds.child("mTime").getValue().toString(), ds.child("mFriend").getValue().toString()));
                    Log.d("태그", "날짜" + date);
                }
                promiseAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}
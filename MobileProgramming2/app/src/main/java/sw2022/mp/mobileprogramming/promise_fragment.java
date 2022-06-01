package sw2022.mp.mobileprogramming;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class promise_fragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<ListViewFriendList> mfriendDataList;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스

    private ListView listView;

    public Double latitude=0.0; // 위도
    public Double longitude=0.0; // 경도

    private int yearValue;
    private int monthValue;
    private int dayValue;
    private int hourValue;
    private int minuteValue;

    LatLng marker = new LatLng(latitude, longitude);
    MapView sView = null;

    CustomChoiceListViewAdapter myAdapter;

    public static promise_fragment newInstance(){
        return new promise_fragment();
    }


    public promise_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_promise_fragment, container, false);

        CheckBox invisibleCheckbox = (CheckBox) v.findViewById(R.id.invisibleCheckbox);
        invisibleCheckbox.setVisibility(View.INVISIBLE);

        mfriendDataList = new ArrayList<ListViewFriendList>();

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        TextView datePciker = (TextView) v.findViewById(R.id.textDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePciker.setText(String.format("%02d/%02d/%02d", year, (month+1), dayOfMonth));

                yearValue = year;
                monthValue = month+1;
                dayValue = dayOfMonth;
            }
        }, mYear, mMonth, mDay);

        datePciker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datePciker.isClickable()){
                    datePickerDialog.show();
                }
            }
        });

        TextView timePciker = (TextView) v.findViewById(R.id.textTime);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timePciker.setText(String.format("%02d:%02d", hourOfDay, minute));

                hourValue = hourOfDay;
                minuteValue = minute;
            }
        }, mHour, mMinute, false);

        timePciker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timePciker.isClickable()) {
                    timePickerDialog.show();
                }
            }
        });

        ScrollView mScrollV = (ScrollView) v.findViewById(R.id.scrollV);
        listView = (ListView) v.findViewById(R.id.friendList);
        myAdapter = new CustomChoiceListViewAdapter();
        listView.setAdapter(myAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("friendList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mfriendDataList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    myAdapter.addItem(ds.child("friendName").getValue().toString(), ds.child("friendEmailId").getValue().toString());
                    Log.d("태그", "태그" + ds.child("friendName").getValue().toString() + ds.child("friendEmailId").getValue().toString());
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollV.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //mapview 부름
        sView = v.findViewById(R.id.s_map);
        sView.onCreate(savedInstanceState);
        sView.getMapAsync(this);
//
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");
//        firebaseUser = mFirebaseAuth.getCurrentUser();
//        String Uid = firebaseUser.getUid();
//
//        mDatabaseRef.child("UserAccount").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                latitude = snapshot.child("latitude").getValue(Double.class);
//                longitude = snapshot.child("longitude").getValue(Double.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Button appointbtn = (Button)v.findViewById(R.id.appointment);

        appointbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocation(latitude, longitude);
            }
        });

        return v;
    }

    // 최근 추가한 위치정보 저장
    private void addLocation(double latitude, double longitude) {
        String promiseName;
        String DateValue;
        String TimeValue;

        String strMonthValue;
        String strDayValue;
        String strHourValue;
        String strMinuteValue;

        EditText promiseText = (EditText) getView().findViewById(R.id.textName);

        if (promiseText.length() == 0)
        {
            Toast.makeText(getContext(), "약속이름을 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (yearValue == 0 || monthValue == 0 || dayValue == 0)
        {
            Toast.makeText(getContext(), "날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (hourValue == 0 || minuteValue == 0)
        {
            Toast.makeText(getContext(), "시간을 입력해주세요.", Toast.LENGTH_LONG).show();
        } else if (latitude == 0 || longitude == 0) {
            Toast.makeText(getContext(), "위치를 입력해주세요.", Toast.LENGTH_LONG).show();
        } else {
            if (monthValue < 10)
                strMonthValue = 0 + String.valueOf(monthValue);
            else
                strMonthValue = String.valueOf(monthValue);

            if (dayValue < 10)
                strDayValue = 0 + String.valueOf(dayValue);
            else
                strDayValue = String.valueOf(dayValue);

            if (hourValue < 10)
                strHourValue = 0 + String.valueOf(hourValue);
            else
                strHourValue = String.valueOf(hourValue);

            if (minuteValue < 10)
                strMinuteValue = 0 + String.valueOf(minuteValue);
            else
                strMinuteValue = String.valueOf(minuteValue);

            promiseName = String.valueOf(yearValue) + strMonthValue + strDayValue + strHourValue + strMinuteValue;
            DateValue = String.valueOf(yearValue) + "-" + strMonthValue + "-" + strDayValue;
            TimeValue = strHourValue + ":" + strMinuteValue;

            PromiseList promiseData = new PromiseList();

            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

            promiseData.setmPromiseName(String.valueOf(promiseText.getText()));
            promiseData.setmDate(DateValue);
            promiseData.setmTime(TimeValue);

            // 여기가 문제
            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
            int count = myAdapter.getCount() ;
            String friendList = "";

            for (int i = count-1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    friendList = friendList + myAdapter.getItem(i) + "  ";
                }
            }

            // 모든 선택 상태 초기화.
            listView.clearChoices();
            myAdapter.notifyDataSetChanged();

            promiseData.setmFriend(friendList);
            promiseData.setMlongitude(longitude);
            promiseData.setMlatitude(latitude);

            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("PromiseList").child(promiseName).setValue(promiseData);

            Log.d("태그", "날짜" + yearValue + monthValue + dayValue);
            Log.d("태그", "시간" + hourValue + minuteValue);

            promiseText.setText(null);
        }
    }

    //이 메서드가 없으면 지도가 보이지 않음
    @Override
    public void onStart() {
        super.onStart();
        sView.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        sView.onStop();

    }
    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        sView.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        sView.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sView.onLowMemory();
    }

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng seoul = new LatLng(37.564214, 127.001699);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,10));

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();

                // 마커 타이틀
                mOptions.title("마커 좌표");
                latitude = point.latitude; // 위도
                longitude = point.longitude; // 경도
                // 임시 저장
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
                // Add a marker in Sydney and move the camera
                marker = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(marker).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                String msg = "위치를 추가하려면 추가하기 버튼을 누르세요.";
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
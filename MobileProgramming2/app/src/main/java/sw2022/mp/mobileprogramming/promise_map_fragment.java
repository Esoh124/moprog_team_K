package sw2022.mp.mobileprogramming;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class promise_map_fragment extends promise_fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private double latitude = 0;
    private double longitude = 0;

    LatLng marker = new LatLng(latitude, longitude);
    MapView sView = null;

    public static promise_map_fragment newInstance() {
        return new promise_map_fragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.promise_map_fragment, container, false);

        //mapview 부름
        sView = view.findViewById(R.id.s_map);
        sView.onCreate(savedInstanceState);
        sView.getMapAsync(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        Button btn = (Button) view.findViewById(R.id.sendbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocation(latitude, longitude);
            }
        });
        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendbtn:
                Intent intent = new Intent(view.getContext(), promise_fragment.class);
                startActivity(intent);
                break;
        }
    }

    // 최근 추가한 위치정보 저장
    private void addLocation(double latitude, double longitude) {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = firebaseUser.getUid();
                saveLoLa(value, latitude, longitude);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
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
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng gachon = new LatLng(37.44890133000,127.12690433300);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gachon,15));

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();

                // 마커 타이틀
                mOptions.title("마커 좌표");
                latitude = point.latitude; // 위도
                longitude = point.longitude; // 경도
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

    private void saveLoLa(String idToken, double latitude, double longitude) {
        mDatabaseRef.child("UserAccount").child(idToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                UserAccount account = new UserAccount();

                account.setLatitude(latitude);
                account.setLongitude(longitude);

                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

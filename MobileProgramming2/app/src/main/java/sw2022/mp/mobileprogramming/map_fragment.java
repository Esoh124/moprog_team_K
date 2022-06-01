package sw2022.mp.mobileprogramming;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import sw2022.mp.mobileprogramming.camera.CameraActivity;

public class map_fragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    FusedLocationProviderClient client;
    Timer timer = null;
    double latitude = 0;
    double longitude = 0;
    boolean isFirst = true;

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

//        client = LocationServices.getFusedLocationProviderClient(getActivity());
//
//
//        timer = new Timer();
//
//        TimerTask TT = new TimerTask() {
//            @Override
//            public void run() {
//                // 반복실행할 구문
//                permissionCheck();
//            }
//        };
//
//        timer.schedule(TT, 0, 10000); //Timer 실행
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0)
                && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            getCurrentLocation();
        }
        else {
            // When permission are denied
            // Display toast
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map_fragment, container, false);

        client = LocationServices.getFusedLocationProviderClient(getActivity());


        timer = new Timer();

        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                permissionCheck();
            }
        };

        timer.schedule(TT, 0, 10000); //Timer 실행

//        Button btnCamera = view.findViewById(R.id.btn_camera);

//        btnCamera.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng CURRENT = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(CURRENT);

        addCustomMarker();


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                return null;
            }

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.LEFT);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

//        mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 15));
//
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void addCustomMarker() {
        if (mMap == null) {
            return;
        }


        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String userUid = firebaseUser.getUid();
        UserAccount account = new UserAccount();
        PromiseData promiseData = new PromiseData();

        promiseData.getPromiseDate();

        // 오늘날짜
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        int getToday = Integer.parseInt(today.format(date));

        mDatabaseRef.child("UserAccount").child(userUid).child("PromiseList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String promiseDate = ds.child("mDate").getValue().toString();
                    promiseDate = promiseDate.replaceAll("-", "");
                    int calDate = Integer.parseInt(promiseDate);

                    if (calDate < getToday){
                        latitude = Double.parseDouble(ds.child("mlatitude").getValue().toString());
                        longitude = Double.parseDouble(ds.child("mlongitude").getValue().toString());

                        String mname =  ds.child("mPromiseName").getValue().toString();
                        String mdate = ds.child("mDate").getValue().toString();
                        String mtime = ds.child("mTime").getValue().toString();
                        String mfriend =  ds.child("mFriend").getValue().toString();

                        LatLng CURRENT = new LatLng(latitude, longitude);
                        // adding a marker on map with image from  drawable

                        mMap.addMarker(new MarkerOptions().position(CURRENT).title(mname)
                        .snippet(mdate + "\n" + mtime + "\n" + mfriend));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LatLng CURRENT = new LatLng(37.564214, 127.001699);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 10));
        /**
         Glide.with(getActivity()).asBitmap()
         .load("https://mobileteamk.s3.ap-northeast-2.amazonaws.com/teamk/4AB38AC6.png")
         .fitCenter()
         .into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

        mMap.addMarker(new MarkerOptions().position(CURRENT).title("").icon(BitmapDescriptorFactory.fromBitmap(bitmappros(resource))));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 15));
        }
        });


         **/

    }

    private Bitmap bitmappros(Bitmap res){
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImage  = customMarkerView.findViewById(R.id.location_image);
        markerImage.setImageBitmap(res);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)getActivity()
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task)
                        {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Log.d("getLatitude",String.valueOf(location.getLatitude()));
                                Log.d("getLatitude",String.valueOf(location.getLongitude()));
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                if(isFirst)
                                {
                                    if(mMap != null)
                                    {
                                        LatLng CURRENT = new LatLng(location.getLatitude(), location.getLongitude());
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(CURRENT);
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 15));

                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                        isFirst = false;
                                    }

                                }

                            }
                            else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest
                                        = new LocationRequest()
                                        .setPriority(
                                                LocationRequest
                                                        .PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult)
                                    {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();
                                        Log.d("getLatitude",String.valueOf(location1.getLatitude()));
                                        Log.d("getLatitude",String.valueOf(location1.getLongitude()));
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(
                            Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();

    }

    // Location 퍼미션 체크
    private void permissionCheck()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            getCurrentLocation();

        }
        else {
            // When permission is not granted
            // Call method
            requestPermissions(
                    new String[] {
                            Manifest.permission
                                    .ACCESS_FINE_LOCATION,
                            Manifest.permission
                                    .ACCESS_COARSE_LOCATION },
                    100);
        }
    }
}
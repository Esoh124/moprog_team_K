package sw2022.gachon.mobileprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity{
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private GoogleMap mMap;
    private friend_fragment fragmentFriend = new friend_fragment();
    private map_fragment fragmentMap = new map_fragment();
    private promise_fragment fragmentPromise = new promise_fragment();
    private info_fragment fragmentInfo = new info_fragment();
    private meeting fragmentMeeting = new meeting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frameLayout, fragmentMap).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_mainmap:
                    transaction.replace(R.id.main_frameLayout, fragmentMap).commitAllowingStateLoss();
                    break;
                case R.id.menu_meeting:
                    transaction.replace(R.id.main_frameLayout, fragmentMeeting).commitAllowingStateLoss();
                    break;
                case R.id.menu_friend:
                    transaction.replace(R.id.main_frameLayout, fragmentFriend).commitAllowingStateLoss();
                    break;
                case R.id.menu_promise:
                    transaction.replace(R.id.main_frameLayout, fragmentPromise).commitAllowingStateLoss();
                    break;
                case R.id.menu_info:
                    transaction.replace(R.id.main_frameLayout, fragmentInfo).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }


}
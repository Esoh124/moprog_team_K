package sw2022.mp.mobileprogramming;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class info_fragment extends Fragment {

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference reference;

    private File tempFile;

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_fragment, container, false);

        // 이미지 권한 요청
        tedPermission();

        imageView = (ImageView) view.findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.profileImage:
                        if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                }
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming");

        TextView userRealName = (TextView) view.findViewById(R.id.userRealName);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView userId = (TextView) view.findViewById(R.id.userId);
//        imageView = getView().findViewById(R.id.profileImage);

        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount account = snapshot.getValue(UserAccount.class);
                userRealName.setText(account.getRealName());
                userName.setText(account.getName());

                if (snapshot.child("profile").child("imageUrl").getValue() != null)
                {
                    Model model = snapshot.child("profile").getValue(Model.class);

                    Glide.with(view)
                            .load(model.getImageUrl())
                            .into(imageView);
                } else {
                    Glide.with(view)
                            .load(R.drawable.person)
                            .into(imageView);
                }
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

                        redirectSignupActivity();
                }
            }
        });

        // 탈퇴처리
        // mFirebaseAuth.getCurrentUser().delete();

        // Inflate the layout for this fragment
        return view;
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("state", "kill");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri uri = data.getData();

            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("mobileProgramming").child("UserAccount");
            reference = FirebaseStorage.getInstance().getReference();

            StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getfileExtension(uri));

            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Model model = new Model(uri.toString());

                            mDatabaseRef.child(firebaseUser.getUid()).child("profile").setValue(model);

                            mDatabaseRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String id = snapshot.child("name").getValue().toString();
                                    String name = snapshot.child("realName").getValue().toString();
                                    String idToken = snapshot.child("idToken").getValue().toString();

                                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                if (ds.child("friendList").child(id).getValue() != null)
                                                {
                                                    UserAccount account = new UserAccount();

                                                    account.setFriendEmailId(id);
                                                    account.setFriendIdToken(idToken);
                                                    account.setFriendName(name);
                                                    account.setProfileUri(uri.toString());

                                                    Log.d("태그", "친구 있는 곳 " + ds);

                                                    FirebaseDatabase.getInstance().getReference("mobileProgramming").child("UserAccount").child(ds.getKey()).child("friendList").child(id).setValue(account);
                                                }
                                            }



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            });

            imageView = getView().findViewById(R.id.profileImage);

            try {
                InputStream in = getActivity().getContentResolver().openInputStream(uri);
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                imageView.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String getfileExtension(Uri uri)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
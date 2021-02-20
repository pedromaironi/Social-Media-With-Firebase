package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.fragments.ChatsFragment;
import com.pedrodev.appchat.fragments.FilterFragment;
import com.pedrodev.appchat.fragments.HomeFragment;
import com.pedrodev.appchat.fragments.ProfileFragment;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.TokenProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.utils.ViewedMessageHelper;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
        createToken();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.updateOnline(true, HomeActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, HomeActivity.this);
    }

    //    private void updateOnline(boolean status){
//        mUsersProvider.updateOnline(mAuthProvider.getUid(), status);
//    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.itemHome:
//                            openFragment(HomeFragment.newInstance("", ""));
//                            return true;
//                        case R.id.navigation_sms:
//                            openFragment(SmsFragment.newInstance("", ""));
//                            return true;
//                        case R.id.navigation_notifications:
//                            openFragment(NotificationFragment.newInstance("", ""));
//                            return true;
//                    }
//                    return false;
                    if(item.getItemId() == R.id.itemHome){
                        openFragment(new HomeFragment());
                        // Fragment Home
                    }else if(item.getItemId()==R.id.itemChat){
                        openFragment(new ChatsFragment());
                        // Fragment Chat
                    }else if(item.getItemId()==R.id.itemFilters){
                        openFragment(new FilterFragment());
                        // Fragment Filters
                    }else if(item.getItemId()==R.id.itemProfile){
                        openFragment(new ProfileFragment());
                        // Fragment Profile
                    }
                    return true;
                }
            };

    private void createToken(){
        mTokenProvider.create(mAuthProvider.getUid());
    }

}
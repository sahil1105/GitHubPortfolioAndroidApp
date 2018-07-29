package edu.illinois.cs465.github_portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Activity that represents basically the whole app.
 * Contains several fragments.
 * @author sahil1105
 */
public class MainActivity extends AppCompatActivity {

    // Username of the logged in user.
    private String username;
    private String authHead;

    //OnClickListener for the 4 tab icons in the Bottom Navigation drawer.
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    selectedFragment = ProfilePage.newInstance(username, username, authHead);
                    break;
                case R.id.navigation_repositories:
                    selectedFragment = ReposPage.newInstance(username, username, authHead);
                    break;
                case R.id.navigation_followers:
                    selectedFragment = UserListPage.newInstance(username, username, authHead,
                            "followers");
                    break;
                case R.id.navigation_following:
                    selectedFragment = UserListPage.newInstance(username, username, authHead,
                            "following");
                    break;
            }
            if (selectedFragment != null) {
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, selectedFragment);
                fragmentTransaction.commit();
            }
            return true;
        }
    };

    /**
     * What to do when the activity is created.
     * Setup the main activity including the bottom navigation drawer.
     * Also initializes the Realm Database instance.
     * @param savedInstanceState Bundle object from last run
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("username"); // get the username that was passed in
        authHead = intent.getStringExtra("authHead");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, ProfilePage.newInstance(username, username, authHead));
//        fragmentTransaction.commit();

        Realm.init(this.getApplicationContext());

    }


    /**
     * Utility method to update the Realm associated with the app.
     * @param realmObject the object to put/update in the Realm
     */
    @NonNull
    public static void updateRealm(RealmObject realmObject) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
        realm.close();
    }

}

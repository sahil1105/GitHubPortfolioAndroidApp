package edu.illinois.cs465.github_portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Used to represent the Profile Page fragment of the App.
 * REFERENCE: http://www.baeldung.com/retrofit
 * https://stackoverflow.com/questions/28478987/how-to-view-my-realm-file-in-the-realm-browser
 * @author sahil1105
 */
public class ProfilePage extends Fragment {
    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "mainUsername";
    private static final String ARG_PARAM3 = "authHead";
    public User user;
    private String username;
    private String mainUsername;
    private String authHead;
    private View view;
    private Button signOutButton;

    public ProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param username username of the person whole information must be shown
     * @param mainUsername username of the logged in user.
     * @return A new instance of fragment ProfilePage.
     */
    public static ProfilePage newInstance(String username, String mainUsername, String authHead) {
        ProfilePage fragment = new ProfilePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        args.putString(ARG_PARAM2, mainUsername);
        args.putString(ARG_PARAM3, authHead);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Steps to take when fragment is created. Sets the parameters and
     * and load the user's information from GitHub.
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            mainUsername = getArguments().getString(ARG_PARAM2);
            authHead = getArguments().getString(ARG_PARAM3);
        }
    }

    /**
     * Automatic function for view generation.
     * Also sets a listener on the 'Sign Out' button.
     * @param inflater LayoutInflater object
     * @param container ViewGroup object
     * @param savedInstanceState Bundle object
     * @return a View of the fragment to show.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_profile__page,
                container, false);
        this.signOutButton = view.findViewById(R.id.signOutButton);
        // Go back to the Login Page when the user clicks the 'Sign Out' button.
        this.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLoginIntent = new Intent(getActivity().getBaseContext(),
                        LoginActivity.class);
                startActivity(goToLoginIntent);
            }
        });
        if (!this.username.equals(this.mainUsername)) {
            this.signOutButton.setVisibility(View.INVISIBLE);
        }
        loadUserInfo(username); //load the information of the user using an API call to GitHub
        return this.view;
    }

    //TODO
//    protected class MyAsyncTask extends AsyncTask<String, > {
//
//        private User user;
//        private String username;
//        private String mainUsername;
//        private String authHead;
//        private View view;
//        private Fragment context;
//        private Button signOutButton;
//
//        public MyAsyncTask(String username, String mainUsername, String authHead, View view, Fragment context, Button signOutButton) {
//            this.username = username;
//            this.mainUsername = mainUsername;
//            this.authHead = authHead;
//            this.view = view;
//            this.context = context;
//            this.signOutButton = signOutButton;
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            loadUserInfo(username);
//            return null;
//        }
//
//        /**
//         * Utility function to load the information of the specified username
//         * from GitHub and update the UI/fragment with this information.
//         * @param username Username of the person whose information is to be pulled.
//         */
//        public void loadUserInfo(String username) {
//
//            //Get the service to make the call.
//            UserService userService = GitHubServiceGenerator.createService(UserService.class);
//            //Call the function defined in the UserService Interface.
//            Call<User> callSync = userService.getUser(this.authHead, this.username);
//            //Make an asynchronous network request to get the information
//            try {
//                Response<User> response = callSync.execute();
//                if (response.isSuccessful()) {
//                    this.user = response.body();
//                }
//                else {
//                    Toast.makeText(getContext(), "Couldn't load data. Check username again.",
//                            Toast.LENGTH_LONG).show(); //Error notification
//                    this.signOutButton.performClick(); //Sign out if login failed.
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                this.signOutButton.performClick(); //Sign out if login failed.
//            }
//
//        }
//
//    }


    /**
     * Utility function to load the information of the specified username
     * from GitHub and update the UI/fragment with this information.
     * @param username Username of the person whose information is to be pulled.
     */
    public void loadUserInfo(String username) {

        //Get the service to make the call.
        UserService userService = GitHubServiceGenerator.createService(UserService.class);
        //Call the function defined in the UserService Interface.
        Call<User> callASync = userService.getUser(this.authHead, this.username);
        //Make an asynchronous network request to get the information
        callASync.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body(); //get the user
                    updateUI(user); //update the UI with this information
                    MainActivity.updateRealm(user); //update the Realm with the information
                }
                else {
                    Toast.makeText(getContext(), "Couldn't load data. Check username again.",
                            Toast.LENGTH_LONG).show(); //Error notification
                    this.onFailure(call, null);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                signOutButton.performClick(); //Sign out if login failed.
            }
        });
    }



    /**
     * Utility function that updates the fragment UI based on the
     * information from the User object passed in.
     * @param user User object to load information from.
     */
    public void updateUI(final User user) {

        this.user = user;

        TextView userNameBox = view.findViewById(R.id.username);
        userNameBox.setText("@"+user.getLogin());

        TextView nameBox = view.findViewById(R.id.name);
        nameBox.setText(user.getName());

        TextView websiteBox = view.findViewById(R.id.website);
        websiteBox.setText("Website: "+user.getHtml_url());

        TextView emailBox = view.findViewById(R.id.email);
        emailBox.setText("Email: "+user.getEmail());

        TextView bioBox = view.findViewById(R.id.bio);
        bioBox.setText("Bio: "+user.getBio());

        TextView repoCountBox = view.findViewById(R.id.repoCount);
        repoCountBox.setText(user.getPublic_repos()+"\nRepos");

        TextView followersCountBox = view.findViewById(R.id.followersCount);
        followersCountBox.setText(user.getFollowers()+"\nFollowers");

        TextView followingCountBox = view.findViewById(R.id.followingCount);
        followingCountBox.setText(user.getFollowing()+"\nFollowing");

        TextView createdDateBox = view.findViewById(R.id.created_date);
        createdDateBox.setText("Member Since: " + getFormattedDate(user.getCreated_at()));


        // Reference: http://square.github.io/picasso/
        //Set the profile picture with their GitHub Avatar
        ImageView profileImage = view.findViewById(R.id.profileImage);
        Picasso.get().load(this.user.getAvatar_url()).resize(200,200).
                centerCrop().into(profileImage);
        //Set listeners on the Count boxes so they open the respective pages.
        repoCountBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherTab(ReposPage.newInstance(username, mainUsername, authHead));
            }
        });
        followingCountBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherTab(UserListPage.newInstance(username, mainUsername, authHead,
                        "following"));
            }
        });
        followersCountBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherTab(UserListPage.newInstance(username, mainUsername, authHead,
                        "followers"));
            }
        });

    }

    /**
     * Utility function to convert from the GitHub date format to the one we want to display in.
     * Reference: https://stackoverflow.com/questions/12503527/how-do-i-convert-the-date-from-
     * one-format-to-another-date-object-in-another-form
     * @param date Date string in GitHib's usual format (yyyy-MM-dd'T'HH:MM:SS'Z')
     * @return Date string in the format (MM/dd/yyyy)
     */
    private String getFormattedDate(String date) {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SS'Z'");
        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date originalDate = originalFormat.parse(date);
            return targetFormat.format(originalDate);
        }
        catch (java.text.ParseException e) {
            return date;
        }
    }

    /**
     * Utility function to execute a fragment transaction and replace the current
     * fragment with the given fragment.
     * @param fragmentToOpen Fragment object to replace current fragment with.
     */
    public void openOtherTab(Fragment fragmentToOpen) {

        FragmentTransaction fragmentTransaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragmentToOpen);
        fragmentTransaction.commit();

    }


}

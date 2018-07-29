package edu.illinois.cs465.github_portfolio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Fragment containing a list of UserBasic objects.
 * Used to present a user's followers and following list.
 * Reference: https://stackoverflow.com/questions/18558084/implement-asynctask-in-fragment-android
 * @author sahil1105
 */
public class UserListPage extends Fragment {

    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "mainUsername";
    private static final String ARG_PARAM3 = "authHead";
    private static final String ARG_PARAM4 = "pageType";


    private String username;
    private String mainUsername;
    private String authHead;
    private String pageType;
    private List<UserBasic> users;
    private List<UserBasic> mainUserFollowing;
    //View objects
    private ListView userList;
    private View view;
    private SwipeRefreshLayout swipeContainer;

    public UserListPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param username Parameter 1.
     * @param mainUsername Parameter 2.
     * @param pageType 'followers' or 'following'
     * @return A new instance of fragment UserListPage.
     */
    public static UserListPage newInstance(String username, String mainUsername, String authHead,
                                           String pageType) {
        UserListPage fragment = new UserListPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        args.putString(ARG_PARAM2, mainUsername);
        args.putString(ARG_PARAM3, authHead);
        args.putString(ARG_PARAM4, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Sets up the fragment, loads the followers/followed users of a specified user.
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            mainUsername = getArguments().getString(ARG_PARAM2);
            authHead = getArguments().getString(ARG_PARAM3);
            pageType = getArguments().getString(ARG_PARAM4);
        }

    }

    /**
     * Creates the view of the fragment. Also sets the header of the list.
     * Also setups up the Swipe layout for the list swipe down to reload feature.
     * @param inflater LayoutInflater object
     * @param container ViewGroup object
     * @param savedInstanceState Bundle object
     * @return Returns the view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.list_fragment, container, false);
        this.userList = this.view.findViewById(R.id.list);
        TextView listHeader = this.view.findViewById(R.id.list_header);
        listHeader.setText(this.pageType);
        this.swipeContainer = this.view.findViewById(R.id.swipe_container);
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });
        loadList(); //load the list in the listview
        return this.view;
    }


    /**
     * Wrapper function around execute call to MyAsyncTask which loads the followers/following
     * of a user. Also turns off refreshing sign when done.
     */
    private void loadList() {
        new MyAsyncTask(this, username, mainUsername, authHead, pageType,
                userList, view).execute("");
        this.swipeContainer.setRefreshing(false);
    }


    /**
     * Asynchronous Task which loads the followers/following list of the user.
     */
    protected class MyAsyncTask extends AsyncTask<String, String, String> {

        private String username;
        private String mainUsername;
        private String authHead;
        private String pageType;
        private Fragment context;
        private List<UserBasic> mainUserFollowing;
        private List<UserBasic> users;
        //View objects
        private ListView userList;
        private View view;

        /**
         * Constructor of the AsyncTask
         * @param username username of the user whose repos are to be pulled up
         * @param mainUsername username of the signed in user
         * @param authHead authorization header to use to make the API requests
         * @param context The fragment this task occurs in
         * @param userList ListView reference where to put the list
         * @param pageType 'followers' or 'following'
         * @param view View object where the task occurs.
         */
        public MyAsyncTask(Fragment context, String username, String mainUsername,
                           String authHead, String pageType,
                         ListView userList, View view) {
            this.context = context;
            this.username = username;
            this.mainUsername = mainUsername;
            this.authHead = authHead;
            this.pageType = pageType;
            this.userList = userList;
            this.view = view;
        }


        /**
         * Wrapper around loadUsers
         * @param strings Parameters
         * @return ""
         */
        @Override
        protected String doInBackground(String... strings) {
            loadUsers(username, mainUsername);
            return "";
        }

        /**
          * Utility function load the followers/followed users of the specified users by calling the
          * GitHub API and then populates them into the list view on the screen.
          * @param username Username of the person whose followers/following to pull up.
          * @param mainUsername Username of the logged in user
          */
        public void loadUsers(String username, String mainUsername) {

            UserBasicService userBasicService =
                    GitHubServiceGenerator.createService(UserBasicService.class);
            Call<List<UserBasic>> callUsers = userBasicService.getUsers(this.authHead,
                    this.mainUsername, "following");
            try {
                Response<List<UserBasic>> listResponse = callUsers.execute();
                if (listResponse.isSuccessful()) {
                    this.mainUserFollowing = listResponse.body();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            callUsers = userBasicService.getUsers(this.authHead, username, this.pageType);
            try {
                Response<List<UserBasic>> listResponse = callUsers.execute();
                if (listResponse.isSuccessful()) {
                    this.users = listResponse.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        /**
         * Post execution, update the UI with the information gathered and update the Realm DB
         * @param result result of background execution
         */
        @Override
        protected void onPostExecute(String result) {
            updateUI(users);
            RealmList<UserBasic> userBasicsRealmList = new RealmList<>();
            userBasicsRealmList.addAll(users);
            UserFollowListRealm userFollowListRealm = new UserFollowListRealm(
                    this.username+"/"+this.pageType, userBasicsRealmList);
            MainActivity.updateRealm(userFollowListRealm);
        }

        /**
         * Utility function to update the UI.
         * Loads the users into the list and sets onClick listeners on list items.
         * @param users List of UserBasic Objects to populate the list with.
         */
        public void updateUI(List<UserBasic> users) {
            this.users = users;
            UserListAdapter userListAdapter = new UserListAdapter(getContext(),
                    this.users, this.mainUserFollowing, this.authHead);
            this.userList.setAdapter(userListAdapter);
            this.userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openProfilePage(position);
                }
            });

        }

        /**
         * onClick function for list items.
         * Opens the profile page fragment of the selected user by making a fragment transaction
         * and replacing current fragment with it.
         * @param position Position in the list
         */
        private void openProfilePage(int position) {

            UserBasic userProfileToOpen = this.users.get(position);
            FragmentTransaction fragmentTransaction = context.getActivity().
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,
                    ProfilePage.newInstance(userProfileToOpen.getLogin(),
                            this.mainUsername, this.authHead));
            fragmentTransaction.commit();

        }

    }
}

package edu.illinois.cs465.github_portfolio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Used to represent the Fragment presenting the Repositories page of the user.
 * Reference: https://guides.codepath.com/android/implementing-pull-to-refresh-guide
 * @author sahil1105
 */
public class ReposPage extends Fragment {

    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "mainUsername";
    private static final String ARG_PARAM3 = "authHead";

    private String username;
    private String mainUsername;
    private String authHead;
    private List<Repo> repos;
    private List<Repo> mainUserStarredRepos;

    //View parameters
    private ListView repoList;
    private View view;
    private SwipeRefreshLayout swipeContainer;

    public ReposPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param username Parameter 1.
     * @param main_username Parameter 2.
     * @return A new instance of fragment ReposPage.
     */
    public static ReposPage newInstance(String username, String main_username, String authHead) {
        ReposPage fragment = new ReposPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        args.putString(ARG_PARAM2, main_username);
        args.putString(ARG_PARAM3, authHead);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Loads the Repos using API calls and populates them on the view.
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
     * Creates the View of the Repositories List Page.
     * Sets the headers and assigns layout instances
     * to fragment instance variables.
     * @param inflater LayoutInflater object
     * @param container ViewGroup object
     * @param savedInstanceState Bundle object
     * @return View of the Fragment Page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.list_fragment, container, false);
        this.repoList = this.view.findViewById(R.id.list);
        TextView listHeader = this.view.findViewById(R.id.list_header);
        listHeader.setText("Repositories");
        this.swipeContainer = this.view.findViewById(R.id.swipe_container);
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });
        loadList();
        return this.view;
    }

    /**
     * Wrapper function around an execute call to MyAsyncTask which loads the user's repos.
     * Also stops the refreshing sign when done.
     */
    private void loadList() {
        new MyAsyncTask(username, mainUsername, authHead, this, repoList, view).execute("");
        this.swipeContainer.setRefreshing(false);
    }

    /**
     * AsyncTask extension to help load the list of repos of the user in background.
     */
    protected class MyAsyncTask extends AsyncTask<String,String,String> {

        private String username;
        private String mainUsername;
        private String authHead;
        private List<Repo> repos;
        private List<Repo> mainUserStarredRepos;
        private Fragment context;

        //View parameters
        private ListView repoList;
        private View view;

        /**
         * Constructor of the AsyncTask
         * @param username username of the user whose repos are to be pulled up
         * @param mainUsername username of the signed in user
         * @param authHead authorization header to use to make the API requests
         * @param context The fragment this task occurs in
         * @param repoList ListView reference where to put the list
         * @param view View object where the task occurs.
         */
        public MyAsyncTask(String username, String mainUsername, String authHead,
                           Fragment context, ListView repoList, View view) {
            this.context = context;
            this.username = username;
            this.mainUsername = mainUsername;
            this.authHead = authHead;
            this.repoList = repoList;
            this.view = view;
        }

        /**
         * Wrapper around loadRepos call.
         * @param strings Parameters
         * @return null
         */
        @Override
        protected String doInBackground(String... strings) {
            loadRepos(username);
            return null;
        }

        /**
         * Utility function to get the repos list of the specified username.
         * @param username Username of the Person whose Repositories to pull up.
         */
        public void loadRepos(String username) {

            //Get a RepoService Interface Object using the generic Service Generator
            RepoService repoService = GitHubServiceGenerator.createService(RepoService.class);
            Call<List<Repo>> callRepos = repoService.getStarredRepos(this.authHead, this.mainUsername);
            // Get the main user's starred repos
            try {
                Response<List<Repo>> listResponse = callRepos.execute();
                if (listResponse.isSuccessful()) {
                    this.mainUserStarredRepos = listResponse.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            callRepos = repoService.getRepos(this.authHead, this.username);
            // Get the current user's repos.
            try {
                Response<List<Repo>> listResponse = callRepos.execute();
                if (listResponse.isSuccessful()) {
                    this.repos = listResponse.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Updates the UI with the repos list and also updates the Realm with the information.
         * @param result String containing the result of the background operation.
         */
        @Override
        protected void onPostExecute(String result) {
            updateUI(repos);
            RealmList<Repo> repoRealmList = new RealmList<>();
            repoRealmList.addAll(repos);
            UserRepoListRealm userRepoListRealm = new UserRepoListRealm(this.username, repoRealmList);
            MainActivity.updateRealm(userRepoListRealm);
        }

        /**
         * Utility function to update the UI with the list of Repos.
         * Basically sets the adapter on the list view.
         * @param repos List of Repo objects to fill the list with.
         */
        public void updateUI(List<Repo> repos) {
            this.repos = repos;
            RepoListAdapter repoListAdapter = new RepoListAdapter(getContext(),
                    repos, mainUserStarredRepos, authHead);
            this.repoList.setAdapter(repoListAdapter);

        }


    }

}




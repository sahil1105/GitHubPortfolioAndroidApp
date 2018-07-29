package edu.illinois.cs465.github_portfolio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Custom List/Base Adapter for use in application.
 * Puts given data repository in a row of the format defined in list_item.xml
 * Reference: https://stackoverflow.com/questions/30058842/android-how-to-use-
 * list-of-string-arrays-inside-list-row
 * @author sahil1105
 */
public class RepoListAdapter extends BaseAdapter {

    private Context context; //Context of the list
    private List<Repo> repoList; //List of Repo Objects to enlist
    private List<Repo> mainUserStarredRepos; //List of mainUser's starred Repos to crosscheck with
    private LayoutInflater layoutInflater; // LayoutInflater to use
    String authHead; // authorization header to use to authenticate star requests.

    //Boilerplate functions
    @Override
    public int getCount() {
        return repoList.size();
    }

    @Override
    public Object getItem(int position) {
        return repoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Function to get the contents of every item in the list.
     * @param position Position in the list
     * @param repoListItemView The list item view
     * @param parent No idea
     * @return List item populated with relevant data
     */
    @Override
    public View getView(int position, View repoListItemView, ViewGroup parent) {

        ViewHolder viewHolder;
        //if there already is a ViewHolder object assigned, recycle it
        if (repoListItemView != null) {
            viewHolder = (ViewHolder) repoListItemView.getTag();
        }
        else {
            viewHolder = new ViewHolder();
            repoListItemView = this.layoutInflater.inflate(R.layout.list_item,
                    parent, false);
            viewHolder.repoName = repoListItemView.findViewById(R.id.list_item_txtBox1);
            viewHolder.repoDescription = repoListItemView.findViewById(R.id.list_item_txtBox2);
            viewHolder.starButton = repoListItemView.findViewById(
                    R.id.list_item_action_button_image);
            repoListItemView.setTag(viewHolder);
        }
        //put in the relevant data
        final Repo repoDataToFill = this.repoList.get(position);
        setupViewHolder(position, viewHolder, repoDataToFill);
        //set on click listener
        repoListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClick(v);
            }
        });
        return repoListItemView;

    }

    /**
     * Utility function to setup the ViewHolder associated with a list item
     * @param position Position in the list
     * @param viewHolder The viewHolder to setup
     * @param repoDataToFill The Repo object to use to setup the viewHolder
     */
    private void setupViewHolder(int position, ViewHolder viewHolder, final Repo repoDataToFill) {
        viewHolder.position = position;
        viewHolder.repoName.setText(repoDataToFill.getFull_name());
        viewHolder.repoDescription.setText(repoDataToFill.getDescription());
        viewHolder.starButton.setImageResource(mainUserStarredRepos.contains(repoDataToFill)?
                R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
        viewHolder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepoService repoService = GitHubServiceGenerator.createService(RepoService.class);
                String[] splitRepoBySlash = repoDataToFill.getFull_name().split("/");
                if (mainUserStarredRepos.contains(repoDataToFill)) {
                    ((ImageView)v).setImageResource(R.drawable.ic_star_border_black_24dp);
                    makeUnstarCall(repoService, splitRepoBySlash[0], splitRepoBySlash[1]);
                    mainUserStarredRepos.remove(repoDataToFill);
                }
                else {
                    ((ImageView)v).setImageResource(R.drawable.ic_star_black_24dp);
                    makeStarCall(repoService, splitRepoBySlash[0], splitRepoBySlash[1]);
                    mainUserStarredRepos.add(repoDataToFill);
                }

            }
        });
    }

    /**
     * Utility function to make a call to star a repository.
     * @param repoService The repoService to use to make the call
     * @param owner Name of owner of the repository.
     * @param repoName Name of the repo to star.
     */
    private void makeStarCall(RepoService repoService, String owner, String repoName) {
        Call<String> starCall = repoService.starRepo(authHead, owner, repoName);
        starCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
                else {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * Utility function to make a call to unstar a repository.
     * @param repoService The repoService to use to make the call
     * @param owner Name of owner of the repository.
     * @param repoName Name of the repo to unstar.
     */
    private void makeUnstarCall(RepoService repoService, String owner, String repoName) {
        Call<String> unstarCall = repoService.unStarRepo(authHead, owner, repoName);
        unstarCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
                else {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * Constructor for the list adapter.
     * @param context Context of the list
     * @param repoList List of Repo objects to pull information from.
     */
    public RepoListAdapter(Context context, List<Repo> repoList, List<Repo> mainUserStarredRepos,
                           String authHead) {
        this.context = context;
        this.repoList = repoList;
        this.mainUserStarredRepos = mainUserStarredRepos;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.authHead = authHead;
    }


    /**
     * Custom Container class to hold the relevant Layout views for the adapter
     */
    private class ViewHolder {
        TextView repoName;
        TextView repoDescription;
        ImageView starButton;
        int position;
    }


    /**
     * Function called when a list item is clicked. Basically,
     * pulls the HTML URL of the corresponding Repository
     * and opens the page in a web browser.
     * @param v The view that was clicked.
     */
    private void onListItemClick(View v) {

        ViewHolder viewHolder = (ViewHolder) v.getTag();
        int position = viewHolder.position;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(repoList.get(position).getHtml_url()));
        this.context.startActivity(browserIntent);

    }



}

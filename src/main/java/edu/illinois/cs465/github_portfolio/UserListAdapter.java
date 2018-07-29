package edu.illinois.cs465.github_portfolio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Custom List Adapter for a list of users and their basic information.
 * @author sahil1105
 */

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<UserBasic> userList;
    private List<UserBasic> mainUserFollowing;
    private LayoutInflater layoutInflater;
    String authHead;

    //Boilerplate functions
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets the view corresponding the
     * @param position Position in the list
     * @param userListItemView The view corresponding to item in the position in the list
     * @param parent ViewGroup object
     * @return Modified userListItemView object.
     */
    @Override
    public View getView(final int position, View userListItemView, final ViewGroup parent) {
        ViewHolder viewHolder;
        //if already a ViewHolder assigned, then use it again
        if (userListItemView != null) {
            viewHolder = (ViewHolder) userListItemView.getTag();
        }
        else {
            viewHolder = new ViewHolder();
            userListItemView = this.layoutInflater.inflate(R.layout.list_item,
                    parent, false);
            viewHolder.userUserName = userListItemView.findViewById(R.id.list_item_txtBox1);
            viewHolder.userUrl = userListItemView.findViewById(R.id.list_item_txtBox2);
            viewHolder.userAvatar = userListItemView.findViewById(R.id.list_item_image);
            viewHolder.followButton = userListItemView.findViewById(
                    R.id.list_item_action_button_image);
            userListItemView.setTag(viewHolder);
        }

        final UserBasic userInfoToFill = this.userList.get(position);
        //set the relevant parameters of the viewHolder, including the listeners
        setupViewHolder(position, viewHolder, userInfoToFill);
        return userListItemView;
    }

    /**
     * Utility function to setup the elements of the Viewholder, their contents and set appropriate
     * listeners.
     * @param position Position in the list
     * @param viewHolder ViewHolder to setup
     * @param userInfoToFill User object to setup ViewHolder with
     */
    private void setupViewHolder(int position, ViewHolder viewHolder,
                                 final UserBasic userInfoToFill) {
        viewHolder.position = position;
        viewHolder.userUserName.setText(userInfoToFill.getLogin());
        viewHolder.userUrl.setText(userInfoToFill.getHtmlUrl());
        Picasso.get().load(userInfoToFill.getAvatar_url()).
                resize(100,100).centerCrop().
                into(viewHolder.userAvatar);
        viewHolder.followButton.setImageResource(mainUserFollowing.contains(userInfoToFill)?
                R.drawable.ic_following_24dp : R.drawable.ic_person_add_following_24dp);
        viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBasicService userBasicService =
                        GitHubServiceGenerator.createService(UserBasicService.class);
                if (mainUserFollowing.contains(userInfoToFill)) {
                    ((ImageView)v).setImageResource(R.drawable.ic_person_add_following_24dp);
                    makeUnfollowCall(userBasicService, userInfoToFill);
                    mainUserFollowing.remove(userInfoToFill);
                }
                else {
                    ((ImageView)v).setImageResource(R.drawable.ic_following_24dp);
                    makeFollowCall(userBasicService, userInfoToFill);
                    mainUserFollowing.add(userInfoToFill);
                }
            }
        });
    }

    /**
     * Utility function that makes the follow call.
     * @param userBasicService UserBasicService to use to make the API call.
     * @param userToFollow UserBasic object with information about the user to follow.
     */
    private void makeFollowCall(UserBasicService userBasicService, UserBasic userToFollow) {
        Call<String> followCall = userBasicService.followUser(authHead, userToFollow.getLogin());
        followCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * Utility function that makes the unfollow call.
     * @param userBasicService UserBasicService to use to make the API call.
     * @param userToUnfollow UserBasic object with information about the user to unfollow.
     */
    private void makeUnfollowCall(UserBasicService userBasicService, UserBasic userToUnfollow) {
        Call<String> deleteCall = userBasicService.unfollowUser(authHead, userToUnfollow.getLogin());
        deleteCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * Constructor for the UserListAdapter instance.
     * @param context Context to function in
     * @param userList List of UserBasic objects to populate
     */
    public UserListAdapter(Context context, List<UserBasic> userList,
                           List<UserBasic> mainUserFollowing, String authHead) {
        this.context = context;
        this.userList = userList;
        this.mainUserFollowing = mainUserFollowing;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.authHead = authHead;
    }

    /**
     * Custom class to store the Views from the corresponding layout file.
     */
    private class ViewHolder {
        TextView userUserName;
        TextView userUrl;
        ImageView userAvatar;
        ImageView followButton;
        int position;
    }



}

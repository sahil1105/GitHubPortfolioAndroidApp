package edu.illinois.cs465.github_portfolio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface defining the GET functionality of getting a user's followers and list of people followed.
 * @author sahil1105
 */

public interface UserBasicService {

    /**
     * Gets the list of followers/following of a user using an API call to GitHub.
     * @param authHead Authorization header to use while making the API call.
     * @param username Username of the person whose list of followers/following to pull up
     * @param user_type 'followers' or 'following'
     * @return List of UserBasic objects
     */
    @GET("/users/{username}/{user_type}")
    public Call<List<UserBasic>> getUsers(@Header("Authorization") String authHead,
                                          @Path("username") String username,
                                          @Path("user_type") String user_type);

    /**
     * @param authHead Authorization header to use while making the API call.
     * @param userToFollow login of the user to follow
     * @return None if successful, error string otherwise.
     */
    @PUT("/user/following/{user_to_follow}")
    public Call<String> followUser(@Header("Authorization") String authHead,
                                   @Path("user_to_follow") String userToFollow);

    /**
     * @param authHead Authorization header to use while making the API call.
     * @param userToUnFollow login of the user to un-follow
     * @return None if successful, error string otherwise.
     */
    @DELETE("/user/following/{user_to_unFollow}")
    public Call<String> unfollowUser(@Header("Authorization") String authHead,
                                     @Path("user_to_unFollow") String userToUnFollow);

    
}

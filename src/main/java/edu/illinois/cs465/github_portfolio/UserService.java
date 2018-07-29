package edu.illinois.cs465.github_portfolio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Interface defining the GET requests for getting the information of a specified user.
 * @author sahil1105
 */

public interface UserService {

    /**
     * GET functionality to get the profile information of a specific user.
     * @param username Username of the person whose profile information is to be pulled up
     * @return User object containing the profile information.
     */
    @GET("/users/{username}")
    public Call<User> getUser(@Header("Authorization") String authHead,
                              @Path("username") String username);


}

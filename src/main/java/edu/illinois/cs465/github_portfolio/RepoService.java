package edu.illinois.cs465.github_portfolio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface to define API calls to get Repo information.
 * @author sahil1105
 */

public interface RepoService {

    /**
     * Get function request for getting the repos and basic information about them.
     * @param authHead Authorization to provide with the API request
     * @param username Username of the person whose Repos to pull up
     * @return List of Repo objects
     */
    @GET("/users/{username}/repos")
    public Call<List<Repo>> getRepos(@Header("Authorization") String authHead,
                                     @Path("username") String username);

    /**
     * @param authHead Authorization to provide with the API request
     * @param username Username of the person whose starred Repos to pull up
     * @return List of Starred Repos of the user
     */
    @GET("/users/{username}/starred")
    public Call<List<Repo>> getStarredRepos(@Header("Authorization") String authHead,
                                            @Path("username") String username);

    /**
     * @param authHead Authorization to provide with the API request
     * @param repoOwner Owner of the repo to star
     * @param repoName Name of the repo to star
     * @return None if successful, else Error string
     */
    @PUT("/user/starred/{repoOwner}/{repoName}")
    public Call<String> starRepo(@Header("Authorization") String authHead,
                                 @Path("repoOwner") String repoOwner,
                                 @Path("repoName") String repoName);

    /**
     * @param authHead Authorization to provide with the API request
     * @param repoOwner Owner of the repo to unstar
     * @param repoName Name of the repo to unstar
     * @return None if successful, else Error string
     */
    @DELETE("/user/starred/{repoOwner}/{repoName}")
    public Call<String> unStarRepo(@Header("Authorization") String authHead,
                                   @Path("repoOwner") String repoOwner,
                                   @Path("repoName") String repoName);
}

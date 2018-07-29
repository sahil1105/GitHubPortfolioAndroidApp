Assignment 3.1

Code Submission: Submitted to both GitLab and SVN.

Documentation: Provided Javadoc and inline comments (explaining specific parts) for all functions (including utility functions)

Naming: Followed a camelCase style in line with the Java Style Guidelines on the CS242 Wiki.

Requirements:

    Backend: Using Realm Storage to store the data. Since using retrofit already required making classes to store the response data,
            I used Realm since it is easy to convert such container classes to a DB format using Realm. For storing the repository/followers/following
            list of a particular user, I use a wrapper class (UserRepoListRealm and UserFollowListRealm). In the fragment classes ProfilePage, RepoPage and
            UserListPage, I make a call to updateRealm method defined in MainActivity (can handle storing of all kinds of data). The call is made such that
            if the information about the user is already in the DB, it is updated rather than making a duplicate copy. The storage is persistent across multiple runs.
            The Realm database is stored on the phone and is hence hard to get hold of. I use a series of 'adb' commands to get this file. I have included a copy
            of the 'default.realm' file that I generated during testing. The file can be opened using RealmStudio. The schema allows me to define PrimaryKeys
            which is useful to avoid duplication. I can also get the data out easily and perform queries because of the way the DB is set up.

    GitHub API Posting: I can post data to the GitHub API. The functions to do so are included in the Interface files RepoService, UserService and UserBasicService.
                        The actual calls occurs in the various Fragments and are attached to appropriate onClickListeners. These listeners are attached to the list items
                        inside the Custom List Adapters.

    OAuth: I have implemented Basic Auth. I have a login page wherein I ask for the user's username and password (EC I think). I then generate a BasicAuth token for that user
           and try to get the user's information. If the call is successful, the credentials are valid, otherwise user is asked to re-enter the username or password.
           Once a user is authenticated, I use the authentication header with all the API requests, including GET, PUT and DELETE.

    UI: Updated last week's UI a little. Followers and Followed pages have been implemented. I have added a feature to pull down to refresh lists such as
        a user's repos, following and followers. I used a SwipeRefreshLayout to achieve this. So pulling down on a list updates it. I have also removed the SIGN OUT button
        from profile pages when it isn't the logged in user's profile page (just made more sense). Also changed the color scheme of the app and modularized the
        color selection throughout the app (colors taken from colors.xml).

    Testing: Implemented exhaustive testing for all the required functionality including making API calls. I used the testing suite 'Robotium'. The tests are in the androidTest
             folders (LoginActivityUnitTest and MainActivityUnitTest). I have one test file to test the functionality of the login page and one file which tests the functionality of all the fragments (Profile Page, Followers List,
             Following List, Repos List).

NOTE: After signing in, you have to manually press one of the buttons in the bottom navigation drawer.


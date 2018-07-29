package edu.illinois.cs465.github_portfolio;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * UnitTests for the MainActivity.
 * @author sahil1105
 */
public class MainActivityUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    private Solo solo;

    /**
     * Setup the MainActivity by passing in the correct username and authHead as required by
     * the MainActivity.
     * @throws Exception
     */
    public void setUp() throws Exception {
        Intent newIntent = new Intent();
        newIntent.putExtra("username", "sahil1105");
        newIntent.putExtra("authHead", "Basic c2FoaWwxMTA1OlRpaXRDVGJwaXRDMA==");
        setActivityIntent(newIntent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * Tests functionality of the Profile fragment.
     */
    public void testProfileFragment() {

        solo.clickOnMenuItem("Profile");
        assertTrue(solo.searchText("Sahil Gupta")); //check that the correct name shows up
        assertTrue(solo.searchText("sahil1105@hotmail.com")); //check that the email is correct.

    }

    /**
     * Tests functionality of the Repos fragment.
     */
    public void testReposFragment() {

        //navigate to the Repos fragment
        solo.clickOnMenuItem("Profile");
        solo.clickOnView(solo.getView(R.id.repoCount));
        //make sure the content is correct
        assertTrue(solo.searchText("Repositories"));
        assertTrue(solo.searchText("sahil1105/MovieRatingPrediction"));
        //check that initially the top repo is not starred
        solo.getView(R.id.list_item_action_button_image).getContext().
                getResources().getDrawable(R.drawable.ic_star_border_black_24dp);
        //star the repo
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the icon turned to starred.
        solo.getView(R.id.list_item_action_button_image).getContext().getResources().
                getDrawable(R.drawable.ic_star_black_24dp);
        //un-star the repo
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the logo is back to un-star
        solo.getView(R.id.list_item_action_button_image).getContext().getResources().
                getDrawable(R.drawable.ic_star_border_black_24dp);
        //click on a repo on the list
        solo.clickOnText("sahil1105/MovieRatingPrediction");
        //check that it opens a web activity.
        assertTrue(!(solo.getCurrentActivity().getClass() != MainActivity.class));

    }

    /**
     * Tests functionality of the Followers fragment.
     */
    public void testFollowersFragment()  {

        //navigate to follower's fragment
        solo.clickOnMenuItem("Profile");
        solo.clickOnView(solo.getView(R.id.followersCount));
        //check that the content is correct
        assertTrue(solo.searchText("Followers"));
        assertTrue(solo.searchText("rkaucic"));
        //ensure that the first person on the list is followed
        solo.getView(R.id.list_item_action_button_image).getContext().
                getResources().getDrawable(R.drawable.ic_following_24dp);
        //click to unfollow
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the icon turns to not followed
        solo.getView(R.id.list_item_action_button_image).getContext().
                getResources().getDrawable(R.drawable.ic_person_add_following_24dp);
        //click to follow again
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the icon turns back to followed
        solo.getView(R.id.list_item_action_button_image).getContext().
                getResources().getDrawable(R.drawable.ic_following_24dp);

    }

    public void testFollowingFragment()  {

        //navigate to follower's fragment
        solo.clickOnMenuItem("Profile");
        solo.clickOnView(solo.getView(R.id.followingCount));
        //check that the content is correct
        assertTrue(solo.searchText("Following"));
        assertTrue(solo.searchText("rkaucic"));
        //ensure that the first person on the list is followed
        solo.getView(R.id.list_item_action_button_image).getContext().getResources().
                getDrawable(R.drawable.ic_following_24dp);
        //click to unfollow
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the icon turns to not followed
        solo.getView(R.id.list_item_action_button_image).getContext().getResources().
                getDrawable(R.drawable.ic_person_add_following_24dp);
        //click to follow again
        solo.clickOnView(solo.getView(R.id.list_item_action_button_image));
        //check that the icon turns back to followed
        solo.getView(R.id.list_item_action_button_image).getContext().
                getResources().getDrawable(R.drawable.ic_following_24dp);

    }

    /**
     * Close the activity after the test is finished.
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


}

package edu.illinois.cs465.github_portfolio;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Unit Tests for the Login Activity
 * Reference: http://www.vogella.com/tutorials/Robotium/article.html
 * @author sahil1105
 */

public class LoginActivityUnitTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityUnitTest() {
        super(LoginActivity.class);
    }

    private Solo solo;

    /**
     * Set up the solo object
     * @throws Exception
     */
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * Utility function to login
     * @param username username to login with
     * @param password Password to use while logging in.
     */
    public void loginHelper(String username, String password) {

        //enter details
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.email), username);
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.password), password);
        //click Sign In button
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.email_sign_in_button));

    }

    /**
     * Test that correct credentials are accepted.
     * @throws Exception
     */
    public void testSuccessfulLogin() throws Exception{

        loginHelper("sahil1105", "TiitCTbpitC0");
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

    }

    /**
     * Test that incorrect credentials are not accepted.
     * @throws Exception
     */
    public void testUnsuccessfulLogin() throws Exception{

        loginHelper("sahil1105", "TiitsdfCTbpitC0");
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);

    }

    /**
     * Close the activity after the test.
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }






}

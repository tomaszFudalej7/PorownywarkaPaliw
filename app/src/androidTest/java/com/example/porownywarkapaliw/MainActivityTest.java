package com.example.porownywarkapaliw;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.example.porownywarkapaliw.UsersPart.Login;
import com.example.porownywarkapaliw.UsersPart.Registration;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Major_Kuprich on 23.04.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);
    private MainActivity mainActivity;

    @Before
    public void setActivity(){
        mainActivity = mActivityRule.getActivity();
    }

    @Test
    public void testMainActivity(){
        Assert.assertNotNull(mainActivity);
    }

    @Test
    public void checkAlarmDB(){
        Assert.assertNotNull(mainActivity.getAlarmDB());
        Assert.assertTrue(mainActivity.getboolAlarmDB());
    }

    @Test
    public void testDrawerLayout(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void toolbarTest(){
        Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        mainActivity.setSupportActionBar(toolbar);
        Assert.assertNotNull(mainActivity);
        Assert.assertNotNull(toolbar);
    }

    @Test(expected = NoMatchingViewException.class)
    public void actionBarDrawerToogleTest(){
        onView(withId(R.string.navigation_drawer_open)).check(matches(isClickable()));
    }

    @Test
    public void testAddGasStationIdentifier(){
        int titleId = mainActivity.getResources().getIdentifier("Add new gas station", "id", "android");
        Assert.assertEquals(mainActivity.getResources().getIdentifier("Add new gas station", "test", "android"), titleId);
    }

    @Test
    public void checkFragmentHelper(){
        mainActivity.getFragmentHelper().loadFragment(new Login());
        mainActivity.getFragmentHelper().loadFragment(new Registration());
        Assert.assertNotNull(mainActivity.getFragmentHelper());
    }

    @Test
    public void checkAddGasStation(){
        Assert.assertNotNull(onView(withId(R.id.etGasPrice)));
        Assert.assertNotNull(onView(withId(R.id.etPetrolPrice)));
        Assert.assertNotNull(onView(withId(R.id.etServicePoints)));
        Assert.assertNotNull(onView(withId(R.id.etGasStationName)));
        Assert.assertNotNull(onView(withId(R.id.etGasStationLocation)));
    }

    @Test(expected = AssertionError.class)
    public void checkAddGasStationIfIsNull(){
        Assert.assertNull(onView(withId(R.id.etGasPrice)));
        Assert.assertNull(onView(withId(R.id.etPetrolPrice)));
        Assert.assertNull(onView(withId(R.id.etServicePoints)));
        Assert.assertNull(onView(withId(R.id.etGasStationName)));
        Assert.assertNull(onView(withId(R.id.etGasStationLocation)));
    }

    @Test(expected = NoMatchingViewException.class)
    public void checkAddGasStationMatchesDisplayed(){
        onView(withId(R.id.etGasPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.etPetrolPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.etServicePoints)).check(matches(isDisplayed()));
        onView(withId(R.id.etGasStationName)).check(matches(isDisplayed()));
        onView(withId(R.id.etGasStationLocation)).check(matches(isDisplayed()));
    }

    @Test(expected = NoMatchingViewException.class)
    public void checkAboutDialog(){
        int titleId = mainActivity.getResources()
                .getIdentifier( "About", "id", "android" );
        onView(withText("About")).check(matches(isDisplayed()));
        onView(withId(titleId)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown(){
        closeSoftKeyboard();
    }

}

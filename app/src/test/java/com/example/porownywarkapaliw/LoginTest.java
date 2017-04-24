package com.example.porownywarkapaliw;

import android.os.Build;
import android.view.View;

import com.example.porownywarkapaliw.UsersPart.Login;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * Created by Major_Kuprich on 24.04.2017.
 */

@RunWith(JUnit4.class)
public class LoginTest {

    private Login login;

    @Before
    public void setUp(){
        login = new Login();
    }

    @Test(expected = NullPointerException.class, timeout = 5000)
    public void etALPassword_ClickListener(){
        login.etALPassword_ClickListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            org.junit.Assert.assertTrue(String.valueOf(login.getPbALWaitingForResponse()), login.getAllowEnterTransitionOverlap());
        }
    }

    @Test(expected = UnsatisfiedLinkError.class, timeout = 0)
    public void testLoginResponse() throws IOException {
        login.loginResponse(null, null);
        org.junit.Assert.assertNotNull(login);
    }

    @Test(expected = NullPointerException.class, timeout = 5000)
    public void testOnClick(){
        View view = login.getView();
        login.onClick(view);
        org.junit.Assert.assertNotNull(login.getView());
        login.setView(view);
        org.junit.Assert.assertEquals(login.getView(), view);
    }

    @Test
    public void testToString(){
        Assert.assertEquals("Fragment Login", login.toString());
    }

}

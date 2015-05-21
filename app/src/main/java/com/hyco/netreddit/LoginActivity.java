/*
package com.hyco.netreddit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;


public class LoginActivity extends Activity {
    private static final String REDIRECT_URL = "http://mrsvedberg.github.io/";
    private static RedditClient reddit;
    String psswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);


        // Create our RedditClient
         reddit = new RedditClient(UserAgent.of("Android", "com.hyco.netreddi", "0.1", "mrsvedberg"));
        final OAuthHelper helper = reddit.getOAuthHelper();
        // This is Android, so our OAuth app should be an installed app.

        final Credentials credentials = Credentials.installedApp("mrsvedberg", psswd, "UMQ5vNDcMhb2XA", REDIRECT_URL);

        // If this is true, then you will be able to refresh to access token
        boolean permanent = true;
        // OAuth2 scopes to request. See https://www.reddit.com/dev/api/oauth for a full list
        String[] scopes = {"identity", "read"};

        URL authorizationUrl = helper.getAuthorizationUrl(credentials, permanent, scopes);
        // Load the authorization URL into the browser






        webView.loadUrl(authorizationUrl.toExternalForm());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    // We've detected the redirect URL
                    new UserChallengeTask(helper, credentials).execute(url);

                }
            }
        });
    }

    private class UserChallengeTask extends AsyncTask<String, Void, OAuthData> {
        private OAuthHelper helper;
        private Credentials creds;
        public UserChallengeTask(OAuthHelper helper, Credentials creds) {
            this.helper = helper;
            this.creds = creds;
        }

        @Override
        protected OAuthData doInBackground(String... params) {
            try {
                return helper.onUserChallenge(params[0], creds);
            } catch (NetworkException | OAuthException e) {
                // Handle me gracefully
            }
            return null;
        }

        @Override
        protected void onPostExecute(OAuthData oAuthData) {
            reddit.authenticate(oAuthData);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));



        }
    }
}*/

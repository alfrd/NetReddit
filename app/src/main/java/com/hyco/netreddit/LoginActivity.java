package com.hyco.netreddit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Create our RedditClient
        WebView webView = new WebView(this);
        setContentView(webView);
        reddit = new RedditClient(UserAgent.of("Android", "com.hyco.netreddi", "0.1", "mrsvedberg"));
        final OAuthHelper helper = reddit.getOAuthHelper();
        // This is Android, so our OAuth app should be an installed app.
        String psswd = getString(R.string.iamzzleeping_password);
        final Credentials credentials = Credentials.installedApp("mrsvedberg",psswd,"UMQ5vNDcMhb2XA","http://mrsvedberg.github.io/");

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
        finish();
    }

    private static final class UserChallengeTask extends AsyncTask<String, Void, OAuthData> {
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

       }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

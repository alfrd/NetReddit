package com.hyco.netreddit;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.Account;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.UserSubredditsPaginator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity {
    ListView listView;

    private List<String[]> itemList = new LinkedList<String[]>();
    private RedditClient redditClient;
    private ArrayList<String> subredditList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog hej;
    String chosenSubreddit;
    private WebView webView;
    public final static String EXTRA_MESSAGE = "com.hyco.netreddit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        webView = new WebView(MainActivity.this);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // Create our RedditClient
        redditClient = new RedditClient(UserAgent.of("Android", "com.hyco.netreddi", "0.1", "mrsvedberg"));


        final OAuthHelper helper = redditClient.getOAuthHelper();
        // This is Android, so our OAuth app should be an installed app.
        String psswd = getString(R.string.iamzzleeping_password);
        final Credentials credentials = Credentials.installedApp("mrsvedberg", psswd, "UMQ5vNDcMhb2XA", "http://mrsvedberg.github.io/");

        // If this is true, then you will be able to refresh to access token
        boolean permanent = true;
        // OAuth2 scopes to request. See https://www.reddit.com/dev/api/oauth for a full list
        String[] scopes = {"identity", "edit", "flair", "history", "modconfig", "modflair", "modlog", "modposts", "modwiki", "mysubreddits", "privatemessages", "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread"};

        URL authorizationUrl = helper.getAuthorizationUrl(credentials, permanent, scopes);
        // Load the authorization URL into the browser


        webView.loadUrl(authorizationUrl.toExternalForm());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {

                    new UserChallengeTask(helper, credentials).execute(url);

                }
            }
        });

        setContentView(webView);




    }

    private class loginUser extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            hej = new ProgressDialog(MainActivity.this);
            hej.setMessage("Loading");
            hej.show();
        }

        protected String doInBackground(Void... arg0) {
            String done = "done";


            return done;
        }

        protected void onPostExecute(String s) {


        }
    }




    private class getsubreddits extends AsyncTask<Void, Void, List<String>> {


        protected List<String> doInBackground(Void... arg0) {

            UserSubredditsPaginator subreddits = new UserSubredditsPaginator(redditClient, "subscriber");
            Listing<Subreddit> submissions = subreddits.next();

            for (Subreddit s : submissions) {
                subredditList.add(s.getDisplayName());
            }
            Collections.sort(subredditList);
            subredditList.add(0,"frontpage");
            return subredditList;

        }

        protected void onPostExecute(List<String> list) {

            drawerList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, subredditList));
        }
    }


    private class getLinks extends AsyncTask<String, Void, List<String[]>> {



        @Override
        protected List<String[]> doInBackground(String... string) {
            SubredditPaginator current;
            if (string[0] == null || string[0].equals("frontpage")) {
                current = new SubredditPaginator(redditClient);
            } else {
                current = new SubredditPaginator(redditClient, string[0]);
            }

            Listing<Submission> submissions = current.next();
            for (Submission s : submissions) {

                itemList.add(new String[]{s.getTitle(), s.getCommentCount() + " comments" + " * " + s.getDomain(), "u/" + s.getAuthor() + " * r/" + s.getSubredditName() + " * " + s.getScore() + " points", s.getUrl(),s.getThumbnail(),"https://www.reddit.com" +s.getPermalink() + ".json",s.getId(),Integer.toString(s.getVote().getValue())});
            }

            /*try {

                URL subredditURL = new URL(
                        "http://www.reddit.com/r/" + chosenSubreddit + ".json?limit=15");
                URLConnection tc = subredditURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(tc
                        .getInputStream()   ));

                String line;
                while ((line = in.readLine()) != null) {
                    JSONObject object = new JSONObject(line);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray hotTopics = data.getJSONArray("children");
                    subredditName = hotTopics.getJSONObject(0).getJSONObject("data").getString("subreddit");
                    for (int i = 0; i < hotTopics.length(); i++) {
                        JSONObject topic = hotTopics.getJSONObject(i).getJSONObject("data");

                        String author = topic.getString("author");
                        String imageUrl = topic.getString("thumbnail");
                        String postTime = topic.getString("created_utc");
                        String rScore = topic.getString("score");
                        String title = topic.getString("title");

                        itemList.add(new String[]{title,author});
                    }

                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();

            }*/
            return itemList;
        }

        protected void onPostExecute(List<String[]> list) {
            hej.dismiss();

            listView.setAdapter(new ArrayAdapter<String[]>(
                    MainActivity.this,
                    R.layout.post_list,
                    R.id.text1,
                    itemList) {

                @Override
                public int getViewTypeCount() {
                    int Count = 30;
                    return Count;
                }

                @Override
                public int getItemViewType(int position) {

                    return position;
                }
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {


                    View view = super.getView(position, convertView, parent);


                    String[] entry = itemList.get(position);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    TextView text4 = (TextView) view.findViewById(R.id.text4);
                    TextView text5 = (TextView) view.findViewById(R.id.text5);
                    TextView text6 = (TextView) view.findViewById(R.id.text6);
                    TextView text7 = (TextView) view.findViewById(R.id.text7);
                    ImageView img1 = (ImageView) view.findViewById(R.id.img1);


                    text1.setText(entry[0]);
                    text2.setText(entry[1]);
                    text3.setText(entry[2]);
                    text4.setText(entry[3]);
                    text5.setText(entry[5]);
                    text6.setText(entry[6]);
                    text7.setText(entry[7]);

                    Picasso.with(getBaseContext()).load(entry[4]).resize(200, 200).centerCrop().into(img1);

                    final Button button1 = (Button) view.findViewById(R.id.button1);
                    final Button button2 = (Button) view.findViewById(R.id.button2);



                    int liked = Integer.parseInt(text7.getText().toString());

                    if (liked > 0) {
                        button1.setTextColor(Color.parseColor("#FF5722"));
                    } else if(liked < 0){
                        button2.setTextColor(Color.parseColor("#00B0FF"));
                    }

                    button1.setOnClickListener(new AdapterView.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            View parentRow = (View) view.getParent();
                            TextView c = (TextView) parentRow.findViewById(R.id.text6);
                            String s = c.getText().toString();
                            new upvote(s).execute();
                            button1.setTextColor(Color.parseColor("#FF5722"));
                            button2.setTextColor(Color.WHITE);


                        }
                    });


                    button2.setOnClickListener(new AdapterView.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            View parentRow = (View) view.getParent();
                            TextView c = (TextView) parentRow.findViewById(R.id.text6);
                            String s = c.getText().toString();
                            new downvote(s).execute();
                            button2.setTextColor(Color.parseColor("#00B0FF"));
                            button1.setTextColor(Color.WHITE);

                        }
                    });


                    return view;


                }
            });




        }
    }

    private class upvote extends AsyncTask<String,Void,String>{
        private String helper;

        public upvote(String helper) {
            this.helper = helper;

        }


        @Override
        protected String doInBackground(String... params) {

            Submission submission = redditClient.getSubmission(helper);
            VoteDirection newVoteDirection = submission.getVote() == VoteDirection.NO_VOTE ? VoteDirection.UPVOTE : VoteDirection.NO_VOTE;
            net.dean.jraw.managers.AccountManager accountManager = new net.dean.jraw.managers.AccountManager(redditClient);

            try {
                accountManager.vote(submission, newVoteDirection);
            } catch (ApiException e) {

            }

            return null;
        }

        protected void onPostExecute(String string){
            Toast.makeText(MainActivity.this, "Upvoted", Toast.LENGTH_SHORT).show();
        }
    }


    private class downvote extends AsyncTask<String,Void,String>{
        private String helper;

        public downvote(String helper) {
            this.helper = helper;

        }


        @Override
        protected String doInBackground(String... params) {

            Submission submission = redditClient.getSubmission(helper);
            VoteDirection newVoteDirection = submission.getVote() == VoteDirection.NO_VOTE ? VoteDirection.DOWNVOTE : VoteDirection.NO_VOTE;
            net.dean.jraw.managers.AccountManager accountManager = new net.dean.jraw.managers.AccountManager(redditClient);

            try {
                accountManager.vote(submission, newVoteDirection);
            } catch (ApiException e) {

            }

            return null;
        }

        protected void onPostExecute(String string){
            Toast.makeText(MainActivity.this, "Downvoted", Toast.LENGTH_SHORT).show();
        }
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

            redditClient.authenticate(oAuthData);

            setContentView(R.layout.activity_main);

            getActionBar().show();
            listView = (ListView) findViewById(R.id.listView);
            //textView = (TextView) findViewById(R.id.headline_subreddit);
            Intent intent = getIntent();

            chosenSubreddit = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

            if (chosenSubreddit == null) {
                setTitle("frontpage");
            } else {
                setTitle(chosenSubreddit);
            }
            new loginUser().execute();
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView c = (TextView) view.findViewById(R.id.text5);
                    String s = c.getText().toString();
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(browserIntent);*/
                    Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, s);
                    startActivity(intent);
                    return true;
                }
            });



                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            TextView c = (TextView) view.findViewById(R.id.text4);
                            String s = c.getText().toString();
                            Intent intent = new Intent(MainActivity.this, WebActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, s);
                            startActivity(intent);
               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(browserIntent);*/
                        }


                    });

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerList = (ListView) findViewById(R.id.left_drawer);

            mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.iamzzleeping_password, R.string.iamzzleeping_password);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            drawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String selectedFromList = (drawerList.getItemAtPosition(position).toString());
                    hej.show();
                    new getLinks().execute(selectedFromList);
                    itemList.clear();
                    drawerLayout.closeDrawers();
                    drawerList.setItemChecked(position, true);
                    setTitle(selectedFromList);
                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, selectedFromList);
                startActivity(intent);
                */
                }
            });



            new getLinks().execute("frontpage");
            new getsubreddits().execute();



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(mDrawerToggle.onOptionsItemSelected(item)){
           if(drawerLayout.isDrawerOpen(GravityCompat.START)){
               drawerLayout.closeDrawers();
           }else{
               drawerLayout.openDrawer(GravityCompat.START);
           }

        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        clearPreferences();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear com.hyco.netreddit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

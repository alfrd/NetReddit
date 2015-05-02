package com.hyco.netreddit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
<<<<<<< HEAD
<<<<<<< HEAD
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
=======
>>>>>>> parent of 15c641c... Johan
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.v7.app.ActionBarDrawerToggle;

=======
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
>>>>>>> origin/master
=======
>>>>>>> parent of 15c641c... Johan
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
<<<<<<< HEAD
<<<<<<< HEAD
import java.text.DateFormat;
import java.text.SimpleDateFormat;
=======
>>>>>>> parent of 15c641c... Johan
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
<<<<<<< HEAD
import java.util.Locale;
=======
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
>>>>>>> origin/master
=======
>>>>>>> parent of 15c641c... Johan


public class MainActivity extends Activity {
    ListView listView;
    private TextView textView;
    private List<String[]> itemList = new LinkedList<String[]>();
<<<<<<< HEAD
<<<<<<< HEAD
=======
    String subredditName;
>>>>>>> origin/master
=======
    String subredditName;
>>>>>>> parent of 15c641c... Johan

    private ArrayList<String> subredditList;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
<<<<<<< HEAD
<<<<<<< HEAD
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog hej;
    String chosenSubreddit;
    public final static String EXTRA_MESSAGE = "com.hyco.netreddit.MESSAGE";
=======
    String chosenSubreddit;
>>>>>>> origin/master
=======
    String chosenSubreddit;
>>>>>>> parent of 15c641c... Johan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().show();
<<<<<<< HEAD
<<<<<<< HEAD



        listView = (ListView) findViewById(R.id.listView);
        //textView = (TextView) findViewById(R.id.headline_subreddit);
        Intent intent = getIntent();

        chosenSubreddit = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if (chosenSubreddit == null){
            setTitle("frontpage");
        }else{
            setTitle(chosenSubreddit);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView c = (TextView) view.findViewById(R.id.text4);
                String s = c.getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(browserIntent);
            }
        });
=======
        setTitle("Frontpage");
        chosenSubreddit = "gunners";
        subredditName = "Frontpage";
        listView = (ListView) findViewById(R.id.listView);
        //textView = (TextView) findViewById(R.id.headline_subreddit);
>>>>>>> origin/master
=======
        setTitle("Frontpage");
        chosenSubreddit = "gunners";
        subredditName = "Frontpage";
        listView = (ListView) findViewById(R.id.listView);
        //textView = (TextView) findViewById(R.id.headline_subreddit);
>>>>>>> parent of 15c641c... Johan
        new getLinks().execute();

        String[] osArray = { "android", "soccer", "all", "pics", "videos" };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, osArray));
<<<<<<< HEAD
<<<<<<< HEAD
        mDrawerToggle = new ActionBarDrawerToggle (this,drawerLayout,R.string.iamzzleeping_password,R.string.iamzzleeping_password);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedFromList = (drawerList.getItemAtPosition(position).toString());
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, selectedFromList);
                startActivity(intent);
                drawerLayout.closeDrawers();
            }
        });
=======
>>>>>>> parent of 15c641c... Johan

        //drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



<<<<<<< HEAD
=======

        //drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



    private class getLinks extends AsyncTask<Void,Void,List<String[]>> {
>>>>>>> origin/master
=======
    private class getLinks extends AsyncTask<Void,Void,List<String[]>> {
>>>>>>> parent of 15c641c... Johan


        @Override
        protected List<String[]> doInBackground(Void... arg0) {
            UserAgent myUserAgent = UserAgent.of("Android", "com.hyco.netreddi", "0.1", "iamzzleeping");

            RedditClient redditClient = new RedditClient(myUserAgent);
<<<<<<< HEAD
            String psswd = getString(R.string.iamzzleeping_password);

<<<<<<< HEAD
            Credentials credentials = Credentials.script("", psswd , "ClientID", "ClientSecret");
=======


            Credentials credentials = Credentials.script("iamzzleeping", "PLEASE INSERT PASSWORD" , "gYCAsAZbxsXdAA", "j6AjliaTCY1r8_tSP86mVyROJJo");
>>>>>>> origin/master
=======
            Credentials credentials = Credentials.script("iamzzleeping", psswd , "gYCAsAZbxsXdAA", "j6AjliaTCY1r8_tSP86mVyROJJo");
>>>>>>> parent of 15c641c... Johan

            try {

                OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
                redditClient.authenticate(authData);
            } catch(OAuthException e) {
                e.printStackTrace();
            }

<<<<<<< HEAD
<<<<<<< HEAD
            SubredditPaginator frontPage = new SubredditPaginator(redditClient,chosenSubreddit);

            Listing<Submission> submissions = frontPage.next();
            for (Submission s : submissions) {
                itemList.add(new String[]{s.getTitle(), s.getCommentCount() + " comments","u/" + s.getAuthor() + " * r/" + s.getSubredditName() + " * " + s.getScore() + " points",s.getUrl() });
=======
            SubredditPaginator frontPage = new SubredditPaginator(redditClient);

            Listing<Submission> submissions = frontPage.next();
            for (Submission s : submissions) {

                itemList.add(new String[]{s.getTitle(),s.getAuthor()});
>>>>>>> origin/master
=======
            SubredditPaginator frontPage = new SubredditPaginator(redditClient);

            Listing<Submission> submissions = frontPage.next();
            for (Submission s : submissions) {

                itemList.add(new String[]{s.getTitle(),s.getAuthor()});
>>>>>>> parent of 15c641c... Johan
            }




<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> parent of 15c641c... Johan









<<<<<<< HEAD
>>>>>>> origin/master
=======
>>>>>>> parent of 15c641c... Johan
            /*try {
                URL subredditURL = new URL(
                        "http://www.reddit.com/r/" + chosenSubreddit + ".json?limit=15");
                URLConnection tc = subredditURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(tc
                        .getInputStream()));

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
        protected void onPostExecute(List<String[]> list){

<<<<<<< HEAD
<<<<<<< HEAD
            hej.dismiss();
=======
>>>>>>> origin/master
=======
>>>>>>> parent of 15c641c... Johan
            listView.setAdapter(new ArrayAdapter<String[]>(
                    MainActivity.this,
                    R.layout.post_list,
                    R.id.text1,
                    itemList) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {


                    View view = super.getView(position, convertView, parent);


                    String[] entry = itemList.get(position);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
<<<<<<< HEAD
<<<<<<< HEAD
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    TextView text4 = (TextView) view.findViewById(R.id.text4);

                    text1.setText(entry[0]);
                    text2.setText(entry[1]);
                    text3.setText(entry[2]);
                    text4.setText(entry[3]);
=======
                    text1.setText(entry[0]);
                    text2.setText(entry[1]);

>>>>>>> origin/master
=======
                    text1.setText(entry[0]);
                    text2.setText(entry[1]);

>>>>>>> parent of 15c641c... Johan
                    return view;


                }
            });

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
<<<<<<< HEAD
<<<<<<< HEAD



=======
>>>>>>> origin/master
=======
>>>>>>> parent of 15c641c... Johan
}

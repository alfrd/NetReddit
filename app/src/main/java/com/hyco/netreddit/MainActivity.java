package com.hyco.netreddit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity {
    ListView listView;
    private TextView textView;
    private List<String[]> itemList = new LinkedList<String[]>();
    String subredditName;

    private ArrayList<String> subredditList;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    String chosenSubreddit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().show();
        setTitle("Frontpage");
        chosenSubreddit = "gunners";
        subredditName = "Frontpage";
        listView = (ListView) findViewById(R.id.listView);
        //textView = (TextView) findViewById(R.id.headline_subreddit);
        new getLinks().execute();

        String[] osArray = { "android", "soccer", "all", "pics", "videos" };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, osArray));

        //drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



    private class getLinks extends AsyncTask<Void,Void,List<String[]>> {


        @Override
        protected List<String[]> doInBackground(Void... arg0) {
            UserAgent myUserAgent = UserAgent.of("Android", "com.hyco.netreddi", "0.1", "iamzzleeping");

            RedditClient redditClient = new RedditClient(myUserAgent);
            String psswd = getString(R.string.iamzzleeping_password);

            Credentials credentials = Credentials.script("iamzzleeping", psswd , "gYCAsAZbxsXdAA", "j6AjliaTCY1r8_tSP86mVyROJJo");

            try {

                OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
                redditClient.authenticate(authData);
            } catch(OAuthException e) {
                e.printStackTrace();
            }

            SubredditPaginator frontPage = new SubredditPaginator(redditClient);

            Listing<Submission> submissions = frontPage.next();
            for (Submission s : submissions) {

                itemList.add(new String[]{s.getTitle(),s.getAuthor()});
            }













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
                    text1.setText(entry[0]);
                    text2.setText(entry[1]);

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
}

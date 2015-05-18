package com.hyco.netreddit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.UserSubredditsPaginator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity {
    ListView listView;
    private TextView textView;
    private List<Object[]> itemList = new LinkedList<Object[]>();
    private RedditClient redditClient;
    private ArrayList<String> subredditList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog hej;
    String chosenSubreddit;
    public final static String EXTRA_MESSAGE = "com.hyco.netreddit.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        new loginUser().execute();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.iamzzleeping_password, R.string.iamzzleeping_password);

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


    }

    private class loginUser extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            hej = new ProgressDialog(MainActivity.this);
            hej.setMessage("Loading");
            hej.show();

        }
        protected String doInBackground(Void... arg0) {
            String done = "done";
            UserAgent myUserAgent = UserAgent.of("Android", "com.hyco.netreddi", "0.1", "iamzzleeping");

            redditClient = new RedditClient(myUserAgent);
            String psswd = getString(R.string.iamzzleeping_password);

            Credentials credentials = Credentials.script("mrsvedberg", psswd, "gYCAsAZbxsXdAA", "j6AjliaTCY1r8_tSP86mVyROJJo");

            try {

                OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
                redditClient.authenticate(authData);
            } catch (OAuthException e) {
                e.printStackTrace();
            }
            return done;
        }

        protected void onPostExecute(String s) {
            new getLinks().execute("frontpage");
            new getsubreddits().execute();
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


    private class getLinks extends AsyncTask<String, Void, List<Object[]>> {

        @Override
        protected List<Object[]> doInBackground(String... string) {
            SubredditPaginator current;
            if (string[0] == null || string[0].equals("frontpage")) {
                current = new SubredditPaginator(redditClient);
            } else {
                current = new SubredditPaginator(redditClient, string[0]);
            }

            Listing<Submission> submissions = current.next();
            for (Submission s : submissions) {

                itemList.add(new Object[]{s.getTitle(), s.getCommentCount() + " comments" + " * " + s.getDomain(), "u/" + s.getAuthor() + " * r/" + s.getSubredditName() + " * " + s.getScore() + " points", s.getUrl(),s.getThumbnail(),"https://www.reddit.com" +s.getPermalink() + ".json"});
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

        protected void onPostExecute(List<Object[]> list) {

            hej.dismiss();
            listView.setAdapter(new ArrayAdapter<Object[]>(
                    MainActivity.this,
                    R.layout.post_list,
                    R.id.text1,
                    itemList) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {


                    View view = super.getView(position, convertView, parent);


                    Object[] entry = itemList.get(position);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    TextView text4 = (TextView) view.findViewById(R.id.text4);
                    TextView text5 = (TextView) view.findViewById(R.id.text5);

                    ImageView img1 = (ImageView) view.findViewById(R.id.img1);



                    text1.setText(entry[0].toString());
                    text2.setText(entry[1].toString());
                    text3.setText(entry[2].toString());
                    text4.setText(entry[3].toString());
                    text5.setText(entry[5].toString());

                    if(entry[4] != null) {
                        Picasso.with(getBaseContext()).load(entry[4].toString()).resize(200, 200).centerCrop().into(img1);
                    }
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

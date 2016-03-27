package com.hyco.netreddit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class CommentsActivity extends Activity {
    private String comments;
    private ProgressDialog hej;
    private ListView listView;
    private String selfpost_title;
    private List<String[]> itemList = new LinkedList<String[]>();
    private TextView tv;
    private ImageView im;
    private String selfpost_text;
    private Boolean is_selfpost;
    private String flair;
    private String author;
    private String score;
    private String subreddit;
    private String imgurl;
    private Boolean hasthumb = true;
    private String thumbnail;


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        comments = intent.getStringExtra(WebActivity.EXTRA_MESSAGE);
        listView = (ListView) findViewById(R.id.list);

        is_selfpost = false;
        tv = new TextView(this);
        im = new ImageView(this);


        setTitle("Comments");
        new getComments().execute();


    }


    private class getComments extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            hej = new ProgressDialog(CommentsActivity.this);
            hej.setMessage("Loading");
            hej.show();

        }

        protected String doInBackground(Void... arg0) {
            String s = "done";

            try {
                URL subredditURL = new URL(
                        comments);
                URLConnection tc = subredditURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(tc
                        .getInputStream()));

                String raw = in.readLine();

                JSONArray r = new JSONArray(raw)
                        .getJSONObject(1)
                        .getJSONObject("data")
                        .getJSONArray("children");

                JSONArray selfpostarray = new JSONArray(raw)
                        .getJSONObject(0)
                        .getJSONObject("data").getJSONArray("children");
                JSONObject selfpost = selfpostarray.getJSONObject(0).getJSONObject("data");
                if (selfpost.getBoolean("is_self")) {
                    selfpost_text = selfpost.getString("selftext");
                    is_selfpost = true;
                }
                selfpost_title = selfpost.getString("title");
                flair = selfpost.getString("link_flair_text");
                score = selfpost.getString("score");
                author = selfpost.getString("author");
                subreddit = selfpost.getString("subreddit");
                imgurl = selfpost.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getString("url");
                thumbnail = selfpost.getString("thumbnail");

                if (thumbnail.equals("null")) {
                    hasthumb = false;
                }

                for (int i = 0; i < r.length(); i++) {

                    if (r.getJSONObject(i).optString("kind") == null)
                        continue;


                    if (!r.getJSONObject(i).optString("kind").equals("t1"))
                        continue;
                    JSONObject data = r.getJSONObject(i).getJSONObject("data");

                    itemList.add(new String[]{data.getString("author") + " (" + data.getString("score") + " points" + ") ", data.getString("body")});
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String s) {
            hej.dismiss();
            setTitle(subreddit);
            listView.addHeaderView(tv);
            tv.setTextColor(Color.WHITE);
            tv.append(Html.fromHtml("<h1>" + selfpost_title + "</h1>"));

            if (is_selfpost) {
                tv.setTextSize(14);
                tv.append(Html.fromHtml("<h6>" + flair + " * " + author + " * " + score + " points" + "</h6>"));
                tv.append(selfpost_text);
            } else if (hasthumb) {
                listView.addHeaderView(im);
                tv.append(Html.fromHtml("<strong>" + flair + " * " + author + " * " + score + " points" + "<strong>"));
                Picasso.with(getBaseContext()).load(imgurl).centerCrop().resize(1350, 700).into(im);
            } else {
                tv.append(Html.fromHtml("<strong>" + flair + " * " + author + " * " + score + " points" + "<strong>"));

            }

            tv.setPadding(25, 25, 25, 25);

            Linkify.addLinks(tv, Linkify.ALL);
            listView.setAdapter(new ArrayAdapter<String[]>(
                    CommentsActivity.this,
                    R.layout.comments_list,
                    R.id.text1,
                    itemList) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    String[] entry = itemList.get(position);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);


                    text1.setText(Html.fromHtml("<strong>" + entry[0] + "<strong>"));
                    text2.setText(entry[1]);
                    Linkify.addLinks(text2, Linkify.ALL);

                    return view;


                }
            });

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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

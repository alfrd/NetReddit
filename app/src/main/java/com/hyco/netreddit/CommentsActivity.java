package com.hyco.netreddit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CommentsActivity extends Activity {
    private String comments;
    private ProgressDialog hej;
    private ListView listView;
    private List<String[]> itemList = new LinkedList<String[]>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent intent = getIntent();
        comments = intent.getStringExtra(WebActivity.EXTRA_MESSAGE);
        listView = (ListView) findViewById(R.id.list);
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
          /* CommentsLoader comments = new CommentsLoader("yo");
            body = comments.fetchComments().get(0).toString();*/
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

                for (int i = 0; i < r.length(); i++) {
                    if (r.getJSONObject(i).optString("kind") == null)

                        continue;
                    if (!r.getJSONObject(i).optString("kind").equals("t1"))
                        continue;
                    JSONObject data = r.getJSONObject(i).getJSONObject("data");

                    itemList.add(new String[]{data.getString("author") + " * " + data.getString("score") + " points", data.getString("body")});

                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String s) {
            hej.dismiss();
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

                    String html = entry[1];


                    text1.setText(entry[0]);
                    text2.setText(html);


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

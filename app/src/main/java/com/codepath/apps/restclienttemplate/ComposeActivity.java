package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;

    EditText etCompose;
    Button btnTweet;

    String replyUsername;
    Long replyId;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        Intent intent = getIntent();
        replyUsername = intent.getStringExtra("REPLY_USERNAME");
        replyId = intent.getLongExtra("REPLY_ID", -1);
        Log.i(TAG, "in reply to user: " + replyUsername + " id: " + replyId.toString());

        if (replyUsername != null) {
            etCompose.setText("@" + replyUsername);
        }

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (replyUsername == null) {
                    //make an API call to Twitter to publish the tweet
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "published tweet says: " + tweet.body);
                                Intent intent = new Intent();
                                //set result code and bundle data for response
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                //close the activity and pass data to parent
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publish tweet", throwable);
                        }
                    });
                } else {
                    //make an API call to Twitter to reply to tweet
                    client.replyTweet(tweetContent, replyId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "replied published tweet says: " + tweet.body);
                                Intent intent = new Intent();
                                //set result code and bundle data for response
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                //close the activity and pass data to parent
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publish tweet", throwable);
                        }
                    });
                }
            }
        });

        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(replyUsername);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void showEditDialog(String content) {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(content);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    // This method is invoked in the activity when the listener is triggered
    // Access the data result passed to the activity here
    public void onFinishEditDialog(String tweetContent) {
        if (tweetContent.isEmpty()) {
            Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
            showEditDialog();
        }
        if (tweetContent.length() > MAX_TWEET_LENGTH) {
            Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
            showEditDialog(tweetContent);
        }
        if (replyUsername == null) {
            //make an API call to Twitter to publish the tweet
            client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        Log.i(TAG, "published tweet says: " + tweet.body);
                        Intent intent = new Intent();
                        //set result code and bundle data for response
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        //close the activity and pass data to parent
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to publish tweet", throwable);
                }
            });
        } else {
            //make an API call to Twitter to reply to tweet
            client.replyTweet(tweetContent, replyId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        Log.i(TAG, "replied published tweet says: " + tweet.body);
                        Intent intent = new Intent();
                        //set result code and bundle data for response
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        //close the activity and pass data to parent
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to publish tweet", throwable);
                }
            });
        }
    }
}
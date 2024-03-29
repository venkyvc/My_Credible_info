package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalDetailsActivity extends AppCompatActivity {

   @BindView(R.id.user_name_edit_text)
   EditText mUserName;
   @BindView(R.id.user_mobile_edit_text)
   EditText mUserMobile;
   @BindView(R.id.user_location_edit_text)
   EditText mUserLocation;
   @BindView(R.id.user_links_edit_text)
   EditText mUserLinks;
   @BindView(R.id.save_personal_details_button)
   Button mSaveButton;

    private String mUrl = "http://139.59.65.145:9090//user/personaldetail/";
    private SessionManager mSessionManager;
    private VolleySingleton mVolleySingleton;
    private List<UserData> PersonalDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        mSessionManager = new SessionManager(this);
        PersonalDetails = new ArrayList<>();

        final Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("user_id");
        final String flag = bundle.getString("flag");
        String name = bundle.getString("name");
        String mobile = bundle.getString("mobile");
        String links = bundle.getString("links");
        String location = bundle.getString("location");

        if(flag.equals("Add")){
            mUserName.setText("");
            mUserMobile.setText("");
            mUserLinks.setText("");
            mUserLocation.setText("");
        } else if (flag.equals("Edit")){
            mUserName.setText(name);
            mUserMobile.setText(mobile);
            mUserLinks.setText(links);
            mUserLocation.setText(location);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewVolleyRequest();
                if (flag.equals("Add")) {
                    setNewData(id, Request.Method.POST);

                } else if (flag.equals("Edit")) {
                    setNewData(id, Request.Method.PUT);
                }
            }
        });
    }

    private void setNewVolleyRequest(){
        mVolleySingleton = VolleySingleton.getInstance(this);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue requestQueue = mVolleySingleton.getRequestQueue(cache, network);
        requestQueue.start();

    }

    private void setNewData(String id, int method){
        setUserData();
        mUrl += id;
        JSONObject userCredential = new JSONObject();
        try {
            userCredential.put("name", PersonalDetails.get(0).getmName());
            userCredential.put("mobile_no", PersonalDetails.get(0).getmMobile());
            userCredential.put("links", PersonalDetails.get(0).getmLinks());
            userCredential.put("location", PersonalDetails.get(0).getmLocation());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(method,
                mUrl, userCredential,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response-personal-info", response.toString());
                        Toast.makeText(getApplicationContext(), "Your Details Are Successfully Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        mVolleySingleton.addToRequestQueue(request);
    }

    private void setUserData(){

        String userName = mUserName.getText().toString().trim();
        String userMobile = mUserMobile.getText().toString().trim();
        String userLocations = mUserLocation.getText().toString().trim();
        String userLinks = mUserLinks.getText().toString().trim();

        PersonalDetails.add(new UserData(userName, userLocations, userLinks, userMobile));

    }

}

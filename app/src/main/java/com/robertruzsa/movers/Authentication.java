package com.robertruzsa.movers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.HashMap;

public class Authentication {

    public static void requestPhoneVerification(String phoneNumber){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("phoneNumber", phoneNumber);

        ParseCloud.callFunctionInBackground("sendVerificationCode", params, new FunctionCallback<JSONObject>() {
            public void done(JSONObject response, ParseException e) {
                if (e == null) {
                    Log.d("Response", "no exceptions! "/* + response.toString()*/);
                    // Code sent successfully you have to wait it or ask the user to enter the code for verification
                } else {
                    Log.i("Response", "Exception: "  + e);
                }
            }
        });
    }


}

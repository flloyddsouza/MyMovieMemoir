package com.flloyd.mymoviememoir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Login extends AppCompatActivity {

    NetworkConnection networkConnection = null;
    EditText emailText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkConnection = new NetworkConnection();
        Log.i("Flloyd: ", "Start");

        Button Login = findViewById(R.id.login);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String e = emailText.getText().toString();
              String p = null;
                try {
                    p = hashPassword(passwordText.getText().toString());
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                Log.i("Flloyd: ", "Email:" + e);
              Log.i("Flloyd: ", "Password:" + p);
              Authenticate authenticate = new Authenticate();
              if(validate())
                  authenticate.execute(e,p);
            }
        });

    }

    private class Authenticate extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(@NotNull String... params) {
            return networkConnection.getCredintials(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            String test = "User";
            if(result != null) {
                try {
                    test = result.getJSONObject("personid").getString("personfname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Hello " + test, Toast.LENGTH_SHORT).show();
                Log.i("Flloyd: ", "Result after login: " + result.toString());
                Intent intent =  new Intent(Login.this, MovieMemoir.class);
                try {
                    intent.putExtra("Person", String.valueOf(result.getJSONObject("personid")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK,intent);
                startActivity(intent);
                finish();
            }
            else
               Toast.makeText(getApplicationContext(), "Incorrect Username Or Password",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate() {

        boolean valid = true;
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty()) {
            passwordText.setError("Password cannot be empty");
            valid = false;
        }else if(password.length() < 6 )
        {
            passwordText.setError("Alpha numeric character greater than 6");
        }
        else {
            passwordText.setError(null);
        }
        return valid;
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : hashInBytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}

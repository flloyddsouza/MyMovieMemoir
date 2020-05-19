package com.flloyd.mymoviememoir;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flloyd.mymoviememoir.networkConnection.NetworkConnection;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register extends AppCompatActivity {

    NetworkConnection networkConnection = null;
    EditText fName, lName,dateOfBirth, address, postalCode, emailText, passwordText,passwordConfirm;
    Spinner state;
    RadioGroup gender;
    Boolean validation;
    String firstName,lastName,personGender,DOB,streetAddress,stateCode,postCode,email,password;
    TextInputLayout fNameLayout,lNameLayout,DOBLayout,AddressLayout,postCodeLayout,emailLayout,passwordLayout,passwordConfirmLayout;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button submit = findViewById(R.id.register);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        gender = findViewById(R.id.input_layout_gender);
        address = findViewById(R.id.streetAddress);
        state = findViewById(R.id.stateSpinner);
        postalCode = findViewById(R.id.postCode);
        emailText = findViewById(R.id.newEmail);
        passwordText = findViewById(R.id.newPassword);
        networkConnection = new NetworkConnection();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateOfBirth.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = fName.getText().toString().trim();
                lastName = lName.getText().toString().trim();
                switch (gender.getCheckedRadioButtonId()){
                    case R.id.male:
                        personGender ="M";
                        break;
                    case R.id.female:
                        personGender ="F";
                        break;
                    default:
                        personGender = "NA";
                }
                DOB = dateOfBirth.getText().toString();
                streetAddress = address.getText().toString().trim();
                stateCode = state.getSelectedItem().toString();
                postCode = postalCode.getText().toString();
                email = emailText.getText().toString().trim();
                password = null;
                try {
                    password = hashPassword(passwordText.getText().toString());
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }

                Log.i("Flloyd", "Fname: " + firstName);
                Log.i("Flloyd", "Lname: " + lastName);
                Log.i("Flloyd", "Gender: " + personGender);
                Log.i("Flloyd", "DOB: " +DOB);
                Log.i("Flloyd", "Address: " + streetAddress);
                Log.i("Flloyd", "State: " + stateCode);
                Log.i("Flloyd", "Post Code: " + postCode);
                Log.i("Flloyd", "Email: " + email);
                Log.i("Flloyd", "Password: " + password);
                validation = Validate();
            }
        });

    }


    private class emailCheck extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(@NotNull String... params) {
            return networkConnection.emailChecker(params[0]);
        }
        @Override
        protected void onPostExecute(JSONArray result) {
            emailLayout = findViewById(R.id.input_layout_new_email);
            if(result != null && !result.isNull(0)){
                emailLayout.setError("Email already exists");
                Log.i("Flloyd: ", "Result after email check " + result.toString());
            }else if(result == null) {
                Toast.makeText(getApplicationContext(), "Network Error. Try Again!", Toast.LENGTH_SHORT).show();
            }else if (result.isNull(0)){
                Toast.makeText(getApplicationContext(), "Email Available", Toast.LENGTH_SHORT).show();
                if(validation)
                {
                    registerToDatabase rg = new registerToDatabase();
                    rg.execute();
                }

            }
        }
    }


    private class registerToDatabase extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(@NotNull String... params) {
            return networkConnection.register(firstName,lastName,personGender,DOB,streetAddress,stateCode,postCode,email,password);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("Flloyd ","result: " + result);
            if (result.equals("ERROR")){
                Toast.makeText(getApplicationContext(), "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
            }else{
                SharedPreferences sharedPref = getSharedPreferences("DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                String shareCredential = "[" + result + "]";
                spEditor.putString("Credential", shareCredential);
                spEditor.apply();
                Intent intent =  new Intent(Register.this, MyMovieMemoir.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public Boolean Validate()
    {
        fNameLayout = findViewById(R.id.input_layout_name);
        Boolean valid = Boolean.TRUE;
        if(firstName.trim().isEmpty()){
            fNameLayout.setError("First name cannot be empty");
            valid = Boolean.FALSE;
        }else if(firstName.length() > 20){
            fNameLayout.setError("First name cannot exceed 20 characters");
            valid = Boolean.FALSE;
        }
        else {
            fNameLayout.setError(null);
        }

        lNameLayout = findViewById(R.id.input_layout_surname);
        if(lastName.trim().isEmpty()){
            lNameLayout.setError("Last name cannot be empty");
            valid = Boolean.FALSE;
        }else if(lastName.length() > 20){
            lNameLayout.setError("Last name cannot exceed 20 characters");
            valid = Boolean.FALSE;
        }
        else {
            lNameLayout.setError(null);
        }

        DOBLayout = findViewById(R.id.input_layout_DOB);
        if(DOB.isEmpty()){
            DOBLayout.setError("Select the Date Of Birth ");
            valid = Boolean.FALSE;
        }
        else {
            DOBLayout.setError(null);
        }

        AddressLayout = findViewById(R.id.input_layout_address);
        if(streetAddress.trim().isEmpty()){
            AddressLayout.setError("Address cannot be empty");
            valid = Boolean.FALSE;
        }else if(streetAddress.length() > 50){
            AddressLayout.setError("Address cannot exceed 50 characters");
            valid = Boolean.FALSE;
        }else {
            AddressLayout.setError(null);
        }

        postCodeLayout = findViewById(R.id.input_layout_postCode);
        if (postCode.trim().isEmpty()){
            postCodeLayout.setError("Post Code cannot be empty");
            valid = Boolean.FALSE;
        }else if (postCode.length() != 4){
            postCodeLayout.setError("Enter a valid Post Code");
            valid = Boolean.FALSE;
        }else {
            postCodeLayout.setError(null);
        }

        emailLayout = findViewById(R.id.input_layout_new_email);
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            valid = Boolean.FALSE;
        }else {
            emailLayout.setError(null);
        }

        if(!email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailCheck emailCheck = new emailCheck();
            emailCheck.execute(email);
        }

        passwordText = findViewById(R.id.newPassword);
        String _password = passwordText.getText().toString();
        passwordLayout = findViewById(R.id.input_layout_new_password);
        if (_password.isEmpty()) {
            passwordLayout.setError("Password cannot be empty");
            valid = Boolean.FALSE;
        }else if(_password.length() < 6 )
        {
            valid = Boolean.FALSE;
            passwordLayout.setError("Alpha numeric character greater than 6");
        }
        else {
            passwordLayout.setError(null);
        }

        passwordConfirm = findViewById(R.id.confirmPassword);
        String confirm_Password = passwordConfirm.getText().toString();
        passwordConfirmLayout = findViewById(R.id.input_layout_confirm_password);
        if (!confirm_Password.equals(_password) || confirm_Password.isEmpty()){
            valid = Boolean.FALSE;
            passwordConfirmLayout.setError("Password does not match");
        }else{
            passwordConfirmLayout.setError(null);
        }
        return valid;
    }


    public String hashPassword(@NotNull String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : hashInBytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}

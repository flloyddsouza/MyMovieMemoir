package com.flloyd.mymoviememoir;

import android.app.DatePickerDialog;
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


                String firstName = fName.getText().toString();
                Log.i("Flloyd", "Fname: " + firstName);

                String lastName = lName.getText().toString();
                Log.i("Flloyd", "Lname: " + lastName);

                String personGender;
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
                Log.i("Flloyd", "Gender: " + personGender);

                String DOB = dateOfBirth.getText().toString();
                Log.i("Flloyd", "DOB: " +DOB);

                String streetAddress = address.getText().toString();
                Log.i("Flloyd", "Address: " + streetAddress);

                String stateCode = state.getSelectedItem().toString();
                Log.i("Flloyd", "State: " + stateCode);

                String postCode = postalCode.getText().toString();
                Log.i("Flloyd", "Post Code: " + postCode);

                String email = emailText.getText().toString();
                Log.i("Flloyd", "Email: " + email);

                String password = null;
                try {
                    password = hashPassword(passwordText.getText().toString());
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                Log.i("Flloyd", "Password: " + password);

                Validate();

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
                //Todo matched email
                emailLayout.setError("Email already exists");
                Log.i("Flloyd: ", "Result after email check " + result.toString());
            }else if(result == null) {
                Toast.makeText(getApplicationContext(), "Network Error. Try Again!", Toast.LENGTH_SHORT).show();
            }else
            {
              //Todo No Match found
            }
        }
    }


    public boolean Validate()
    {
        boolean valid = true;

        fName = findViewById(R.id.fName);
        String firstName = fName.getText().toString();
        fNameLayout = findViewById(R.id.input_layout_name);
        if(firstName.trim().isEmpty()){
            fNameLayout.setError("First name cannot be empty");
            valid = false;
        }else if(firstName.length() > 20){
            fNameLayout.setError("First name cannot exceed 20 characters");
            valid = false;
        }
         else {
            fNameLayout.setError(null);
        }


        lName = findViewById(R.id.lName);
        String lastName = lName.getText().toString();
        lNameLayout = findViewById(R.id.input_layout_surname);
        if(lastName.trim().isEmpty()){
            lNameLayout.setError("Last name cannot be empty");
            valid = false;
        }else if(lastName.length() > 20){
            lNameLayout.setError("Last name cannot exceed 20 characters");
            valid = false;
        }
        else {
            lNameLayout.setError(null);
        }

        dateOfBirth = findViewById(R.id.dateOfBirth);
        String DOB = dateOfBirth.getText().toString();
        DOBLayout = findViewById(R.id.input_layout_DOB);
        if(DOB.isEmpty()){
            DOBLayout.setError("Select the Date Of Birth ");
            valid = false;
        }
        else {
            DOBLayout.setError(null);
        }


        address = findViewById(R.id.streetAddress);
        String streetAddress = address.getText().toString();
        AddressLayout = findViewById(R.id.input_layout_address);
        if(streetAddress.trim().isEmpty()){
            AddressLayout.setError("Address cannot be empty");
            valid = false;
        }else if(streetAddress.length() > 50){
            AddressLayout.setError("Address cannot exceed 50 characters");
            valid = false;
        }else {
            AddressLayout.setError(null);
        }


        postalCode = findViewById(R.id.postCode);
        String postCode = postalCode.getText().toString();
        postCodeLayout = findViewById(R.id.input_layout_postCode);
        if (postCode.trim().isEmpty()){
            postCodeLayout.setError("Post Code cannot be empty");
            valid = false;
        }else if (postCode.length() != 4){
            postCodeLayout.setError("Enter a valid Post Code");
            valid = false;
        }else {
            postCodeLayout.setError(null);
        }

        emailText = findViewById(R.id.newEmail);
        String email = emailText.getText().toString();
        emailLayout = findViewById(R.id.input_layout_new_email);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            valid = false;
        }else {
            emailLayout.setError(null);
        }

        if(!email.isEmpty()){
            emailCheck emailCheck = new emailCheck();
            emailCheck.execute(email);
        }

        passwordText = findViewById(R.id.newPassword);
        String password = passwordText.getText().toString();
        passwordLayout = findViewById(R.id.input_layout_new_password);
        if (password.isEmpty()) {
            passwordLayout.setError("Password cannot be empty");
            valid = false;
        }else if(password.length() < 6 )
        {
            valid = false;
            passwordLayout.setError("Alpha numeric character greater than 6");
        }
        else {
            passwordLayout.setError(null);
        }


        passwordConfirm = findViewById(R.id.confirmPassword);
        String confirmPassword = passwordConfirm.getText().toString();
        passwordConfirmLayout = findViewById(R.id.input_layout_confirm_password);
        if (!confirmPassword.equals(password) || confirmPassword.isEmpty()){
            valid = false;
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

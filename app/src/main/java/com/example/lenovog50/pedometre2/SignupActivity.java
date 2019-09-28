package com.example.lenovog50.pedometre2;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.data.SharedPrefsUtils;
import com.example.lenovog50.pedometre2.data.UserRepository;
import com.example.lenovog50.pedometre2.data.model.User;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {


    @BindView(R.id.button_login)
    TextView loginButton;
    @BindView(R.id.text_signup)
    TextView signupText;
    @BindView(R.id.editText_firstname)
    TextInputEditText firstNameText;
    @BindView(R.id.editText_lastname)
    TextInputEditText lastNameText;
    @BindView(R.id.editText_email)
    TextInputEditText emailText;
    @BindView(R.id.editText_password)
    TextInputEditText passwordText;
    @BindView(R.id.editText_confirm_password)
    TextInputEditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstNameText.getText().toString();
                String lastname = lastNameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();

                if (!StringUtils.isValidName(firstname))
                {
                    firstNameText.setError("erreur");
                    Toast.makeText(SignupActivity.this,"Veuillez vérifier les informations que vous avez entrer",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!StringUtils.isValidName(lastname))
                {
                    lastNameText.setError("erreur");
                    Toast.makeText(SignupActivity.this,"Veuillez vérifier les informations que vous avez entrer",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!StringUtils.isValidEmailAddress(email))
                {
                    emailText.setError("erreur");
                    Toast.makeText(SignupActivity.this,"Veuillez vérifier les informations que vous avez entrer",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!StringUtils.isValidPassword(password))
                {
                    passwordText.setError("erreur");
                    Toast.makeText(SignupActivity.this,"Veuillez vérifier les informations que vous avez entrer",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword))
                {
                    confirmPasswordText.setError("erreur");
                    Toast.makeText(SignupActivity.this,"Veuillez vérifier les informations que vous avez entrer",Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User(firstname,lastname,email,password);
                UserRepository userRepository = new UserRepository(SignupActivity.this);
                if (!userRepository.emailExists(user.getEmail()))
                {
                    userRepository.addUser(new User(firstname,lastname,email,password));
                    userRepository.connectUser(user);
                    Intent intent = new Intent(SignupActivity.this,PersonInformationActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SignupActivity.this,"Un utilisateur avec cet email" +
                            "existe deja",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

package com.example.lenovog50.pedometre2;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovog50.pedometre2.data.UserRepository;
import com.example.lenovog50.pedometre2.data.model.User;
import com.example.lenovog50.pedometre2.ui.main.MainActivity;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonInformationActivity extends AppCompatActivity {

    @BindView(R.id.editText_height)
    TextInputEditText heightEditText;
    @BindView(R.id.editText_weight)
    TextInputEditText weightEditText;
    @BindView(R.id.text_imc)
    TextView imcText;
    @BindView(R.id.text_ideal_weight)
    TextView idealWeightText;
    @BindView(R.id.layout_define_objectives)
    View defineObjectivesLayout;
    @BindView(R.id.text_next)
    TextView nextText;

    int height;
    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        ButterKnife.bind(this);
        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defineObjectivesLayout.getVisibility()==View.GONE)
                {
                    if (heightEditText.getText().toString().length()==0)
                    {
                        Toast.makeText(PersonInformationActivity.this,
                                "Veuillez vérifier les informaitons que vous avez saisi",
                                Toast.LENGTH_LONG).show();
                    }
                    if (weightEditText.getText().toString().length()==0)
                    {
                        Toast.makeText(PersonInformationActivity.this,
                                "Veuillez vérifier les informaitons que vous avez saisi",
                                Toast.LENGTH_LONG).show();
                    }
                    height = Integer.parseInt(heightEditText.getText().toString());
                    weight = Integer.parseInt(weightEditText.getText().toString());
                    defineObjectivesLayout.setVisibility(View.VISIBLE);
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    imcText.setText("Votre IMC : "+df.format(IMCUtils.getIMC(weight,height)));
                    idealWeightText.setText("Observation : "+IMCUtils.getRmq(IMCUtils.getIMC(weight,height)));
                }
                else
                {
                    UserRepository userRepository = new UserRepository(PersonInformationActivity.this);
                    User user = userRepository.getConnectedUser();
                    user.setHeight(height);
                    user.setWeight(weight);
                    userRepository.updateUser(user);
                    Intent intent = new Intent(PersonInformationActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

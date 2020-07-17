package com.example.filn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnSave;
    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;
    private TextView txtGetData;
    private Button btnGetAllData;

    private String allKickBoxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSave = findViewById(R.id.btnSave);
        btnGetAllData = findViewById(R.id.btnGetAllData);
        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPuchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);

        txtGetData = findViewById(R.id.txtGetData);

        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("SsC8nww0ZF", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object != null && e == null) {
                            txtGetData.setText(object.get("name") + " - " + "Punch Power: " + object.get("punchPower"));
                        } else {
                            Toast.makeText(SignUp.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allKickBoxers = "";
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject kickBoxer : objects) {
                                    allKickBoxers = allKickBoxers + kickBoxer.get("name") + "- Punch Power = " + kickBoxer.get("punchPower") + "\n";
                                }
                                FancyToast.makeText(SignUp.this, allKickBoxers, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            } else {
                                FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }
                    }
                });
            }
        });

        btnSave.setOnClickListener(SignUp.this);
    }

    @Override
    public void onClick(View view) {
        try {
            final ParseObject kickBoxer = new ParseObject("KickBoxer");
            kickBoxer.put("name", edtName.getText().toString());
            kickBoxer.put("punchSpeed", edtPunchSpeed.getText().toString());
            kickBoxer.put("punchPower", edtPunchPower.getText().toString());
            kickBoxer.put("kickSpeed", edtKickSpeed.getText().toString());
            kickBoxer.put("kickPower", edtKickPower.getText().toString());
            kickBoxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, kickBoxer.get("name") + " is added to the server successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
    }
}
package dev.form.formapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtName, txtContact, txtCity, txtComment;
    Button btnShare, btnAdd;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (EditText)findViewById(R.id.et_name);
        txtContact = (EditText)findViewById(R.id.et_contact);
        txtCity = (EditText)findViewById(R.id.et_city);
        txtComment = (EditText)findViewById(R.id.et_comment);
        btnShare = (Button)findViewById(R.id.btn_share);
        btnAdd = (Button)findViewById(R.id.btn_add_img);
        btnShare.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch(id)
        {
            case R.id.btn_add_img:

                //Pick multiple images from gallery.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);

                break;

            case R.id.btn_share:

                //Share Image via Email.


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    String imagePath = data.getStringExtra("data");
                    Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_LONG).show();
                    list.add("" + imagePath);
                }
                break;

        }
    }
}

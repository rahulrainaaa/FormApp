package dev.form.formapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtName, txtContact, txtCity, txtComment;
    Button btnShare, btnAdd;
    LinearLayout layout;
    ArrayList<String> list = new ArrayList<String>();
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }*/

        layout = (LinearLayout) findViewById(R.id.linearlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_appear);
        layout.startAnimation(animation);
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
    protected void onDestroy()
    {
        list = null;
        animation = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch(id)
        {
            case R.id.btn_add_img:

                //RUNTIME PERMISSION Android M
               /* if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Toast.makeText(getApplicationContext(), "Go to permissions and \nEnable the Storage permission", Toast.LENGTH_LONG).show();
                    final Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    MainActivity.this.startActivity(i);
                    break;
                }*/

                //Pick multiple images from gallery.
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

                break;

            case R.id.btn_share:

                String name = txtName.getText().toString().trim();
                String contact = txtContact.getText().toString().trim();
                String city = txtCity.getText().toString().trim();
                String comment = txtComment.getText().toString().trim();
                String msgBody = comment + "\n\n" + name + "\n" + city + "\n" + contact + "";

                //Share Image via Email.
                Toast.makeText(getApplicationContext(), "Attachments: " + list.size(), Toast.LENGTH_SHORT).show();
                final Intent ei = new Intent(Intent.ACTION_SEND_MULTIPLE);
                ei.setType("plain/text");
                ei.putExtra(Intent.EXTRA_EMAIL, new String[] {"" + Constants.EMAIL});
                ei.putExtra(Intent.EXTRA_SUBJECT, Constants.SUBJECT);
                ei.putExtra(Intent.EXTRA_TEXT, msgBody.toString());
                ArrayList<Uri> sendDoc = new ArrayList<Uri>();  //Attachments Array
                Iterator<String> iterator = list.iterator();
                String dataVal = "";
                while (iterator.hasNext()) {

                    dataVal = "" + iterator.next();
                    if (dataVal.trim().equals("")) {
                        continue;
                    }
                    File fileIn = new File(dataVal);
                    Uri u = Uri.fromFile(fileIn);
                    sendDoc.add(u);
                }
                ei.putParcelableArrayListExtra(Intent.EXTRA_STREAM, sendDoc);
                startActivityForResult(Intent.createChooser(ei, "Sending multiple attachment"), 12345);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK)
                {
                    Uri selectedImageUri = data.getData();
                    String imagePath = getPath(selectedImageUri);
                    list.add("" + imagePath);
                    Toast.makeText(getApplicationContext(), "Attached files: " + list.size(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * @method getPath
     * @desc helper method to retrieve the path of an image URI
     */
    public String getPath(Uri uri)
    {
        if( uri == null )
        {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
}

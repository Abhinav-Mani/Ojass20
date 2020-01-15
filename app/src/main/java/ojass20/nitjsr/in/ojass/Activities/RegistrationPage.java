package ojass20.nitjsr.in.ojass.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import ojass20.nitjsr.in.ojass.R;

public class RegistrationPage extends AppCompatActivity {

    private TextInputEditText name_reg, email_reg,mobile_reg,
        college_reg;
    private Spinner tshirt_size_spinner, branch_spinner;
    private Button register_button, skip_button;
    private CircleImageView self_image;
    private TextView over_text;

    private boolean fromProfile;

    private String[] tshirt_sizes_list={"S","M","L","XL","XXL"}, branch_list;

    private FirebaseAuth mauth;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ojass20.nitjsr.in.ojass.R.layout.activity_registration_page);

        init();

        branch_list = getResources().getStringArray(R.array.eng_courses);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_list_item_1, branch_list);
        branch_spinner.setAdapter(branchAdapter);

        ArrayAdapter<String> madap = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,tshirt_sizes_list);
        tshirt_size_spinner.setAdapter(madap);

        mauth = FirebaseAuth.getInstance();
        current_user_id = mauth.getCurrentUser().getUid();

        over_text.setText("Welcome "+mauth.getCurrentUser().getDisplayName());
        name_reg.setText(mauth.getCurrentUser().getDisplayName());

        if(fromProfile){
            register_button.setText("Update");
            skip_button.setVisibility(View.GONE);
        }
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationPage.this,  MainActivity.class));
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    register_user();
                }
            }
        });

        Glide.with(this)
                .load(mauth.getCurrentUser().getPhotoUrl().toString())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        self_image.setImageResource(R.drawable.ic_mood_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(self_image);

        email_reg.setText("");
        mobile_reg.setText("");
        college_reg.setText("");
        branch_spinner.setSelection(0);
        tshirt_size_spinner.setSelection(0);
        fetch_existing_data();

    }

    private void fetch_existing_data(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("Users").child(mauth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("email")){
                    String email = dataSnapshot.child("email").getValue(String.class);
                    if((email_reg.getText().toString()).equals(""))
                        email_reg.setText(email);
                }
                if(dataSnapshot.hasChild("mobile")){
                    String mobile = dataSnapshot.child("mobile").getValue(String.class);
                    if((mobile_reg.getText().toString()).equals(""))
                        mobile_reg.setText(mobile);
                }
                if(dataSnapshot.hasChild("college")){
                    String college = dataSnapshot.child("college").getValue(String.class);
                    if((college_reg.getText().toString()).equals(""))
                        college_reg.setText(college);
                }

                if(dataSnapshot.hasChild("branch")){
                    String branch = dataSnapshot.child("branch").getValue(String.class);
                    for(int i = 0;i < branch_list.length;i ++){
                        if(branch.equals(branch_list[i])){
                            branch_spinner.setSelection(i);
                            break;
                        }
                    }
                }
                if(dataSnapshot.hasChild("tshirtSize")){
                    String tshirtSize = dataSnapshot.child("tshirtSize").getValue(String.class);
                    for(int i = 0;i < tshirt_sizes_list.length;i ++){
                        if(tshirtSize.equals(tshirt_sizes_list[i])){
                            tshirt_size_spinner.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void register_user() {
        DatabaseReference dref = FirebaseDatabase
                .getInstance().getReference("Users");

        HashMap<String,Object> data = new HashMap<>();

        data.put("username",name_reg.getText().toString());
        data.put("email",email_reg.getText().toString());
        data.put("mobile",mobile_reg.getText().toString());
        data.put("college",college_reg.getText().toString());
        data.put("branch", branch_list[(int) branch_spinner.getSelectedItemId()]);

        data.put("tshirtSize",tshirt_sizes_list[tshirt_size_spinner.getSelectedItemPosition()]);

        String pic_url = "";
        pic_url = mauth.getCurrentUser().getPhotoUrl().toString();
        data.put("photoUrl",pic_url);
        data.put("uid",current_user_id);

        //String temp_key = dref.push().getKey();
        dref.child(current_user_id).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            String toast_message = (fromProfile) ? "Updated Successfully" : "Registered Successfully";
                            Toast.makeText(RegistrationPage.this, toast_message, Toast.LENGTH_SHORT).show();
                            if(!fromProfile) {
                                Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                        else{
                            String toast_message = (fromProfile) ? "Update Failed" : "Registered Failed";
                            Toast.makeText(RegistrationPage.this, toast_message, Toast.LENGTH_SHORT).show();
                            Log.e("onComplete: "," "+task.getException().toString());
                            startActivity(new Intent(RegistrationPage.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void init() {
        fromProfile = getIntent().getBooleanExtra("fromProfile", false);
        name_reg = findViewById(R.id.Name_Registration_page);
        email_reg = findViewById(R.id.Email_Registration_page);
        mobile_reg = findViewById(R.id.Mobile_Registration_page);
        college_reg = findViewById(R.id.College_Registration_page);
        branch_spinner = findViewById(R.id.branch_registration_page);
        tshirt_size_spinner = findViewById(R.id.TShirt_Size_Registration_page);
        register_button = findViewById(R.id.register_button_Registration_page);
        self_image = findViewById(R.id.register_self_pic);
        over_text = findViewById(R.id.overlap_text);
        skip_button = findViewById(R.id.skip_button_Registration_page);
    }

    public boolean validate(){

        boolean valid=true;
        if(email_reg.getText().toString().trim().isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email_reg.getText().toString().trim()).matches())
        {
            email_reg.setError("Please Enter Valid Email Address");
            valid=false;
        }
        if(mobile_reg.getText().toString().trim().isEmpty()||!Patterns.PHONE.matcher(mobile_reg.getText().toString().trim()).matches() )
        {
            mobile_reg.setError("Please Enter Valid Mobile Number");
            valid=false;
        }

        if(name_reg.getText().toString().trim().isEmpty() )
        {
            name_reg.setError("Please Enter Your Name");
            valid=false;
        }

        if(college_reg.getText().toString().trim().isEmpty() )
        {
            college_reg.setError("Please Enter Your College");
            valid=false;
        }

        return valid;
    }


}

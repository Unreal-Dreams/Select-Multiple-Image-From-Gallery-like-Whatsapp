package com.example.multipleimageselector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageIs;
    private Button previousBtn,nextBtn,selectImageBtn;
    private EditText editCopies;
    private Switch color,poster;

    private ArrayList<Uri> imageUris;
    private ArrayList<Integer> noOfCopies;
    private ArrayList<Boolean> colorPrint;
    private ArrayList<Boolean> posterPrint;


    private static final int PICK_IMAGES_CODE=0;
    int copies = 1;
    String  colorPrintEnabled = "No",posterEnabled="No";

    //position of selected image
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageIs=findViewById(R.id.imageIs);
        previousBtn=findViewById(R.id.previousBtn);
        nextBtn=findViewById(R.id.nextBtn);
        selectImageBtn=findViewById(R.id.pickImagesBtn);
        editCopies=findViewById(R.id.editCopies);
        color=findViewById(R.id.color_photo);
        poster=findViewById(R.id.poster);

       //init list
        imageUris=new ArrayList<>();
        noOfCopies=new ArrayList<>();
        colorPrint=new ArrayList<>();
        posterPrint=new ArrayList<>();

        // Setup image Switcher
        imageIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(getApplicationContext());
                return imageView;
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorPrintEnabled.equals("No")) {
                    colorPrintEnabled = "Yes";
                    colorPrint.set(position,true);
                } else {
                    colorPrintEnabled = "No";
                    colorPrint.set(position,false);
                }

            }
        });

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posterEnabled.equals("No")) {
                    posterEnabled = "Yes";
                    posterPrint.set(position,true);
                } else {
                    posterEnabled = "No";
                    posterPrint.set(position,false);
                }

            }
        });

        editCopies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editCopies.getText().toString().equals("")) {
                    int copy = Integer.valueOf(editCopies.getText().toString().trim());
                    if (copy != 0) {

                        copies = copy;
                        noOfCopies.set(position,copies);
                     //   editCopies.setText(noOfCopies.get(position).toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Enter correct copies", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    copies = 1;
                    Toast.makeText(MainActivity.this, "default Copies is 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>0){
                    position--;
                    imageIs.setImageURI(imageUris.get(position));
                    editCopies.setText(noOfCopies.get(position).toString());
                    color.setChecked(colorPrint.get(position));
                    poster.setChecked(posterPrint.get(position));
                }
                else{
                    Toast.makeText(MainActivity.this, "No Previous Images..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<imageUris.size()-1){
                    position++;
                    imageIs.setImageURI(imageUris.get(position));
                    editCopies.setText(noOfCopies.get(position).toString());
                    color.setChecked(colorPrint.get(position));
                    poster.setChecked(posterPrint.get(position));
                }
                else{
                    Toast.makeText(MainActivity.this, "No More Images... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImagesIntent(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Images"),PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGES_CODE){
            if(resultCode== Activity.RESULT_OK){

                if(data.getClipData()!=null){
                    // Picked Multiple Images
                    int cout=data.getClipData().getItemCount(); // no of images picked;
                    for(int i=0;i<cout;i++){
                        //Get Image Uri at every Position
                        Uri imageUri=data.getClipData().getItemAt(i).getUri();

                        //Populating Arrays
                        imageUris.add(imageUri);// Added to array
                        noOfCopies.add(1);
                        colorPrint.add(false);
                        posterPrint.add(false);

                    }

                    //Setting first image to image switcher
                    imageIs.setImageURI(imageUris.get(0));

                    //Setting No of copies in edit text
                    editCopies.setText(noOfCopies.get(0).toString());

                    //Setting Switches
                    color.setChecked(colorPrint.get(0));
                    poster.setChecked(posterPrint.get(0));



                    //Setting Position
                    position=0;

                }
                else{
                    //picked single image
                    Uri imageUri=data.getData();

                    //Populating Arrays
                    imageUris.add(imageUri);
                    noOfCopies.add(1);
                    colorPrint.add(false);
                    posterPrint.add(false);

                    imageIs.setImageURI(imageUris.get(0));
                    editCopies.setText(noOfCopies.get(0).toString());
                    color.setChecked(colorPrint.get(0));
                    poster.setChecked(posterPrint.get(0));

                    position=0;
                }
            }
        }
    }
}
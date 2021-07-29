package com.majorProject.PDClassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.majorProject.PDClassification.ml.PepperModel;
import com.majorProject.PDClassification.ml.PotatoModel;
import com.majorProject.PDClassification.ml.TomatoModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Classifier extends AppCompatActivity {

    private  TextView textView;
    private ImageView imgView;
    private String model_name;
    private String from_Place;
    private String from_gal = "GAL";
    private String from_cam = "CAM";
    private Bitmap img;
    private Button predict;
    private String load_model;

    private static final int CAMERA_REQUEST = 2;
    private static final int GALLERY_REQUEST = 1;
    
    String labelFile = "";
    ArrayList<String> labelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);

        textView = (TextView) findViewById(R.id.textView2);
        imgView = (ImageView) findViewById(R.id.imageView3);
        predict = (Button) findViewById(R.id.predict);


        String sender = getIntent().getExtras().getString("SENDER_KEY");
        Toast.makeText(this, sender, Toast.LENGTH_SHORT).show();

        load_model = getIntent().getExtras().getString("MODEL");;
        Toast.makeText(this, load_model, Toast.LENGTH_SHORT).show();

        if (sender != null) {

            this.receiveData();
            Toast.makeText(this, "Recieved", Toast.LENGTH_SHORT).show();

            if(sender.equals(from_gal) )
            {

                Toast.makeText(this, "Recieved Gallery", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);


            }else if(sender.equals(from_cam))
            {
                Toast.makeText(this, "Recieved Camera", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);


            }
        }

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //PEPPER

                if(load_model.equals( "pepper")){

                    if (imgView.getDrawable()!=null){

                        img = Bitmap.createScaledBitmap(img,224,224,true);
                        textView.setText("Result \n");

                        try {
                            PepperModel model = PepperModel.newInstance(getApplicationContext());

                            // Creates inputs for reference.
                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                            tensorImage.load(img);
                            ByteBuffer byteBuffer = tensorImage.getBuffer();

                            inputFeature0.loadBuffer(byteBuffer);

                            // Runs model inference and gets result.
                            PepperModel.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                            // Releases model resources if no longer used.
                            model.close();

                            //Loading Label Files
                            try {
                                InputStream inputStream = getAssets().open("label_pepper.txt");
                                int size = inputStream.available();
                                byte[] bufferReader = new byte[size];
                                inputStream.read(bufferReader);
                                inputStream.close();

                                labelFile = new String(bufferReader);
                                labelList = new ArrayList<String>(Arrays.asList(labelFile.split("\n")));



                            }catch (IOException e){
                                e.printStackTrace();
                            }

                            for (int i= 0;i<2;i++){
                                textView.append("\n"+i+" "+ outputFeature0.getFloatArray()[i]);

                            }

                            int max = getMax(outputFeature0.getFloatArray(),2);

                            String ind  = Integer.toString(max);

                            textView.append("\n"+labelList.get(max));
                            textView.append("\n the index is = "+ind);


                        } catch (IOException e) {
                            // TODO Handle the exception
                        }

                    }

                }

                // POTATO

                else if (load_model.equals( "potato")){

                    if (imgView.getDrawable() != null){

                    img = Bitmap.createScaledBitmap(img,224,224,true);
                    textView.setText("Result \n");

                    try {
                        PotatoModel model = PotatoModel.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(img);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        PotatoModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        // Releases model resources if no longer used.
                        model.close();

                        //Loading Label Files
                        try {
                            InputStream inputStream = getAssets().open("label_potato.txt");
                            int size = inputStream.available();
                            byte[] bufferReader = new byte[size];
                            inputStream.read(bufferReader);
                            inputStream.close();

                            labelFile = new String(bufferReader);
                            labelList = new ArrayList<String>(Arrays.asList(labelFile.split("\n")));

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        for (int i= 0;i<3;i++){
                            textView.append("\n"+i+" "+ outputFeature0.getFloatArray()[i]);

                        }

                        int max = getMax(outputFeature0.getFloatArray(),3);

                        textView.append("\n"+labelList.get(max));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                    // TOMATO

                }else if (load_model.equals("tomato")){

                   if (imgView.getDrawable()!=null){
                       img = Bitmap.createScaledBitmap(img,224,224,true);
                       textView.setText("Result ");

                       try {
                           TomatoModel model = TomatoModel.newInstance(getApplicationContext());

                           // Creates inputs for reference.
                           TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                           TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                           tensorImage.load(img);
                           ByteBuffer byteBuffer = tensorImage.getBuffer();

                           inputFeature0.loadBuffer(byteBuffer);

                           // Runs model inference and gets result.
                           TomatoModel.Outputs outputs = model.process(inputFeature0);
                           TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                           // Releases model resources if no longer used.
                           model.close();

                           //Loading Label Files
                           try {
                               InputStream inputStream = getAssets().open("label_tomato.txt");
                               int size = inputStream.available();
                               byte[] bufferReader = new byte[size];
                               inputStream.read(bufferReader);
                               inputStream.close();

                               labelFile = new String(bufferReader);
                               labelList = new ArrayList<String>(Arrays.asList(labelFile.split("\n")));

                           }catch (IOException e){
                               e.printStackTrace();
                           }

                           for (int i= 0;i<10;i++){
                               textView.append("\n"+i+" "+ outputFeature0.getFloatArray()[i]);
                           }

                           int max = getMax(outputFeature0.getFloatArray(),3);

                           textView.append("\n"+labelList.get(max));

                       } catch (IOException e) {
                           // TODO Handle the exception
                       }
                   }
                }
            }
        });

    }

    private void receiveData()
    {
        Intent i =getIntent();
        model_name = i.getStringExtra("SENDER_KEY");
        from_Place = i.getStringExtra("FROM");

        textView.setText(model_name+"\n"+from_Place);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST)
        {
            imgView.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imageBitmap);

            img = imageBitmap;

        }
    }

    //GETTING MAX INDEX
    public int getMax(float[] arr,int limit){

        int index =0;
        float max =0.00f;
        int i = 0;

        for (i = 0;i< limit;i++){

            if (arr[i]>max){
                index = i;
                max = arr[i];
            }
        }
        return index;
    }

    public void showError(){

        Toast.makeText(this,"try",Toast.LENGTH_SHORT).show();
    }
    
}
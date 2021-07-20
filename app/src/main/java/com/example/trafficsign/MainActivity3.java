package com.example.trafficsign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.trafficsign.ml.MyModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
    private Bitmap img;
    private ImageView imgView;
    private Button pic1;
    private TextView result;
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        imgView = (ImageView) findViewById(R.id.imageView2);
        result = (TextView) findViewById(R.id.textView2);
        Button tpic = (Button)findViewById(R.id.pic1);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("label.txt"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;

            while ((mLine = reader.readLine()) != null) {
                //process line
                list.add(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        if(ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity3.this,new String[]
                    {
                            Manifest.permission.CAMERA
                    },100);
        }
        tpic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 100);
        }
    });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 100) {
            img = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(img);
            predict();
        }
    }

    private void predict()
    {

        img = Bitmap.createScaledBitmap(img, 30, 30, true);
        try {
            MyModel model = MyModel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 30, 30, 3}, DataType.FLOAT32);
            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(img);
            ByteBuffer byteBuffer = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            MyModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            int max = getMax(outputFeature0.getFloatArray());
            result.setText(list.get(max));
            // Releases model resources if no longer used.
            model.close();


        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    private int getMax(float[] floatArray) {
        int ind = 0;
        float min = 0.0f;
        for (int i = 0; i < 43; i++) {
            if (floatArray[i] > min) {
                ind = i;
                min = floatArray[i];
            }
        }
        return ind;
    }

}
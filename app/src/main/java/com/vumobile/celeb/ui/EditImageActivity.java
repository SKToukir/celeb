package com.vumobile.celeb.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.isseiaoki.simplecropview.CropImageView;
import com.vumobile.celeb.R;
import com.vumobile.utils.CropImageSerializable;

import java.io.ByteArrayOutputStream;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener {

    CropImageView cropImageView;
    ImageButton imageButton_rotateLeft, imageButton_crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        imageButton_rotateLeft = (ImageButton) findViewById(R.id.imageButton_rotateLeft);
        imageButton_crop = (ImageButton) findViewById(R.id.imageButton_crop);
        imageButton_rotateLeft.setOnClickListener(this);
        imageButton_crop.setOnClickListener(this);


        // get Image from CropImageSerializable class
        CropImageSerializable cropImageSerializable = new CropImageSerializable();
        byte[] byteArray = cropImageSerializable.getByteArray();
        if (byteArray != null) {
            Log.d("sss2", "onResume: " + byteArray.length);
        }
        if (byteArray != null) {
            Bitmap bmp = null;
            if (byteArray != null) {
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            cropImageView.setImageBitmap(bmp);
        }

        cropImageView.setAnimationEnabled(true);
        cropImageView.setAnimationDuration(200);
        cropImageView.setCropMode(CropImageView.CropMode.FREE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton_rotateLeft:
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D); // rotate clockwise by 90 degrees
                break;

            case R.id.imageButton_crop:
                Bitmap bitmap = cropImageView.getCroppedBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray1 = stream.toByteArray();
                CropImageSerializable cropImageSerializable = new CropImageSerializable();
                cropImageSerializable.setByteArray(byteArray1);
                finish();
                break;
        }
    }


}

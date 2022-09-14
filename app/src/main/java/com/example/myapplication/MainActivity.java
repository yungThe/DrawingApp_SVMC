package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private AddPhoto addPhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Uri selectedImage = intent.getData();
        String message = intent.getStringExtra("Uri");
        //Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paint_view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(selectedImage !=null){
            Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_SHORT).show();            paintView.importPhoto(selectedImage);
        }else{
            paintView.init(metrics);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pen :
                paintView.pen();
                Toast.makeText(this, "Pen Active!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.eraser :
                paintView.eraser();
                Toast.makeText(this, "Eraser Active!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.clear :
                paintView.clear();
                Toast.makeText(this, "Canvas Empty!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.photo :
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                 return true;
            case R.id.save:
                paintView.save();
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.back:
                paintView.imageReverse();
                Toast.makeText(this, "Reversed!", Toast.LENGTH_SHORT).show();

                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
        if(requestCode==1 && resultCode == RESULT_OK && data != null){


            //lấy Uri của hình ảnh đã chọn
            Uri url_value = data.getData();

            //thông báo
            Toast.makeText(this, url_value.toString(), Toast.LENGTH_LONG).show();
            //mở bitmap bằng hình ảnh


            try {
                paintView.importPhoto(url_value);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else {
            // DetailActivity không thành công, không có data trả về.
        }
    }
}

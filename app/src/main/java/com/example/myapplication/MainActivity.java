package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private AddPhoto addPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //LayoutInflater layoutInflater = getLayoutInflater();
        //layoutInflater.inflate(R.layout.change_draw_specs);
        SeekBar seekbar = findViewById(R.id.seek_bar);
        seekbar.setMax(200);
        seekbar.setProgress(10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                paintView.BRUSH_SIZE = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        paintView = findViewById(R.id.paint_view);
        paintView.COLOR_PEN = Color.BLACK;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pen:
                paintView.pen();
                Toast.makeText(this, "Pen Active!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.eraser:
                paintView.eraser();
                Toast.makeText(this, "Eraser Active!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.clear:
                paintView.clear();
                Toast.makeText(this, "Canvas Empty!", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.photo:
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
                //Toast.makeText(this, "Reversed!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.color_black:
                Toast.makeText(this, "Black", Toast.LENGTH_SHORT).show();
                paintView.COLOR_PEN = Color.BLACK;
                paintView.pen();
                return true;
            case R.id.color_red:
                Toast.makeText(this, "Red", Toast.LENGTH_SHORT).show();
                paintView.COLOR_PEN = Color.RED;
                paintView.pen();
                return true;
            case R.id.color_yellow:
                Toast.makeText(this, "Yellow", Toast.LENGTH_SHORT).show();
                paintView.COLOR_PEN = Color.YELLOW;
                paintView.pen();
                return true;

            case R.id.color_blue:
                Toast.makeText(this, "Blue", Toast.LENGTH_SHORT).show();
                paintView.COLOR_PEN = Color.BLUE;
                paintView.pen();
                return true;
            case R.id.color_green:
                Toast.makeText(this, "Green", Toast.LENGTH_SHORT).show();
                paintView.COLOR_PEN = Color.GREEN;
                paintView.pen();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {


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


        } else {
            // DetailActivity không thành công, không có data trả về.
        }
    }
}

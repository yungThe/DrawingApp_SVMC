package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private AddPhoto addPhoto;
    public static final int PICK_IMAGE = 102;
    private Bitmap nBitmap;
    private String strFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

            case R.id.photo:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
                return true;

            case R.id.save:
                paintView.save();
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.back:
                paintView.imageReverse();
                if(nBitmap != null) {
                    float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
                    float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;
                    Bitmap.Config config;
                    config = nBitmap.getConfig();
                    paintView.mCanvas.drawBitmap(nBitmap, centreX, centreY, null);
                }
                //Toast.makeText(this, "Reversed!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.redo:
                paintView.redo();
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
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try{
                Uri url_value = data.getData();

                String dir = url_value.toString();
                strFileName = dir.substring(dir.lastIndexOf("/") + 1);

                Bitmap tempBitmap;
                try {
                    InputStream img = getContentResolver().openInputStream(url_value);
                    tempBitmap = BitmapFactory.decodeStream(img);
                    Bitmap.Config config;
                    if(tempBitmap.getConfig() != null){
                        config = tempBitmap.getConfig();
                    }else{
                        config = Bitmap.Config.ARGB_8888;
                    }
                    paintView.clear();
                    nBitmap = Bitmap.createScaledBitmap(tempBitmap,paintView.mCanvas.getWidth(),paintView.mCanvas.getHeight(),false);

                    float h = paintView.mCanvas.getHeight();
                    float w = paintView.mCanvas.getWidth();
                    float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
                    float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;

                    paintView.init2(nBitmap.getWidth(),nBitmap.getHeight());
                    paintView.mCanvas.drawBitmap(nBitmap, centreX,centreY, null);
                    paintView.mBitmap.setConfig(nBitmap.getConfig());
                    paintView.pen();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{

        }
    }
}

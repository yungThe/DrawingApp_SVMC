package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PaintView extends View {

    public int BRUSH_SIZE = 10;
    public static int COLOR_PEN = Color.BLACK;
    public static final int COLOR_ERASER = Color.WHITE;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    public ArrayList<FingerPath> paths = new ArrayList<>();
    public ArrayList<FingerPath> deletedPaths = new ArrayList<>();
    private int pathIndex = -1;
    public Bitmap mBitmap;
    public Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(COLOR_PEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics){
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = COLOR_PEN;
    }

    public void init2(int width , int height ){
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(-1);
        currentColor = COLOR_PEN;
    }
    public void pen(){
        currentColor = COLOR_PEN;
    }

    public void eraser(){
        currentColor = COLOR_ERASER;
    }

    public void clear(){
        paths.clear();
        deletedPaths.clear();
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        pathIndex = -1;
        pen();
        invalidate();
    }

    public void redo(){
        if(!deletedPaths.isEmpty()){
            mPath = new Path();
            mPath = deletedPaths.get(deletedPaths.size()-1).getPath();
            FingerPath fp = new FingerPath(currentColor, BRUSH_SIZE, mPath);
            //mCanvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
            mCanvas.drawPath(fp.getPath(),mPaint);
            paths.add(fp);
            pathIndex++;
            deletedPaths.remove(deletedPaths.size()-1);
            invalidate();
        }
    }


    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public void save(){
        SaveImage(mBitmap);
    }

    public void imageReverse(){
        if(!paths.isEmpty()) {
            deletedPaths.add(paths.get(pathIndex));
            paths.remove(pathIndex);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            pathIndex--;
            pen();
            invalidate();
        }
        else{
            pen();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(Color.TRANSPARENT);
        //deletedPaths.clear();
        for (FingerPath fp : paths){
            mPaint.setColor(fp.getColor());
            mPaint.setStrokeWidth(fp.getStrokeWidth());
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.getPath(), mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y){
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, BRUSH_SIZE, mPath);
        paths.add(fp);
        pathIndex++;
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp(){
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}

package comqq.example.asus_pc.manytouch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends Activity{
    private DrawingView _view;
    private EditText edt;
    private Button btn;
    private Canvas mCanvas;
    private Bitmap mbitBitmap;
    private Paint mPaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#e61783"));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(50);
        _view = (DrawingView) findViewById(R.id.view);
        edt= (EditText) findViewById(R.id.edt);
        btn= (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt.getText().equals("")){
                    mbitBitmap=Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
                    mCanvas=new Canvas(mbitBitmap);
                    mCanvas.drawText(String.valueOf(edt.getText()),10,50,mPaint);
                    CustomBitmap customBitmap = new CustomBitmap(mbitBitmap);
                    _view.addBitmap(customBitmap);
                    Log.e("AAA","ok");
                }
            }
        });

        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Bitmap bmp3 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        CustomBitmap customBitmap1 = new CustomBitmap(bmp1);
        CustomBitmap customBitmap2 = new CustomBitmap(bmp2);
        CustomBitmap customBitmap3 = new CustomBitmap(bmp3);
        customBitmap1.setId(1);
        customBitmap2.setId(2);
        customBitmap3.setId(3);
        if (getSavedMatrix(1) != null){
            Log.e("tag", "matrix 1 is not null");
            customBitmap1.setMatrix(getSavedMatrix(1));

        }
        if (getSavedMatrix(2) != null){
            Log.e("tag", "matrix 2 is not null");
            customBitmap2.setMatrix(getSavedMatrix(2));
        }
        if (getSavedMatrix(3) != null){
            Log.e("tag", "matrix 3 is not null");
            customBitmap3.setMatrix(getSavedMatrix(3));
        }
        _view.addBitmap(customBitmap1);
        _view.addBitmap(customBitmap2);
        _view.addBitmap(customBitmap3);
    }

    //保存matrix
    private void saveMatrix(CustomBitmap customBitmap){
        Log.e("tag", "save matrix" + customBitmap.getId());
        SharedPreferences.Editor editor = getSharedPreferences("matrix", 1).edit();
        Matrix matrix = customBitmap.matrix;
        float[] values = new float[9];
        matrix.getValues(values);
        JSONArray array = new JSONArray();
        for (float value:values){
            try {
                array.put(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(String.valueOf(customBitmap.getId()), array.toString());
        editor.commit();
        Log.e("AAA",array.toString());
        Log.e("tag", "save matrix id:" + customBitmap.getId() + "---------"+values[Matrix.MPERSP_0] + " , " + values[Matrix.MPERSP_1] + " , " +
                values[Matrix.MPERSP_2] + " , " + values[Matrix.MSCALE_X] + " , " +
                values[Matrix.MSCALE_Y] + " , " + values[Matrix.MSKEW_X] + " , " +
                values[Matrix.MSKEW_Y] + " , " +values[Matrix.MTRANS_X] + " , " +
                values[Matrix.MTRANS_Y]);
    }

    //获取matrix
    private Matrix getSavedMatrix(int id){
        SharedPreferences sp = getSharedPreferences("matrix", 1);
        String result = sp.getString(String.valueOf(id), null);
        if (result != null){
            float[] values = new float[9];
            Matrix matrix = new Matrix();
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    values[i] = Float.valueOf(String.valueOf(array.getDouble(i)));
                }
                matrix.setValues(values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("tag", "get matrix id:" + id + "---------"+values[Matrix.MPERSP_0] + " , " + values[Matrix.MPERSP_1] + " , " +
                    values[Matrix.MPERSP_2] + " , " + values[Matrix.MSCALE_X] + " , " +
                    values[Matrix.MSCALE_Y] + " , " + values[Matrix.MSKEW_X] + " , " +
                    values[Matrix.MSKEW_Y] + " , " +values[Matrix.MTRANS_X] + " , " +
                    values[Matrix.MTRANS_Y]);

            return matrix ;
        }
        return null;
    }

    @Override
    public void finish() {
        List<CustomBitmap> list = _view.getViews();
        for (CustomBitmap customBitmap:list){
            saveMatrix(customBitmap);
        }
        super.finish();
    }
}


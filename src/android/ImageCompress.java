package cordova.plugin.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class echoes a string called from JavaScript.
 */
public class ImageCompress extends CordovaPlugin {
   
    String base64;
    int fQuality = 80;
    int fSize = 70;
    int base64Quality = 80;
    int base64Size = 70;
    CallbackContext callbackContext;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("file")) {
            String message = args.getString(0);
          
            JSONObject jObj = new JSONObject(message);
            String data = jObj.getString("data");
            JSONObject jData = new JSONObject(data);
            
            this.fSize = jData.getInt("fSize");
            this.fQuality = jData.getInt("fQuality");
            this.base64Size = jData.getInt("base64Size");
            this.base64Quality = jData.getInt("base64Quality");

            String filePath = jData.getString("imageFile");
            String[] path = filePath.split("file:///");

            if(path.length>1){
                File file = new File(path[1]);
                convertBase64(file);
                saveBitmapToFile(file);
            }else{
                callbackContext.error("File error");
            }
            return true;
        }
        return false;
    }

    public void saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            //Burası resmin büyüklüğünü ayarlıyor
            final int REQUIRED_SIZE=fSize;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            Bitmap bmap = rotateImage(selectedBitmap,90);
            try {
                if (bmap.compress(Bitmap.CompressFormat.JPEG, fQuality , outputStream)) {
                    this.callbackContext.success(base64);
                }
            } catch (Exception e) {
                Log.e("Error  ",""+e.getLocalizedMessage());
                this.callbackContext.error("Error: "+e.getLocalizedMessage());
            }

        } catch (Exception e) {
            Log.d("MainActivity","Exception: "+e.getMessage());
            this.callbackContext.error("Error: "+e.getLocalizedMessage());

        }
    }


    public void convertBase64(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 2;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            //Burası resmin büyüklüğünü ayarlıyor
            final int REQUIRED_SIZE=base64Size;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();



            Bitmap bmap = rotateImage(selectedBitmap,90);
            ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();

            try {
                if (bmap.compress(Bitmap.CompressFormat.JPEG, base64Quality, jpeg_data)) {
                    byte[] code = jpeg_data.toByteArray();
                    byte[] output = Base64.encode(code, Base64.NO_WRAP);
                    base64 = new String(output);

                }
            } catch (Exception e) {
                Log.e("Error  ",""+e.getLocalizedMessage());
                this.callbackContext.error("Error: "+e.getLocalizedMessage());
            }

        } catch (Exception e) {
            Log.d("MainActivity","Exception: "+e.getMessage());
            this.callbackContext.error("Error: "+e.getLocalizedMessage());

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}

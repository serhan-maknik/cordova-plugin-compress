package cordova.plugin.compress;

import com.vritra.common.*;
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


public class Compressor extends VritraPlugin {
   
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("compressImage")) {
            JSONObject options=args.optJSONObject(0);
            this.compressImage(options,callbackContext);
            return true; 
        }
        return false;
    }

    public void compressImage(JSONObject options,CallbackContext callbackContext){
        try{
            String filePath = options.getString("path");
            final String prefix="file://";
            if(filePath.startsWith(prefix)) filePath=filePath.substring(prefix.length());
            Log.d("filePath",filePath);
            File file=new File(filePath);
            if(file.exists()){
                final int quality=options.optInt("quality",50);
                final String base64=convertBase64(file,quality);
                final boolean overwrite=options.optBoolean("overwrite",false);
                final File compressedFile=saveBitmapToFile(file,base64,overwrite);
                final JSONObject result=new JSONObject();
                result.put("base64","data:image/jpeg;base64,"+base64);
                result.put("path",compressedFile.getAbsolutePath());
                callbackContext.success(result);
            }
            else throw new Exception("invalid path");
        }
        catch(Exception exception){
            callbackContext.error(new VritraError(exception.getLocalizedMessage()));
        }
    }

    static String convertBase64(File file,int quality) throws Exception {
        // BitmapFactory options to downsize the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 2;
        // factor of downsizing the image
        FileInputStream inputStream = new FileInputStream(file);
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream,null,options);
        inputStream.close();
        // Find the correct scale value. It should be the power of 2.
        int scale=1,base64Size=70;
        while(options.outWidth/scale/2>=base64Size&&options.outHeight/scale/2>=base64Size){
            scale*=2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        inputStream = new FileInputStream(file);
        Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
        inputStream.close();
        Bitmap bmap = rotateImage(selectedBitmap,0);
        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        if(bmap.compress(Bitmap.CompressFormat.JPEG,quality,jpeg_data)) {
            byte[] code = jpeg_data.toByteArray();
            byte[] output = Base64.encode(code,Base64.NO_WRAP);
            return new String(output);
        }
        else return null;
    }

    static File saveBitmapToFile(File file,String base64,boolean overwrite) throws Exception {
        // BitmapFactory options to downsize the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 6;
        // factor of downsizing the image
        FileInputStream inputStream = new FileInputStream(file);
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream,null,options);
        inputStream.close();
        // Find the correct scale value. It should be the power of 2.
        int scale=1,fSize=70;
        while((options.outWidth/scale/2>=fSize)&&options.outHeight/scale/2>=fSize){
            scale*=2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        inputStream = new FileInputStream(file);
        Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,null,o2);
        inputStream.close();
        
        Bitmap bmap=rotateImage(selectedBitmap,0);
        File compressedFile=null;
        if(overwrite) compressedFile=file;
        else{
            File cacheDir=context.getCacheDir();
            compressedFile=new File(cacheDir,file.getName());
        }
        FileOutputStream fileOutputStream=new FileOutputStream(compressedFile);
        final int fQuality=80;
        final boolean didCompress=bmap.compress(Bitmap.CompressFormat.JPEG,fQuality,fileOutputStream);
        fileOutputStream.close();
        if(didCompress) return compressedFile;
        else throw new Exception("compression failed");
    }

    static Bitmap rotateImage(Bitmap source,float angle){
        Matrix matrix=new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }
}

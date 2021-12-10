package amazing.hd.amazinghdwallpaper.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import amazing.hd.amazinghdwallpaper.R;

public class Utils {
	private final String TAG = Utils.class.getSimpleName();
	private final Context _context;

	public Utils(Context context) {
		this._context = context;
	}

	public void saveImageToSDCard(Bitmap bitmap) {
		if (Environment.getExternalStorageState()!=null) {
			File myDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"Amazing HD Wallpaper");

			if (!myDir.exists())
				myDir.mkdirs();

			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);
			String fname = "Wallpaper-" + n + ".jpg";
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
				Toast.makeText(
						_context,
						_context.getString(R.string.toast_saved).replace("#",
								"\"" + "Amazing HD Wallpaper" + "\""),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Wallpaper saved to: " + file.getAbsolutePath());

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(_context,
						_context.getString(R.string.toast_saved_failed),
						Toast.LENGTH_SHORT).show();
			}
		}else{
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);
			String fname = "Wallpaper-" + n + ".jpg";
			writeFileOnInternalStorage(_context, fname, bitmap);
		}
	}

	public void writeFileOnInternalStorage(Context mcoContext,String sFileName, Bitmap bitmap){
		File file = new File(mcoContext.getFilesDir(),"Amazing HD Wallpaper");
		if(!file.exists()){
			file.mkdir();
		}

		try{
			File gpxfile = new File(file, sFileName);
			FileOutputStream out = new FileOutputStream(gpxfile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(
					_context,
					_context.getString(R.string.toast_saved).replace("#",
							"\"" + "Amazing HD Wallpaper" + "\""),
					Toast.LENGTH_SHORT).show();
			Log.d(TAG, "Wallpaper saved to: " + file.getAbsolutePath());

		}catch (Exception e){
			e.printStackTrace();
			Toast.makeText(_context,
					_context.getString(R.string.toast_saved_failed),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void setAsWallpaper(Bitmap bitmap) {
		try {
			WallpaperManager wm = WallpaperManager.getInstance(_context);

			wm.setBitmap(bitmap);
			Toast.makeText(_context,
					_context.getString(R.string.toast_wallpaper_set),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(_context,
					_context.getString(R.string.toast_wallpaper_set_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
}
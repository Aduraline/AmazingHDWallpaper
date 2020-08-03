package amazing.hd.amazinghdwallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import amazing.hd.amazinghdwallpaper.app.AppConst;
import amazing.hd.amazinghdwallpaper.dialog.CustomDialogClass;
import amazing.hd.amazinghdwallpaper.picasa.model.Wallpaper;
import amazing.hd.amazinghdwallpaper.util.Utils;


public class FullScreenViewActivity extends Activity implements OnClickListener {
	private static final String TAG = FullScreenViewActivity.class
			.getSimpleName();
	public static final String TAG_SEL_IMAGE = "data";
	private Wallpaper selectedPhoto;
	private ImageView fullImageView;
	private LinearLayout llSetWallpaper, llDownloadWallpaper, llInfoWallpaper;
	private Utils utils;
	private ProgressBar pbLoader;
	View mDecorView;
	private InterstitialAd mInterstitialAd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_fullscreen_image);

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

		// Create an ad request.
		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

		// Set an AdListener.
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
//				Toast.makeText(FullScreenViewActivity.this,
//						"The interstitial is loaded", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdClosed() {
				// Proceed to the next level.
				if (((BitmapDrawable) fullImageView.getDrawable()) != null){
					Bitmap bitmap = ((BitmapDrawable) fullImageView.getDrawable())
							.getBitmap();
					utils.saveImageToSDCard(bitmap);
				}else{
					Toast.makeText(FullScreenViewActivity.this, "Wallpaper download failed", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Start loading the ad now so that it is ready by the time the user is ready to go to
		// the next level.

		if (AppConst.isAdsDisabled == false)
			mInterstitialAd.loadAd(adRequestBuilder.build());

		mDecorView = getWindow().getDecorView();

		fullImageView = (ImageView) findViewById(R.id.imgFullscreen);
		llSetWallpaper = (LinearLayout) findViewById(R.id.llSetWallpaper);
		llInfoWallpaper = (LinearLayout) findViewById(R.id.llInfoWallpaper);
		llDownloadWallpaper = (LinearLayout) findViewById(R.id.llDownloadWallpaper);
		pbLoader = (ProgressBar) findViewById(R.id.pbLoader);

		// hide the action bar in fullscreen mode
		if (getActionBar()!=null)
		{
			if(getActionBar().isShowing())
				getActionBar().hide();
		}

		utils = new Utils(getApplicationContext());

		// layout click listeners
		llSetWallpaper.setOnClickListener(this);
		llDownloadWallpaper.setOnClickListener(this);
		llInfoWallpaper.setOnClickListener(this);

		// setting layout buttons alpha/opacity
		llSetWallpaper.getBackground().setAlpha(70);
		llDownloadWallpaper.getBackground().setAlpha(70);
		llInfoWallpaper.getBackground().setAlpha(70);

		Intent i = getIntent();
		if (i!=null) {
			selectedPhoto = (Wallpaper) i.getSerializableExtra(TAG_SEL_IMAGE);
		}

		if (selectedPhoto != null) {

			fetchFullResolutionImage();

		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.msg_unknown_error), Toast.LENGTH_SHORT)
					.show();

		}
	}

	/**
	 * Fetching image fullresolution json
	 * */

	private void fetchFullResolutionImage() {
		String url = selectedPhoto.getUrl();

		// show loader before making request
		pbLoader.setVisibility(View.VISIBLE);
		llSetWallpaper.setVisibility(View.GONE);
		llDownloadWallpaper.setVisibility(View.GONE);
		llInfoWallpaper.setVisibility(View.GONE);

		if (url!=null) {
			llSetWallpaper.setVisibility(View.VISIBLE);
			llDownloadWallpaper.setVisibility(View.VISIBLE);
			llInfoWallpaper.setVisibility(View.VISIBLE);


			Glide.with(FullScreenViewActivity.this)
					.load(url)
					.asBitmap()
					.listener(new RequestListener<String, Bitmap>() {
						@Override
						public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
							return false;
						}

						@Override
						public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
							pbLoader.setVisibility(View.GONE);
							return false;
						}
					})
					.into(fullImageView);


		}else{
			pbLoader.setVisibility(View.VISIBLE);
			llSetWallpaper.setVisibility(View.GONE);
			llDownloadWallpaper.setVisibility(View.GONE);
			llInfoWallpaper.setVisibility(View.GONE);
		}

	}

	/**
	 * View click listener
	 * */

	@Override
	public void onClick(View v) {
		if (((BitmapDrawable) fullImageView.getDrawable()) != null){
			Bitmap bitmap = ((BitmapDrawable) fullImageView.getDrawable())
					.getBitmap();
			switch (v.getId()) {
				// button Download Wallpaper tapped
				case R.id.llDownloadWallpaper:
					if (mInterstitialAd.isLoaded()) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (ContextCompat.checkSelfPermission(getApplicationContext(),
									Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
									&& ContextCompat.checkSelfPermission(getApplicationContext(),
									Manifest.permission.READ_EXTERNAL_STORAGE)
									!= PackageManager.PERMISSION_GRANTED) {
								requestPermissions(new String[]{
										Manifest.permission.READ_EXTERNAL_STORAGE,
										Manifest.permission.WRITE_EXTERNAL_STORAGE


								}, 0);

							} else {
								mInterstitialAd.show();
							}
						}else{
							mInterstitialAd.show();
						}
					} else {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (ContextCompat.checkSelfPermission(getApplicationContext(),
									Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
									&& ContextCompat.checkSelfPermission(getApplicationContext(),
									Manifest.permission.READ_EXTERNAL_STORAGE)
									!= PackageManager.PERMISSION_GRANTED) {
								requestPermissions(new String[]{
										Manifest.permission.READ_EXTERNAL_STORAGE,
										Manifest.permission.WRITE_EXTERNAL_STORAGE


								}, 0);

							} else {
								utils.saveImageToSDCard(bitmap);
							}
						}else {
							utils.saveImageToSDCard(bitmap);
						}
					}
					break;
				// button Set As Wallpaper tapped
				case R.id.llSetWallpaper:
					utils.setAsWallpaper(bitmap);
					break;
				case R.id.	llInfoWallpaper:
					CustomDialogClass cdd=new CustomDialogClass(this);
					cdd.show();
				default:
					break;
			}
		}else{
//			Toast.makeText(FullScreenViewActivity.this, "Wallpaper download failed", Toast.LENGTH_SHORT).show();
		}

	}

}
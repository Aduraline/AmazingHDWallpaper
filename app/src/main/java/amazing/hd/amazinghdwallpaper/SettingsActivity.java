package amazing.hd.amazinghdwallpaper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import amazing.hd.amazinghdwallpaper.app.AppConst;

public class SettingsActivity extends AppCompatActivity {

	ImageView back;

	RelativeLayout removeads, push, feedback, share, storage, rate, version;

	Switch activate;

	private RewardedVideoAd mRewardedVideoAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		back = (ImageView) findViewById(R.id.back);

		removeads = (RelativeLayout) findViewById(R.id.removeads);
		push = (RelativeLayout) findViewById(R.id.push);
		feedback = (RelativeLayout) findViewById(R.id.feedback);
		share = (RelativeLayout) findViewById(R.id.share);
		storage = (RelativeLayout) findViewById(R.id.storage);
		rate = (RelativeLayout) findViewById(R.id.rate);
		version = (RelativeLayout) findViewById(R.id.about);
		activate = (Switch) findViewById(R.id.activate_push);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingsActivity.this, MainActivity.class)); finish();
			}
		});

		removeads.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//code to remove ads
				if (mRewardedVideoAd.isLoaded()) {
					mRewardedVideoAd.show();
				}
			}
		});

		storage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//display storage
			}
		});
		push.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//active push
			}
		});
		activate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activate.isActivated()){
					Toast.makeText(SettingsActivity.this, "Push notification is activated", Toast.LENGTH_LONG).show();
				}else{

				}
			}
		});

		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//codes to share
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey friend! Check out Amazing HD Wallpaper on the google play store " + "http://play.google.com/store/apps/details?id=" + getPackageName());
				startActivity(Intent.createChooser(shareIntent, "Share recommendation using"));
			}
		});

		feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//send email code
				String[] to = {"yoursample@email.com"};
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback concerning Amazing HD Wallpaper");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
				try{
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					finish();
				}catch (ActivityNotFoundException ex){
					Toast.makeText(SettingsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		rate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//rating on google play store
				try {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("market://details?id=" + getPackageName())));
				}
				catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
				}
			}
		});
		version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//display app version
			}
		});

		// Initialize the Mobile Ads SDK.
		MobileAds.initialize(this,getString(R.string.api_id));

		mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

		mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewarded(RewardItem rewardItem) {
				AppConst.isAdsDisabled = true;
				AppConst.counting(rewardItem.getAmount()*60000);
				Toast.makeText(getBaseContext(), "You got reward of : "+rewardItem.getAmount()+ "minutes Ad free experience", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoAdLoaded() {
//				Toast.makeText(getBaseContext(), "Ad loaded.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoAdOpened() {
//				Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoStarted() {
//				Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoAdClosed() {
//				Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();
				mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());

			}

			@Override
			public void onRewardedVideoAdLeftApplication() {
//				Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {
//				Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRewardedVideoCompleted() {

			}
		});

		//Load Rewarded Ad
		mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());

	}


	@Override
	public void onPause() {
		super.onPause();
		mRewardedVideoAd.pause(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRewardedVideoAd.destroy(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mRewardedVideoAd.resume(this);
	}
}

package amazing.hd.amazinghdwallpaper;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import amazing.hd.amazinghdwallpaper.app.AppConst;

public class SettingsActivity extends AppCompatActivity {

	ImageView back;

	RelativeLayout push, feedback, share, storage, rate, version;

	@SuppressLint("UseSwitchCompatOrMaterialCode")
	Switch activate;

	@SuppressLint("IntentReset")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		back = (ImageView) findViewById(R.id.back);

		push = (RelativeLayout) findViewById(R.id.push);
		feedback = (RelativeLayout) findViewById(R.id.feedback);
		share = (RelativeLayout) findViewById(R.id.share);
		storage = (RelativeLayout) findViewById(R.id.storage);
		rate = (RelativeLayout) findViewById(R.id.rate);
		version = (RelativeLayout) findViewById(R.id.about);
		activate = (Switch) findViewById(R.id.activate_push);

		back.setOnClickListener(v -> {
			startActivity(new Intent(SettingsActivity.this, MainActivity.class));
			finish();
		});

		storage.setOnClickListener(v -> {
			//display storage
		});
		push.setOnClickListener(v -> {
			//active push
		});
		activate.setOnClickListener(v -> {
			if (activate.isActivated()) {
				Toast.makeText(SettingsActivity.this, "Push notification is activated", Toast.LENGTH_LONG).show();
			}
		});

		share.setOnClickListener(v -> {
			//codes to share
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey friend! Check out Amazing HD Wallpaper on the google play store " + "http://play.google.com/store/apps/details?id=" + getPackageName());
			startActivity(Intent.createChooser(shareIntent, "Share recommendation using"));
		});

		feedback.setOnClickListener(v -> {
			//send email code
			String[] to = {AppConst.EMAIL};
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setData(Uri.parse("mailto:"));
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback concerning Amazing HD Wallpaper");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
			try {
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				finish();
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(SettingsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
			}
		});
		rate.setOnClickListener(v -> {
			//rating on google play store
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + getPackageName())));
			} catch (ActivityNotFoundException anfe) {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
			}
		});
		version.setOnClickListener(v -> {
			//display app version
		});

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}

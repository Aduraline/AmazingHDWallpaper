package amazing.hd.amazinghdwallpaper.app;


import android.os.CountDownTimer;

public class AppConst {

	// Pexels API URL - For Loading & Getting Wallpaper Images
	public static final String URL = "https://api.pexels.com/v1/search?query=value";

	// Authorization API KEY
	public static final String API_KEY = "563492ad6f91700001000001441da41472774f3c97b415a456a412e2";

	public static Boolean isAdsDisabled = false;

	public static void counting(int time){
		new CountDownTimer(time, 1000) {
			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
				isAdsDisabled = true;
			}
		}.start();
	}

}
package amazing.hd.amazinghdwallpaper.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amazing.hd.amazinghdwallpaper.GridViewAdapter;
import amazing.hd.amazinghdwallpaper.R;
import amazing.hd.amazinghdwallpaper.app.AppConst;
import amazing.hd.amazinghdwallpaper.picasa.model.Wallpaper;
import amazing.hd.amazinghdwallpaper.util.Utils;


public class GridFragment extends Fragment {
	private static final String TAG = GridFragment.class.getSimpleName();
	private Utils utils;
	private RecyclerView gridView;
	private int columnWidth;
	private static final String bundleAlbumId = "albumId";
	private String selectedAlbumId;
	private List<Wallpaper> photosList;
	private ProgressBar pbLoader;
	private RequestQueue mRequestQueue;
	GridViewAdapter gridViewAdapter;

	NativeExpressAdView mAdView;
	private static String LOG_TAG = "AMAZINGHDWALLPAPER";
	VideoController mVideoController;
	LinearLayout nonetwork;

	public static final int PER_PAGE = 2;

	public GridFragment() {
	}

	public static GridFragment newInstance(String albumId) {
		GridFragment f = new GridFragment();
		Bundle args = new Bundle();
		args.putString(bundleAlbumId, albumId);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		photosList = new ArrayList<>();

		if (getArguments().getString(bundleAlbumId) != null) {
			selectedAlbumId = getArguments().getString(bundleAlbumId);
		} else {
			selectedAlbumId = null;
		}

		View rootView = inflater.inflate(R.layout.fragment_grid, container,
				false);

		gridView = (RecyclerView) rootView.findViewById(R.id.grid_view);

		nonetwork = (LinearLayout) rootView.findViewById(R.id.nonetwork);

		mAdView = (NativeExpressAdView)rootView.findViewById(R.id.adView);

		try {
			mAdView.setVideoOptions(new VideoOptions.Builder()
					.setStartMuted(true)
					.build());

			mVideoController = mAdView.getVideoController();
			mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
				@Override
				public void onVideoEnd() {
					Log.d(LOG_TAG, "Video playback is finished.");
					super.onVideoEnd();
				}
			});

			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (mVideoController.hasVideoContent()) {
						Log.d(LOG_TAG, "Received an ad that contains a video asset.");
					} else {
						Log.d(LOG_TAG, "Received an ad that does not contain a video asset.");
					}
				}
			});

			if (AppConst.isAdsDisabled == false)
				mAdView.loadAd(new AdRequest.Builder().build());
			else
				mAdView.setEnabled(false);

		}catch(Exception e){

		}

		int mNoOfColumns = calculateNoOfColumns(getContext(), 200);
		GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
		gridView.setLayoutManager(mGridLayoutManager);
		gridView.setVisibility(View.GONE);

		pbLoader = (ProgressBar) rootView.findViewById(R.id.pbLoader);
		pbLoader.setVisibility(View.VISIBLE);
		nonetwork.setVisibility(View.GONE);

		utils = new Utils(getActivity());

		mRequestQueue = Volley.newRequestQueue(getActivity());
		parseJSON();

		return rootView;
	}

	boolean internet_connection(){
		if (getActivity()!=null) {
			ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
			return isConnected;
		}else {
			return false;
		}
	}

	private void parseJSON() {
		String url = AppConst.URL;

		if (selectedAlbumId!=null) {

			if(selectedAlbumId.equals("people"))
				url = "https://api.pexels.com/v1/curated?per_page=15&page=1";
			else
				url = url.replace("value", selectedAlbumId);

		}else {
			//url = "https://api.pexels.com/v1/search?query=people";
			url = url.replace("value", "people");
		}

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (internet_connection()) {
							try {
								JSONArray jsonArray = response.getJSONArray("photos");

								photosList.clear();

								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject photos = jsonArray.getJSONObject(i);

									String creatorName = photos.getString("photographer");
									String imageUrl = photos.getJSONObject("src").getString("portrait");
									boolean likeCount = photos.getBoolean("liked");
									int width = photos.getInt("width");
									int height = photos.getInt("height");

									photosList.add(new Wallpaper(creatorName, imageUrl, width, height));
								}

								gridView.setVisibility(View.VISIBLE);

								pbLoader.setVisibility(View.GONE);

								gridViewAdapter = new GridViewAdapter(getActivity(), photosList);
								gridView.setAdapter(gridViewAdapter);
								gridViewAdapter.notifyDataSetChanged();

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else{

							pbLoader.setVisibility(View.GONE);
							nonetwork.setVisibility(View.VISIBLE);

						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();

				pbLoader.setVisibility(View.GONE);
				nonetwork.setVisibility(View.VISIBLE);

			}
		}){
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Authorization", AppConst.API_KEY);
				return params;
			}
		};

		mRequestQueue.add(request);
	}

	public int calculateNoOfColumns(Context context, float columnWidthDp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
		int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
		return noOfColumns;
	}

}

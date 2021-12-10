package amazing.hd.amazinghdwallpaper.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amazing.hd.amazinghdwallpaper.GridViewAdapter;
import amazing.hd.amazinghdwallpaper.R;
import amazing.hd.amazinghdwallpaper.app.AppConst;
import amazing.hd.amazinghdwallpaper.picasa.model.Wallpaper;

public class GridFragment extends Fragment {
	private RecyclerView gridView;
	private static final String bundleAlbumId = "albumId";
	private String selectedAlbumId;
	private List<Wallpaper> photosList;
	private ProgressBar pbLoader;
	private RequestQueue mRequestQueue;
	GridViewAdapter gridViewAdapter;

	private static final String LOG_TAG = "LogMessage";
	LinearLayout nonetwork;

	public GridFragment() {
	}

	public static GridFragment newInstance(String albumId) {
		GridFragment f = new GridFragment();
		Bundle args = new Bundle();
		args.putString(bundleAlbumId, albumId);
		f.setArguments(args);
		return f;
	}

	@SuppressLint("MissingPermission")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		photosList = new ArrayList<>();

		if (getArguments() != null) {
			if (getArguments().getString(bundleAlbumId) != null) {
				selectedAlbumId = getArguments().getString(bundleAlbumId);
			} else {
				selectedAlbumId = null;
			}
		}

		View rootView = inflater.inflate(R.layout.fragment_grid, container,
				false);

		gridView = (RecyclerView) rootView.findViewById(R.id.grid_view);

		nonetwork = (LinearLayout) rootView.findViewById(R.id.nonetwork);

		int mNoOfColumns = calculateNoOfColumns(requireContext(), 200);
		GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
		gridView.setLayoutManager(mGridLayoutManager);
		gridView.setVisibility(View.GONE);

		pbLoader = (ProgressBar) rootView.findViewById(R.id.pbLoader);
		pbLoader.setVisibility(View.VISIBLE);
		nonetwork.setVisibility(View.GONE);

		mRequestQueue = Volley.newRequestQueue(requireActivity());
		parseJSON();

		return rootView;
	}

	boolean internet_connection(){
		if (getActivity()!=null) {
			ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
			url = url.replace("value", "people");
		}

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
				response -> {
					if (internet_connection()) {
						try {
							JSONArray jsonArray = response.getJSONArray("photos");

							photosList.clear();

							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject photos = jsonArray.getJSONObject(i);

								String creatorName = photos.getString("photographer");
								String imageUrl = photos.getJSONObject("src").getString("portrait");
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
				}, error -> {
					error.printStackTrace();

					pbLoader.setVisibility(View.GONE);
					nonetwork.setVisibility(View.VISIBLE);

				}){
			@Override
			public Map<String, String> getHeaders() {
				Map<String, String> params = new HashMap<>();
				params.put("Authorization", AppConst.API_KEY);
				return params;
			}
		};

		mRequestQueue.add(request);
	}

	public int calculateNoOfColumns(Context context, float columnWidthDp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
		return (int) (screenWidthDp / columnWidthDp + 0.5);
	}
}

package amazing.hd.amazinghdwallpaper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import amazing.hd.amazinghdwallpaper.picasa.model.Wallpaper;



public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.WallpaperViewHolder> {

	private Activity _activity;
	private LayoutInflater inflater;
	private List<Wallpaper> wallpapersList = new ArrayList<Wallpaper>();

	public GridViewAdapter(Activity activity, List<Wallpaper> wallpapersList) {
		this._activity = activity;
		this.wallpapersList = wallpapersList;
	}

	@Override
	public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(_activity).inflate(R.layout.grid_item_photo, parent, false);
		return new WallpaperViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final WallpaperViewHolder holder, final int position) {
		final Wallpaper p = wallpapersList.get(position);

		holder.thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);

		holder.thumbNail.setVisibility(View.VISIBLE);

		Glide.with(_activity)
				.load(p.getUrl())
				.thumbnail(0.5f)
				.crossFade()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						holder.imageloader.setVisibility(View.GONE);
						return false;
					}
				})
				.into(holder.thumbNail);

		holder.thumbNail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(_activity,
						FullScreenViewActivity.class);

				// Passing selected image to fullscreen activity
				Wallpaper photo = wallpapersList.get(position);

				i.putExtra(FullScreenViewActivity.TAG_SEL_IMAGE, photo);
				_activity.startActivity(i);
			}
		});

	}

	@Override
	public int getItemCount() {
		return wallpapersList.size();
	}

	public class WallpaperViewHolder extends RecyclerView.ViewHolder {

		public ImageView thumbNail;
		final ProgressBar imageloader;

		public WallpaperViewHolder(View itemView) {
			super(itemView);

			// Grid thumbnail image view
			imageloader = itemView.findViewById(R.id.imgLoader);
			thumbNail = itemView
					.findViewById(R.id.thumbnail);

		}
	}


}
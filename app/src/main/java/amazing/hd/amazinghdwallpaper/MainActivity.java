package amazing.hd.amazinghdwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import amazing.hd.amazinghdwallpaper.fragment.GridFragment;

public class MainActivity extends AppCompatActivity {
	DrawerLayout mDrawerLayout;
	NavigationView mNavigationView;
	FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mNavigationView = (NavigationView) findViewById(R.id.navView) ;

		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("people")).commit();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mNavigationView.setNavigationItemSelectedListener(menuItem -> {
			mDrawerLayout.closeDrawers();

			if (menuItem.getItemId() == R.id.recent) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("people")).commit();
				toolbar.setTitle("Recent");
			}
			if (menuItem.getItemId() == R.id.musical) {
				FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
				xfragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("musical")).commit();
				toolbar.setTitle("Musical");
			}
			if (menuItem.getItemId() == R.id.nature) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("nature")).commit();
				toolbar.setTitle("Nature");
			}
			if (menuItem.getItemId() == R.id.technology) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("technology")).commit();
				toolbar.setTitle("Technology");
			}
			if (menuItem.getItemId() == R.id.travel) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("travel")).commit();
				toolbar.setTitle("Travel");
			}

			if (menuItem.getItemId() == R.id.random) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("fashion")).commit();
				toolbar.setTitle("Random");
			}
			if (menuItem.getItemId() == R.id.abstrat) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("3D")).commit();
				toolbar.setTitle("3D Abstract");
			}
			if (menuItem.getItemId() == R.id.aerial) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("aerial")).commit();
				toolbar.setTitle("Aerial");
			}
			if (menuItem.getItemId() == R.id.animals) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("animals")).commit();
				toolbar.setTitle("Animals");
			}
			if (menuItem.getItemId() == R.id.architecture) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("architecture")).commit();
				toolbar.setTitle("Architecture");
			}
			if (menuItem.getItemId() == R.id.automotive) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("automotive")).commit();
				toolbar.setTitle("Automotive");
			}
			if (menuItem.getItemId() == R.id.beach) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("beach")).commit();
				toolbar.setTitle("Beach");
			}
			if (menuItem.getItemId() == R.id.celebrations) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("celebration")).commit();
				toolbar.setTitle("Celebrations");
			}
			if (menuItem.getItemId() == R.id.creative) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("creative")).commit();
				toolbar.setTitle("Creative");
			}
			if (menuItem.getItemId() == R.id.cute) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("cute")).commit();
				toolbar.setTitle("Cute");
			}
			if (menuItem.getItemId() == R.id.black) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("black")).commit();
				toolbar.setTitle("Black");
			}
			if (menuItem.getItemId() == R.id.fantasy) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("fantasy")).commit();
				toolbar.setTitle("Fantasy");
			}
			if (menuItem.getItemId() == R.id.flowers) {
				FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.containerView, GridFragment.newInstance("flowers")).commit();
				toolbar.setTitle("Flowers");
			}


			return false;
		});

		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
				R.string.app_name);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerToggle.syncState();

		mDrawerToggle.setDrawerIndicatorEnabled(false);

		mDrawerToggle.setToolbarNavigationClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));

		mDrawerToggle.setHomeAsUpIndicator(R.drawable.menuuuu2);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar actions click
		if (item.getItemId() == R.id.action_settings) {// Selected settings menu item
			// launch Settings activity
			Intent intent = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

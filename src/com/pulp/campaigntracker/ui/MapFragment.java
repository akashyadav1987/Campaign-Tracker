package com.pulp.campaigntracker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pulp.campaigntracker.R;

public class MapFragment extends Fragment{


	private GoogleMap map;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getGoogleMap();
		return inflater.inflate(R.layout.mapframe, container, false);
	}
	private GoogleMap getGoogleMap() {
		if (map == null && getActivity() != null && getActivity().getSupportFragmentManager()!= null) {
			SupportMapFragment smf = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
			if (smf != null) {
				map = smf.getMap();
			}
		}
		return map;
	}
	private void showOnMap(double latitude,double longitude) {


		//Dummy Value
		latitude = 28.5420535;
		longitude = 77.258376;
		//
		if(latitude!=0 && longitude!=0)
		{
			map.clear();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
					longitude), 15));
			map.addMarker(new MarkerOptions().icon(
					BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
					// .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(latitude, longitude)));
			map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
		}
		else
		{
			// Error message.
		}

	}

}

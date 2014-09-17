package com.pulp.campaigntracker.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pulp.campaigntracker.R;

public class SupervisorIconFragment extends Fragment {
	
	private FragmentActivity mActivity;
	private Context mContext;
	private Typeface iconFonts;
	private TextView supervisorIcon;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		mContext = getActivity().getBaseContext();

	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.login_supervisor_fragment, container, false);
        iconFonts = Typeface.createFromAsset(mContext.getAssets(),
				"icomoon.ttf");

        supervisorIcon = (TextView) rootView.findViewById(R.id.supervisorIcon);
        supervisorIcon.setTypeface(iconFonts);

        return rootView;
    }
}
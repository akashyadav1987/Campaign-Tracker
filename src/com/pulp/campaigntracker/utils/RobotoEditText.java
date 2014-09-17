package com.pulp.campaigntracker.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class RobotoEditText extends EditText {

	Context context;

	public RobotoEditText(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public RobotoEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public RobotoEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setTypeface(Typeface tf, int style) {
		if (style == Typeface.NORMAL) {
			super.setTypeface(Typeface.createFromAsset(
					getContext().getAssets(), "fonts/RobotoNormal.ttf"));
		} else if (style == Typeface.ITALIC) {
			super.setTypeface(Typeface.createFromAsset(
					getContext().getAssets(), "fonts/RobotoItalic.ttf"));
		} else if (style == Typeface.BOLD) {
			super.setTypeface(Typeface.createFromAsset(
					getContext().getAssets(), "fonts/RobotoBold.ttf"));
		}
	}
}

package com.pulp.campaigntracker.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypeFaceUtil {

	private Context mContext;
	private Typeface titilliumTextLight;
	private Typeface titilliumTextDark;
	private Typeface titilliumTextMedium;
	private Typeface titilliumTextMedium_1;
	private Typeface icomoon;
	private Typeface titilliumTextMedium_2;

	private static TypeFaceUtil instance;

	// enum
	public static enum EnumCustomTypeFace {
		TITILLIUM_LIGHT, TITILLIUM_DARK, TITILLIUM_MEDIUM, TITILLIUM_MEDIUM_1,TITILLIUM_MEDIUM_2,ICOMOON
	}

	/**
	 * import all of the typefaces
	 * 
	 * @param context
	 */
	public TypeFaceUtil(Context context) {
		mContext = context;
		titilliumTextLight = Typeface.createFromAsset(mContext.getAssets(),
				"TitilliumText22L001_1.otf");
		titilliumTextDark = Typeface.createFromAsset(mContext.getAssets(),
				"TitilliumText22L002_0.otf");
		titilliumTextMedium = Typeface.createFromAsset(mContext.getAssets(),
				"TitilliumText22L003_0.otf");
		titilliumTextMedium_1 = Typeface.createFromAsset(mContext.getAssets(),
				"TitilliumText22L004_0.otf");
		titilliumTextMedium_2 = Typeface.createFromAsset(mContext.getAssets(),
				"TitilliumText22L005_0.otf");
		icomoon = Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf");
	}

	/**
	 * static method to get instance
	 * 
	 * @param context
	 * @return
	 */
	public static TypeFaceUtil getInstance(Context context) {
		if (instance == null)
			instance = new TypeFaceUtil(context);

		return instance;
	}

	/**
	 * sets the typeface for a button
	 * 
	 * @param customTypeFace
	 *            : Enum for identifying typeface
	 * @param button
	 *            : typeface to be applied
	 */
	public void setCustomTypeFaceButton(EnumCustomTypeFace customTypeFace,
			Button button) {
		button.setTypeface(getCustomTypeFace(customTypeFace));
	}

	/**
	 * sets typeface on a textview
	 * 
	 * @param customTypeFace
	 * @param textView
	 */
	public void setCustomTypeFaceText(EnumCustomTypeFace customTypeFace,
			TextView textView) {
		textView.setTypeface(getCustomTypeFace(customTypeFace));
	}

	/**
	 * sets typeface on a textview
	 * 
	 * @param customTypeFace
	 * @param Edit Text
	 */
	public void setCustomTypeFaceEditText(EnumCustomTypeFace customTypeFace,
			EditText editText) {
		editText.setTypeface(getCustomTypeFace(customTypeFace));
	}

	/**
	 * 
	 * @param customTypeFace
	 * @return
	 */
	public Typeface getCustomTypeFace(EnumCustomTypeFace customTypeFace) {
		switch (customTypeFace) {
		case TITILLIUM_LIGHT:
			return titilliumTextLight;
		case TITILLIUM_DARK:
			return titilliumTextDark;
		case TITILLIUM_MEDIUM:
			return titilliumTextMedium;
		case TITILLIUM_MEDIUM_1:
			return titilliumTextMedium_1;
		case TITILLIUM_MEDIUM_2:
			return titilliumTextMedium_2;

		case ICOMOON:
			return icomoon;
		default:
			return icomoon;
		}

	}

}

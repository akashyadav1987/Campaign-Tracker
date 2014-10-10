package com.pulp.campaigntracker.controllers;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.pulp.campaigntracker.R;
import com.pulp.campaigntracker.beans.UserFormDetails;

public class UserFormAdapter extends BaseAdapter {

	private static final String TAG = UserFormAdapter.class.getSimpleName();
	private Context mContext;
	private List<UserFormDetails> mUserForm;

	ViewHolder viewHolder = null;
	int pos = 0;
	private LayoutInflater layoutInflater;

	public UserFormAdapter(Context mContext, List<UserFormDetails> mUserForm) {

		this.mContext = mContext;
		this.mUserForm = mUserForm;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserForm.size();
	}

	@Override
	public UserFormDetails getItem(int position) {
		// TODO Auto-generated method stub
		return mUserForm.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mUserForm.indexOf(getItem(position));
	}

	public void setItemValue(String Label, String value) {
		// TODO Auto-generated method stub

		for (int x = 0; x < mUserForm.size(); x++) {
			if (mUserForm.get(x).getFieldName().equals(Label)) {
				mUserForm.get(x).setFieldValue(value);
				break;
			} else {
				// error tbd
			}

		}
	}

	public String getItemValue(String Label) {
		// TODO Auto-generated method stub

		for (int x = 0; x < mUserForm.size(); x++) {
			if (mUserForm.get(x).getFieldName().equals(Label)) {
				return mUserForm.get(x).getFieldValue();

			}

		}
		return null;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = layoutInflater
				.inflate(R.layout.user_form_list_item, null);
		viewHolder = new ViewHolder(convertView, this, position);
		convertView.setTag(viewHolder);

		viewHolder.formFieldLabel.setText(getItem(position).getFieldName());

		if (getItem(position).getFieldType().equals("date")) {
			viewHolder.formFeildValue.setVisibility(View.GONE);
			viewHolder.activityDate.setVisibility(View.VISIBLE);
		} else {

			viewHolder.formFeildValue.setVisibility(View.VISIBLE);
			viewHolder.activityDate.setVisibility(View.GONE);

		}

		if (getItemValue(getItem(position).getFieldName()) != null) {
			viewHolder.formFeildValue.setText(getItemValue(getItem(position)
					.getFieldName()));
		}
		return convertView;
	}

	public class ViewHolder {
		private TextView formFieldLabel;
		private EditText formFeildValue;
		private DatePicker activityDate;
		private int position;

		public ViewHolder(View convertView,
				final UserFormAdapter userFormAdapter, int position) {

			this.formFieldLabel = (TextView) convertView
					.findViewById(R.id.formFieldLabel);

			this.position = position;

			this.activityDate = (DatePicker) convertView
					.findViewById(R.id.datePicker1);

			this.formFeildValue = (EditText) convertView
					.findViewById(R.id.formFeildValue);

			this.formFeildValue.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					userFormAdapter.setItemValue(formFieldLabel.getText()
							.toString(), s.toString());

				}
			});

		}

		private int getPosition() {

			return this.position;
		}

	}

}

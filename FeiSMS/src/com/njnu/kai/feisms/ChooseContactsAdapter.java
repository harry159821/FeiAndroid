package com.njnu.kai.feisms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.njnu.kai.feisms.ContactsData.ContactsInfo;

public class ChooseContactsAdapter extends BaseAdapter {

	private static final String PREFIX = "ChooseContactsAdapter";
	private ContactsData mContactsData;
	private boolean mDisplayDifference;
	private Context mContext;
	private FeiSMSDataManager mDataManager;

	class ContactsForDisplay {
		long mContactsId;
		String mName;
		String mPhone;

		@Override
		public String toString() {
			StringBuilder build = new StringBuilder(32);
			build.append(mName);
			while (build.length() < 3) {
				build.append('\u3000'); // 全角空格
			}
			build.append(": ");
			build.append(mPhone);
			return build.toString();
		}
	}

	private List<ContactsForDisplay> mListContactsForDisplay;

	private long[] mUsedContactsId;
	private String[] mUsedContactsPhone;

	public ChooseContactsAdapter(Context context) {
		mContext = context;
		mDataManager = FeiSMSDataManager.getDefaultInstance(context);
	}

	public void setContactsData(ContactsData contactsData) {
		mContactsData = contactsData;
		mListContactsForDisplay = new ArrayList<ContactsForDisplay>(mContactsData.getPhoneCount());
	}

	public void refreshContactsData(boolean isDisplayDifference) {
		if (mContactsData == null) return;

		mDisplayDifference = isDisplayDifference;
		mListContactsForDisplay.clear();
		mUsedContactsId = null;
		mUsedContactsPhone = null;

		if (isDisplayDifference) {

		} else {
			mUsedContactsId = mDataManager.getUsedContactsId();
			int idx = -1;
			int totalContactsCount = mContactsData.getContactsCount();
			for (int i = 0; i < totalContactsCount; ++i) {
				ContactsInfo info = mContactsData.getContactsInfo(i);
				if (Arrays.binarySearch(mUsedContactsId, info.getId()) < 0) {
					int infoPhoneCount = info.getPhoneNumberCount();
					for (int j = 0; j < infoPhoneCount; ++j) {
						ContactsForDisplay cfd = new ContactsForDisplay();
						cfd.mContactsId = info.getId();
						cfd.mName = info.getName();
						cfd.mPhone = info.getPhoneNumber(j);
						mListContactsForDisplay.add(cfd);
					}
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListContactsForDisplay != null ? mListContactsForDisplay.size() : 0;
	}

	@Override
	public ContactsForDisplay getItem(int position) {
		return mListContactsForDisplay.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).mContactsId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.simple_list_item_checked, null);
		}
		CheckedTextView tv = (CheckedTextView) convertView;
		tv.setText(getItem(position).toString());
//		Log.i(PREFIX, "getView=" + convertView);
		return convertView;
	}

}
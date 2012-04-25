package com.creativedroid.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Converts telephone number to contact name
 */
public class ContactManager {

	private ContentResolver contentResolver;

	public ContactManager(Context context) {
		contentResolver = context.getContentResolver();
	}

	public String getContactByPhoneNumber(String num) {

		String name_r = num == "" ? "Unknown" : num;
		Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
				Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
				while (pCur.moveToNext()) {
					String Phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "");
					if (Phone.equals(num)) {
						name_r = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					}
				}
				pCur.close();
			}
		}
		cur.close();
		return name_r;
	}
}

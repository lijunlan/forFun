package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.MyLog;

public class AboutActivity extends SherlockActivity {

	private static final String TAG = "com.melonstudio.appworks.AboutActivity";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@ViewInject(id = R.id.about_layout_listview)
	private ListView mListView;

	private PersonItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_layout);

		FinalActivity.initInjectedView(this);

		adapter = new PersonItemAdapter(getApplicationContext());
		adapter.addSeperator();
		adapter.addItem("指导老师 Afan", "才华横溢风流倜傥的副教授", R.drawable.abouth0);
		adapter.addSeperator();
		adapter.addItem("屌丝程序猿一号", "Android客户端开发", R.drawable.abouth1);
		adapter.addItem("屌丝程序猿二号", "服务器端研发", R.drawable.abouth2);
		adapter.addSeperator();
		adapter.addItem("屌丝树莓狗一号", "UE & UI & Documentation", R.drawable.about4);
		adapter.addSeperator();
		adapter.addItem("树莓女神一号", "UE & UI & Documentation", R.drawable.about3);
		adapter.addSeperator();

		mListView.setAdapter(adapter);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		log("onCreate in AboutActivity");
	}

	@Override
	protected void onDestroy() {
		log("onDestroy in AboutActivity");
		super.onDestroy();
	}

	private class PersonItem {
		String name;
		String desc;
		int img;
		int type;
	}

	private class ViewHolder {
		CircularImage header;
		TextView name;
		TextView desc;
	}

	private class FrameViewHolder {

	}

	private class PersonItemAdapter extends BaseAdapter {

		final int TYPE_PERSON = 0;
		final int TYPE_SEPERATOR = 1;
		final int TYPE_NUM = TYPE_SEPERATOR + 1;

		private List<PersonItem> list;
		private LayoutInflater layoutInflater;

		public PersonItemAdapter(Context context) {
			list = new ArrayList<PersonItem>();
			layoutInflater = LayoutInflater.from(context);
		}

		public void addItem(String name, String desc, int resource) {
			PersonItem personItem = new PersonItem();
			personItem.type = TYPE_PERSON;
			personItem.name = name;
			personItem.desc = desc;
			personItem.img = resource;
			list.add(personItem);
		}

		public void addSeperator() {
			PersonItem personItem = new PersonItem();
			personItem.type = TYPE_SEPERATOR;
			list.add(personItem);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return list.get(position).type;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_NUM;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			FrameViewHolder frameViewHolder = null;
			PersonItem personItem = (PersonItem) getItem(position);
			if (convertView == null) {
				switch (personItem.type) {
				case TYPE_PERSON:
					viewHolder = new ViewHolder();
					convertView = layoutInflater.inflate(
							R.layout.about_layout_item, null);
					viewHolder.header = (CircularImage) convertView
							.findViewById(R.id.about_item_header);
					viewHolder.name = (TextView) convertView
							.findViewById(R.id.about_item_name);
					viewHolder.desc = (TextView) convertView
							.findViewById(R.id.about_item_desc);
					viewHolder.header.setVisibility(View.VISIBLE);
					viewHolder.header.setImageResource(personItem.img);
					viewHolder.name.setText(personItem.name);
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					viewHolder.desc.setText(personItem.desc);
					convertView.setTag(R.layout.about_layout_item, viewHolder);
					break;
				case TYPE_SEPERATOR:
					frameViewHolder = new FrameViewHolder();
					convertView = layoutInflater.inflate(
							R.layout.about_seperator, parent, false);
					convertView.setTag(R.layout.about_seperator,
							frameViewHolder);
					break;
				default:
					break;
				}
			} else {
				switch (personItem.type) {
				case TYPE_PERSON:
					viewHolder = (ViewHolder) convertView
							.getTag(R.layout.about_layout_item);
					viewHolder.header.setVisibility(View.VISIBLE);
					viewHolder.header.setImageResource(personItem.img);
					viewHolder.name.setText(personItem.name);
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					viewHolder.desc.setText(personItem.desc);
					break;
				case TYPE_SEPERATOR:
					frameViewHolder = (FrameViewHolder) convertView
							.getTag(R.layout.about_seperator);
					break;
				default:
					break;
				}

			}

			return convertView;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

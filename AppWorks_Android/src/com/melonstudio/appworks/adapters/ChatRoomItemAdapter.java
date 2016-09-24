package com.melonstudio.appworks.adapters;

import java.util.List;

import com.melonstudio.appworks.R;
import com.melonstudio.model.ChatRoomInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author Wo
 * 
 *         ���������������б���ÿһ��item����ͼ
 */
public class ChatRoomItemAdapter extends BaseAdapter {

	private Context context;
	private List<ChatRoomInfo> chatRoomInfos;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<ChatRoomInfo> getChatRoomInfos() {
		return chatRoomInfos;
	}

	public void setChatRoomInfos(List<ChatRoomInfo> chatRoomInfos) {
		this.chatRoomInfos = chatRoomInfos;
	}

	public ChatRoomItemAdapter(Context context, List<ChatRoomInfo> chatRoomInfos) {
		this.context = context;
		if (chatRoomInfos != null)
			this.chatRoomInfos = chatRoomInfos;
	}

	@Override
	public int getCount() {
		return chatRoomInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return chatRoomInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = LayoutInflater.from(context).inflate(
				R.layout.activity_chatroom_list_item, null);
		TextView nameView = (TextView) view
				.findViewById(R.id.chatroom_item_name_id);
		nameView.setText(chatRoomInfos.get(position).getName());
		// û������ͷ������� TODO
		return view;
	}

	/**
	 * ֪ͨ����������
	 */
	public void refresh() {
		this.notifyDataSetChanged();
	}

}

package com.melonstudio.appworks.adapters;

import java.util.ArrayList;
import java.util.List;

import com.melonstudio.appworks.R;
import com.melonstudio.model.ChatRoomInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatRoomItemAdapterLocal extends BaseAdapter {

	private Context context;
	private List<ChatRoomInfo> chatRoomInfos = new ArrayList<ChatRoomInfo>();

	public ChatRoomItemAdapterLocal(Context context, List<ChatRoomInfo> list) {
		this.context = context;
		if (list != null)
			this.chatRoomInfos.addAll(list);
	}

	public List<ChatRoomInfo> getChatRoomInfos() {
		return chatRoomInfos;
	}

	public void setChatRoomInfos(List<ChatRoomInfo> chatRoomInfos) {
		this.chatRoomInfos = chatRoomInfos;
	}

	@Override
	public int getCount() {
		return chatRoomInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return chatRoomInfos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = LayoutInflater.from(context).inflate(
				R.layout.activity_chatroom_list_item_local, null);
		/**
		 * 设置头像 TODO
		 */
		/**
		 * 设置群名称和最新消息
		 */
		TextView nameTextView = (TextView) view
				.findViewById(R.id.chatroom_item_name_local_id);
		nameTextView.setText(chatRoomInfos.get(position).getName());
		TextView lastMessageTextView = (TextView) view
				.findViewById(R.id.chatroom_item_last_message_local_id);
		lastMessageTextView.setText(chatRoomInfos.get(position)
				.getLastMessage());
		TextView unreadTextView = (TextView) view
				.findViewById(R.id.chatroom_item_unread_id);
		if (chatRoomInfos.get(position).getUnreadItems() > 0) {			
			unreadTextView
					.setText(String.valueOf(chatRoomInfos.get(position).getUnreadItems()));
		}
		return view;
	}

	/**
	 * 通知父容器更新
	 */
	public void refresh() {
		this.notifyDataSetChanged();
	}

}

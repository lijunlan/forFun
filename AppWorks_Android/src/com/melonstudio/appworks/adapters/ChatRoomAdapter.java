package com.melonstudio.appworks.adapters;

import java.util.List;

import com.melonstudio.appworks.R;
import com.melonstudio.model.ChatRoomMessage;
import com.melonstudio.util.TimeUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 2014-04-19 �û�������Ϣ��Adapter
 * 
 * @author Wo
 * 
 */
@SuppressLint({ "ViewHolder", "InflateParams" })
public class ChatRoomAdapter extends BaseAdapter {

	private Context context;
	private List<ChatRoomMessage> messages;

	public ChatRoomAdapter(Context context, List<ChatRoomMessage> messages) {
		this.context = context;
		this.messages = messages;
	}

	public List<ChatRoomMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatRoomMessage> messages) {
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int arg0) {
		return messages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View view, ViewGroup mParent) {
		TextView contentView;
		switch (messages.get(index).getType()) {
		case ChatRoomMessage.MessageType_Time:
			view = LayoutInflater.from(context).inflate(
					R.layout.activity_chatroom_time, null);
			contentView = (TextView) view
					.findViewById(R.id.activity_chatroom_time_text);
			contentView.setText(TimeUtil.formatDateTime(messages.get(index)
					.getTime()));
			break;
		case ChatRoomMessage.MessageType_Receive:
			view = LayoutInflater.from(context).inflate(
					R.layout.activity_chatroom_recieve, null);
			contentView = (TextView) view
					.findViewById(R.id.activity_chatroom_receive_text);
			contentView.setText(messages.get(index).getContent());
			break;
		case ChatRoomMessage.MessageType_Send:
			view = LayoutInflater.from(context).inflate(
					R.layout.activity_chatroom_send, null);
			contentView = (TextView) view
					.findViewById(R.id.activity_chatroom_send_text);
			contentView.setText(messages.get(index).getContent());
			break;
		default:
			break;
		}
		return view;
	}

	/**
	 * ֪ͨ�������������и���
	 */
	public void refresh() {
		this.notifyDataSetChanged();
	}

}

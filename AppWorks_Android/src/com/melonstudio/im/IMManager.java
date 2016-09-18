package com.melonstudio.im;

import javax.security.auth.login.LoginException;

import com.melonstudio.exceptions.IMRegistException;
import com.melonstudio.exceptions.IMUpdateException;

/**
 * 
 * @author Wo
 * 
 */
public interface IMManager {

	/**
	 * 连接到服务器
	 * 
	 * @param parameter
	 *            parameter中包含服务器地址、端口等参数
	 * @return 连接到服务器的结果
	 */
	public int connect(IMParameter parameter);

	/**
	 * 用户注册
	 * 
	 * @param parameter
	 *            包含用户名、密码等参数
	 * @return 注册的结果
	 * @throws IMRegistException
	 */
	public int regist(IMParameter parameter) throws IMRegistException;

	/**
	 * 用户发送消息
	 * 
	 * @param parameter
	 *            包含要发送的信息、表情等信息和要发送到的群组
	 * @return 发送的结果
	 */
	public int send(IMParameter parameter);

	/**
	 * 用户登录
	 * 
	 * @param parameter
	 *            包含用户名、密码等参数
	 * @return 登录的结果
	 * @throws LoginException
	 * 
	 */
	public int login(IMParameter parameter) throws LoginException;

	/**
	 * 用户更改密码
	 * 
	 * @param parameter
	 *            待更新的个人用户的信息：密码等参数
	 * @return 更改信息的结果
	 * @throws IMUpdateException
	 */
	public int updateInfo(IMParameter parameter) throws IMUpdateException;

	/**
	 * 用户登出
	 * 
	 * @param parameter
	 *            退出登录的参数
	 * @return 退出的结果
	 */
	public int logout(IMParameter parameter);

	/**
	 * 建立聊天室（群组、粉丝圈之类的东西）
	 * 
	 * @param parameter
	 * @return 建立的结果
	 */
	public int createChatRoom(IMParameter parameter);

	/**
	 * 加入聊天室
	 * 
	 * 
	 * @param parameter
	 *            parameter里包含的信息有：chatroom名，密码，昵称，要浏览的历史数量
	 * @return
	 */
	public int joinChatRoom(IMParameter parameter);

	/**
	 * 查询服务器中属于本App的所有聊天室，并且在本方法中通过接口传数据给Activity
	 * 
	 * @param parameter
	 *            要包含应用名称，和ChatRoomListActivity的实例，
	 *            ChatRoomLIstActivity要实现SmackManager中的IChatRoomListAgent接口
	 */
	public void searchAllChatrooms(IMParameter parameter);

	/**
	 * 在ChatRoomActivity的onStop中调用。当Activity退居幕后的时候要通知IMManager，
	 * 将保存的ChatRoomActivity对象置位null
	 * 
	 * @param parameter
	 */
	public void leaveChatroom(IMParameter parameter);

	/**
	 * 在ChatRoomActivity的onStart中调用。当ChatRoomActivity来到屏幕前台的时候，同志IMManager，
	 * 将保存的ChatRoomActivity对象设置为IMParameter对象传来的参数
	 * 
	 * @param parameter
	 *            包含ChatRoomActivity对象的实例
	 */
	public void getIntoChatroom(IMParameter parameter);
	
	/**
	 * 当前在最前台的Activity是ChatRoomListActivity，此时将ChatRoomActivity的实例赋值给IMManager中的代理对象
	 * @param parameter 传递ChatRoomActivity的对象实例
	 */
	public void enterChatRoomList(IMParameter parameter);
	/**
	 * ChatRoomListActivity离开前台，此时将IMManager中的代理对象赋值为null
	 * @param parameter
	 */
	public void leaveChatRoomList(IMParameter parameter);
}

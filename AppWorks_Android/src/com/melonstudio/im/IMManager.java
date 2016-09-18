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
	 * ���ӵ�������
	 * 
	 * @param parameter
	 *            parameter�а�����������ַ���˿ڵȲ���
	 * @return ���ӵ��������Ľ��
	 */
	public int connect(IMParameter parameter);

	/**
	 * �û�ע��
	 * 
	 * @param parameter
	 *            �����û���������Ȳ���
	 * @return ע��Ľ��
	 * @throws IMRegistException
	 */
	public int regist(IMParameter parameter) throws IMRegistException;

	/**
	 * �û�������Ϣ
	 * 
	 * @param parameter
	 *            ����Ҫ���͵���Ϣ���������Ϣ��Ҫ���͵���Ⱥ��
	 * @return ���͵Ľ��
	 */
	public int send(IMParameter parameter);

	/**
	 * �û���¼
	 * 
	 * @param parameter
	 *            �����û���������Ȳ���
	 * @return ��¼�Ľ��
	 * @throws LoginException
	 * 
	 */
	public int login(IMParameter parameter) throws LoginException;

	/**
	 * �û���������
	 * 
	 * @param parameter
	 *            �����µĸ����û�����Ϣ������Ȳ���
	 * @return ������Ϣ�Ľ��
	 * @throws IMUpdateException
	 */
	public int updateInfo(IMParameter parameter) throws IMUpdateException;

	/**
	 * �û��ǳ�
	 * 
	 * @param parameter
	 *            �˳���¼�Ĳ���
	 * @return �˳��Ľ��
	 */
	public int logout(IMParameter parameter);

	/**
	 * ���������ң�Ⱥ�顢��˿Ȧ֮��Ķ�����
	 * 
	 * @param parameter
	 * @return �����Ľ��
	 */
	public int createChatRoom(IMParameter parameter);

	/**
	 * ����������
	 * 
	 * 
	 * @param parameter
	 *            parameter���������Ϣ�У�chatroom�������룬�ǳƣ�Ҫ�������ʷ����
	 * @return
	 */
	public int joinChatRoom(IMParameter parameter);

	/**
	 * ��ѯ�����������ڱ�App�����������ң������ڱ�������ͨ���ӿڴ����ݸ�Activity
	 * 
	 * @param parameter
	 *            Ҫ����Ӧ�����ƣ���ChatRoomListActivity��ʵ����
	 *            ChatRoomLIstActivityҪʵ��SmackManager�е�IChatRoomListAgent�ӿ�
	 */
	public void searchAllChatrooms(IMParameter parameter);

	/**
	 * ��ChatRoomActivity��onStop�е��á���Activity�˾�Ļ���ʱ��Ҫ֪ͨIMManager��
	 * �������ChatRoomActivity������λnull
	 * 
	 * @param parameter
	 */
	public void leaveChatroom(IMParameter parameter);

	/**
	 * ��ChatRoomActivity��onStart�е��á���ChatRoomActivity������Ļǰ̨��ʱ��ͬ־IMManager��
	 * �������ChatRoomActivity��������ΪIMParameter�������Ĳ���
	 * 
	 * @param parameter
	 *            ����ChatRoomActivity�����ʵ��
	 */
	public void getIntoChatroom(IMParameter parameter);
	
	/**
	 * ��ǰ����ǰ̨��Activity��ChatRoomListActivity����ʱ��ChatRoomActivity��ʵ����ֵ��IMManager�еĴ������
	 * @param parameter ����ChatRoomActivity�Ķ���ʵ��
	 */
	public void enterChatRoomList(IMParameter parameter);
	/**
	 * ChatRoomListActivity�뿪ǰ̨����ʱ��IMManager�еĴ������ֵΪnull
	 * @param parameter
	 */
	public void leaveChatRoomList(IMParameter parameter);
}

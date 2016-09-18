package com.sdll18.umedia.Deal.Service;

import java.util.Calendar;
import java.util.UUID;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.PackUtil.PackWarUtil;
import com.sdll18.umedia.Persistant.Application;
import com.sdll18.umedia.Persistant.User;
import com.sdll18.umedia.Service.ApplicationService;
import com.sdll18.umedia.Service.UserService;

public class UpAppService extends MsgService {

	private ApplicationService applicationService;

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("name")
				&& getData().containsKey("user_id")
				&& getData().containsKey("description")
				&& getData().containsKey("url")
				&& getData().containsKey("model");
	}

	@Override
	public void doit() {
		String name = getData().get("name");
		String description = getData().get("description");
		String url = getData().get("url");
		String key = UUID.randomUUID().toString();
		int user_id = Integer.parseInt(getData().get("user_id"));
		User user = getUserService().query(user_id, false);
		if (user != null) {
			// String model = getData().get("model");
			Application application = new Application();
			application.setCreateUser(user);
			application.setDescription(description);
			application.setName(name);
			application.setUrl(url);
			application.setSecretKey(key);
			application.setCreateTime(""
					+ Calendar.getInstance().getTimeInMillis());
			application.setState("creating");
			getApplicationService().create(application);
			String downUrl = "http://www.sdll18.com/UMediaService/downfile?path="
					+ application.getId() + ".apk";
			PackWarUtil.create(name, user.getIconUrl(), user.getName(),
					user.getIntroduce(), downUrl, "" + user_id, ""
							+ application.getId());
			// // TODO
			// String fileUrl = "http://www.sdll18.com/" + name
			// + "/appManager/upPassage.html";
			setResMsg(MsgUtil.getSuccessMsg("create successfully"));
		} else {
			setResMsg(MsgUtil.getErrorMsg("user is not existed"));
		}
	}

}

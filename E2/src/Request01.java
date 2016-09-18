// 具体请求类01  
class Request01 extends AbstractRequest {

	// 构造函数，调用父类的构造函数
	public Request01(String content) {
		super(content);
		// TODO Auto-generated constructor stub
	}


	// 获取请求的等级
	@Override
	public int getRequestLevel() {
		// TODO Auto-generated method stub
		return 1;
	}
}

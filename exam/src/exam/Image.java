package exam;

public abstract class Image {
	
	protected String path;
	protected String type;
	
	public Image(String p,String t){
		path = p;
		type = t;
	}
	
	public abstract byte[] getByte();
	
	public String getType(){
		return type;
	}

}

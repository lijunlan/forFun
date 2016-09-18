package exam;

public class DisplayImage {
	
	private DealAdapter dealAdapter;
	private Image image;
	
	public DisplayImage(DealAdapter adapter,Image i){
		dealAdapter = adapter;
		image = i;
	}
	
	public void doit(){
		dealAdapter.dealImage(image);
		dealAdapter.displayImage();
	}
}

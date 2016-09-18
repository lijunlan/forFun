package com.example.etc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(1, 1, 1, "FUCK");
		menu.add(1,2,2,"BITCH");
		menu.add(1,3,3,"SHIT");
		menu.addSubMenu(1, 4, 4, "MORE").add("GIRL");
		
		return true;
		
	}
	
}

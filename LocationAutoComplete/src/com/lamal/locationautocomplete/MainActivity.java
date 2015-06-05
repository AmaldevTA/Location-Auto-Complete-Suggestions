package com.lamal.locationautocomplete;



import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import com.lamal.locationautocomplete.R.id;
import com.lamal.locationautocomplete.autocomplete.LocationParser;

public class MainActivity extends Activity {

	private static AutoCompleteTextView locationAutoComplete;
	
	private LocationParser locparser = new LocationParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationAutoComplete = (AutoCompleteTextView) findViewById(id.editText_location);
		
		locationAutoComplete.setThreshold(2);
		locationAutoComplete.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//placesTask = new PlacesTask();
				//placesTask.execute(s.toString());
				int[] uiBindTo = { R.id.textViewItem };
				int res = R.layout.list_view_row_item;
				locparser.parse(getApplicationContext(), s.toString(), locationAutoComplete, uiBindTo, res);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}

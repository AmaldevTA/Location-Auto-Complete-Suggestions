package com.lamal.locationautocomplete.autocomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;


public class LocationParser {
	
	private AutoCompleteTextView locationAutoComplete;
	private Context context;
	private int[] uiBindTo;
	private int resource;

	
	public void parse(Context con, String loc, AutoCompleteTextView view, int[] bindTo, int res) {
		context = con;
		locationAutoComplete = view;
		uiBindTo = bindTo;
		resource = res;

		
		new PlacesTask().execute(loc.toString());
		
		
	}

	private class PlacesTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {

		@Override
		protected List<HashMap<String, String>> doInBackground(String... place) {
			String data = getJson(place[0]);
			return getMapValue(data);
		}

		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			
			SimpleAdapter adapter1 = new SimpleAdapter(context,
				result, resource, from, uiBindTo);
			locationAutoComplete.setAdapter(adapter1);
		}
	}

	
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}
	
	private String getJson(String place){
		
		String data = "";

		String key = "key=AIzaSyArzuM6Bw81D4XYMr18fiuqGEz9QnvTSiY";

		String input = "";

		try {
			input = "input=" + URLEncoder.encode(place, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String types = "types=geocode";

		String sensor = "sensor=false";

		String parameters = input + "&" + types + "&" + sensor + "&" + key;

		String output = "json";

		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;
		//https://maps.googleapis.com/maps/api/place/autocomplete/json?input=moothakunnam&types=geocode&sensor=false&key=AIzaSyArzuM6Bw81D4XYMr18fiuqGEz9QnvTSiY

		try {
			data = downloadUrl(url);
		} catch (Exception e) {
		}
		return data;
	}
	
	private List<HashMap<String, String>> getMapValue(String uri){
		
		List<HashMap<String, String>> places = null;

		PlaceJSONParser placeJsonParser = new PlaceJSONParser();
		JSONObject jObject;
		try {
			jObject = new JSONObject(uri);

			places = placeJsonParser.parse(jObject);

		} catch (Exception e) {
		}
		return places;
	}
	
}

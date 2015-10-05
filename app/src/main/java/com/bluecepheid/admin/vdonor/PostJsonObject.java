package com.bluecepheid.admin.vdonor;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;



public class PostJsonObject {
	//static final Logger log = Logger.getLogger(PostJsonObject.class); 
	

	
	
	
	public static String postJson(JSONObject object, String url) throws JSONException {
		String resp = "";

		try {
			HttpClient client = new DefaultHttpClient();
			
			//Setting proxy start
			/*String isEnable = Utility.getProperties("env.properties").getProperty("enableProxy");
			if(isEnable != null && isEnable.equalsIgnoreCase("yes"))
			{
				//log.debug("Proxy enabled..."+url);
				HttpHost proxy = new HttpHost("proxyarray.mazdausa.com",80);
	   	        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);    
			}	*/		
			//Setting proxy start
   	        
			HttpPost post = new HttpPost(url);
			post.addHeader("User-Agent", "android");

			StringEntity input = new StringEntity(object.toString());
			input.setContentType("application/json");

			post.setEntity(input);
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				resp = resp + line;
			}

		} catch (Exception e) {
			//log.debug("Exception occured while posting request data - " + e);
			e.printStackTrace();
			JSONObject exceptionMess = new JSONObject();
			exceptionMess.put("status", "failure");
			exceptionMess.put("description", "Exception");
			exceptionMess.put("statusMessage", "Please check your internet connection or try again later");
			resp = exceptionMess.toString();
		}
		//log.debug("Request completed >>>" + resp);
		return resp;
	}


	/*public static void main_x(String[] arg) {
		String url = "http://mobilegarage.mazdausa.com/MyMazdaGarage/client/user/login";

		JSONObject outer = new JSONObject();
		JSONObject inner = new JSONObject();

		StringEncrypter encrypto = new StringEncrypter();
		String encrptedPass = encrypto.encrypt("mazdausa");

		inner.put("userid", "carenrpt@hotmail.com");
		inner.put("password", encrptedPass "TzgaimRmE2EdGPBvkE5+rw==" );

		outer.put("data", inner.toString());
		outer.put("apnstoken", "simulator");
		//log.debug("json = " + outer.toString());

		String postResponse = PostJsonObject.postJson(outer, url);

		//log.debug("Response = " + postResponse);

	}*/
}

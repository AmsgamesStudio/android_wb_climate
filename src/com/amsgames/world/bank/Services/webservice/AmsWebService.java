package com.amsgames.world.bank.Services.webservice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import android.content.Context;

import com.amsgames.world.bank.Interfaces.IWSAdapter;
import com.amsgames.world.bank.Services.data.*;

/**
 * "Ams Games Web Service" allows to connect to JSON web services to send and receive data through a HTTP connection.
 * 
 * @author Alejandro Mostajo (AmsGames)
 *
 */
public class AmsWebService implements IWSAdapter {
	
	/*
	 * Attributes
	 */
	private String _serviceUrl;
	private int _serviceId;
	private Context _context;
	private String[] _params;
	private JSONObject _json;
    private HttpParams _httpParams;
    private HttpClient _httpClient;
	private HttpPost _httpPost;
	
	/**
	 * Default class constructor. Initializes the web service.
	 * 
	 * @param 	context			The ANDROID context or activity that creates the web service.
	 * @param	serviceUrlId	The ID in ANDROID resources where can be found the definition for the web service url path.
	 * @param	serviceId		The ID in ANDROID resources where can be found the definition for the web service.   	 
	 */
	public AmsWebService (Context context, int serviceId)	{
		this._context = context;
		this._serviceUrl = this._context.getString(com.amsgames.world.bank.R.string.wsServerUrl);
		this._serviceId = serviceId;
		this._params = new String[0];
		this.Initialize();
	}
	
	/**
	 * Initializes the web service.
	 * 
	 * @param 	context			The ANDROID context or activity that creates the web service.
	 * @param	serviceUrlId	The ID in ANDROID resources where can be found the definition for the web service url path.
	 * @param	serviceId		The ID in ANDROID resources where can be found the definition for the web service. 
	 * @param	params			List of the web service parameters values.    	 
	 */
	public AmsWebService (Context context, int serviceId, String[] params)	{
		this._context = context;
		this._serviceUrl = this._context.getString(com.amsgames.world.bank.R.string.wsServerUrl);
		this._serviceId = serviceId;
		this._params = params;
		this.Initialize();
	}
	
	/**
	 * Initialize Configuration, it also creates the service URL to call the service.
	 */
	private void Initialize () {
		try
		{
			// Normal attributes
			this._json = new JSONObject();
			this._httpParams = new BasicHttpParams();
			this._httpClient = new DefaultHttpClient(_httpParams);
			
			// 	Get web service information
			String[] auxWebService = this._context.getString(this._serviceId).split("!");
			// Parse data
			// 1) Get Web Service Name
			this._serviceUrl += auxWebService[0];
			String[] auxDefinitions = new String[0];
			String[] auxValues = this._params;
			// 2) Get Parameters
			if (auxWebService.length>1)
				auxDefinitions = auxWebService[1].split(",");
			// 3) Build Parameters
			for (int i=0;i<auxValues.length;i++) {
				String prefix = "&";
				// Check for first item
				if (i==0) {
					prefix = "?";
				}
				// Add parameter
				this._serviceUrl += prefix + auxDefinitions[i] + "=" + auxValues[i];
			}
			// Set URL
			this._httpPost = new HttpPost(_serviceUrl);
		} catch (Exception ex) {
			// Do nothing
		}
	}
	
	/* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.webservice.IWSAdapter#Call()
	 */
	public AmsWebServiceResult Call () {
		// Initialize call results
		AmsWebServiceResult results = new AmsWebServiceResult();
		try	{
			// Call
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = _httpClient.execute(_httpPost,responseHandler);
            // Parse
            _json = new JSONObject(responseBody);
            // Fill In Data Set
            AmsDataSet dataSet = new AmsDataSet();
            dataSet.FillWith(_json);
            results = new AmsWebServiceResult(dataSet);
		} catch (Exception ex) {
			// Get errors
			results.HasError = true;
			results.ErrorMessage = ex.getMessage();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.webservice.IWSAdapter#Call(int)
	 */
	public AmsWebServiceResult Call (int serviceId) {
		this._serviceId = serviceId;
		this._params = new String[0];
		this.Initialize();
		// Call service
		return this.Call();
	}
	
	/* (non-Javadoc)
	 * @see com.amsgames.world.bank.Services.webservice.IWSAdapter#Call(int, java.lang.String[])
	 */
	public AmsWebServiceResult Call (int serviceId, String[] params) {
		this._serviceId = serviceId;
		this._params = params;
		this.Initialize();
		// Call service
		return this.Call();
	}
}

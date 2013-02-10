package com.amsgames.world.bank.Interfaces;

import com.amsgames.world.bank.Services.webservice.AmsWebServiceResult;

public interface IWSAdapter {

	/**
	 * Call Web Service.
	 * 
	 * @return ({@link AmsWebServiceResult}) An object that contains all the information received from the call.
	 */
	public abstract AmsWebServiceResult Call();

	/**
	 * This method allows to change the web service once defined for a new one and calls it. 
	 * 
	 * @param	serviceId	The ID in ANDROID resources where can be found the definition for the web service.   
	 * 
	 * @return ({@link AmsWebServiceResult}) An object that contains all the information received from the call.
	 */
	public abstract AmsWebServiceResult Call(int serviceId);

	/**
	 * This method allows to change the web service once defined for a new one and calls it. 
	 * 
	 * @param	serviceId	The ID in ANDROID resources where can be found the definition for the web service. 
	 * @param	params		List of the web service parameters values.    	   
	 * 
	 * @return ({@link AmsWebServiceResult}) An object that contains all the information received from the call.
	 */
	public abstract AmsWebServiceResult Call(int serviceId, String[] params);

}
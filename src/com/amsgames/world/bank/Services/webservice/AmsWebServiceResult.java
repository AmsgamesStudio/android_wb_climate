package com.amsgames.world.bank.Services.webservice;

import com.amsgames.world.bank.Services.data.*;

/**
 * "Ams Games Web Service Result" contains information received by a web service call in {@link AmsWebService}.
 * 
 * @author Alejandro Mostajo (AmsGames)
 */
public class AmsWebServiceResult {
	
	/*
	 * Attributes
	 */
	/**
	 * Data Set generated by a web service call.
	 */
	public AmsDataSet Results;
	/**
	 * Indicates if a web service call returns a data set or not.
	 */
	public boolean Returns;
	/**
	 * Indicates if a web service call generated an error.
	 */
	public boolean HasError;
	/**
	 * Indicates the error message generated by a web service call.
	 */
	public String ErrorMessage;
	
	/**
	 * Default constructor. No errors and returns generated by the web service call.
	 */
	public AmsWebServiceResult () {
		Results = new AmsDataSet();
		Returns = false;
		HasError = false;
		ErrorMessage = "";
	}
	
	/**
	 * Initialize the result with errors and no results generated by the web service call.
	 * 
	 * @param errorMessage	The error message generated by a web service call.
	 */
	public AmsWebServiceResult (String errorMessage) {
		Results = new AmsDataSet();
		Returns = false;
		HasError = true;
		ErrorMessage = errorMessage;
	}
	
	/**
	 * Initialize the result with no errors and with results generated by the web service call.
	 * 
	 * @param results	Data Set information generated by the web service call.
	 */
	public AmsWebServiceResult (AmsDataSet results) {
		Results = results;
		Returns = true;
		HasError = false;
		ErrorMessage = "";
	}
	
	/**
	 * Initialize the result with errors and results generated by the web service call.
	 * 
	 * @param results	Data Set information generated by the web service call.
	 * @param errorMessage	The error message generated by a web service call.
	 */
	public AmsWebServiceResult (AmsDataSet results, String errorMessage) {
		Results = results;
		Returns = true;
		HasError = true;
		ErrorMessage = errorMessage;
	}
	
}

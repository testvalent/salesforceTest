/**
 * Created by snehal.mistry on 10/20/14.
 */

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.*;
import com.sforce.soap.partner.*;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;
import java.io.FileNotFoundException;

public class PartnerSamples {
	PartnerConnection partnerConnection = null;

	public static void main(String[] args) {
		PartnerSamples samples = new PartnerSamples();
		if (samples.login()) {
			// Add calls to the methods in this class.
			// For example:
			samples.querySample();
		}
	}



	private boolean login() {
		boolean success = false;
		String username = "testvalent@gmail.com";
		String password = "Welcome123!NzQa0CIJBwCZuxkdiuslpdTs";
		String instanceUri  = "www.salesforce.com";
		String authEndPoint = "https://" + instanceUri + "/services/Soap/u/23.0";

		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(username);
			config.setPassword(password);
			config.setAuthEndpoint(authEndPoint);
			config.setTraceFile("traceLogs.txt");
			config.setTraceMessage(true);
			config.setPrettyPrintXml(true);
			partnerConnection = new PartnerConnection(config);
			success = true;
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		return success;
	}

	public void querySample() {
		try {
			// Set query batch size
			partnerConnection.setQueryOptions(250);

			// SOQL query to use
			String soqlQuery = "SELECT FirstName, LastName FROM Contact";
			// Make the query call and get the query results
			QueryResult qr = partnerConnection.query(soqlQuery);

			boolean done = false;
			int loopCount = 0;
			// Loop through the batches of returned results
			while (!done) {
				System.out.println("Records in results set " + loopCount++
						+ " - ");
				SObject[] records = qr.getRecords();
				// Process the query results
				for (int i = 0; i < records.length; i++) {
					SObject contact = records[i];
					Object firstName = contact.getField("FirstName");
					Object lastName = contact.getField("LastName");
					if (firstName == null) {
						System.out.println("Contact " + (i + 1) +
								": " + lastName
								);
					} else {
						System.out.println("Contact " + (i + 1) + ": " +
								firstName + " " + lastName);
					}
				}
				if (qr.isDone()){
					done = true;
				}
				else{
					qr = partnerConnection.queryMore(qr.getQueryLocator());
				}
			}
		} 
		catch(ConnectionException ce) {
			ce.printStackTrace();
		}
		System.out.println("\nQuery execution completed.");
	}

	//
	// Add your methods here.
	//
}

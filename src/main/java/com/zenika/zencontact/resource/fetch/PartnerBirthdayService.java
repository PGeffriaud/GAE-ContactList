package com.zenika.zencontact.resource.fetch;

import com.google.appengine.api.urlfetch.*;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by PierreG on 01/03/17.
 *
 */
public class PartnerBirthdayService {

    private static PartnerBirthdayService INSTANCE = new PartnerBirthdayService();
    private URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();

    private Logger LOG = Logger.getLogger(PartnerBirthdayService.class.getSimpleName());

    public static PartnerBirthdayService getInstance() {
        return INSTANCE;
    }

    public String findBirthdata(String firstname, String lastname) {
        String birthdate = null;
        try {
            URL url = new URL("http://zenpartenaire.appspot.com/zenpartenaire");

            HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
            String payload = String.format("%s %s", firstname, lastname);
            request.setPayload(payload.getBytes());

            Future<HTTPResponse> futurePost = urlFetchService.fetchAsync(request);
            HTTPResponse response = futurePost.get();

            if(response.getResponseCode() != 200){
                LOG.warning("Birthdate not found: code " + response.getResponseCode() );
            }
            else {
                birthdate = new String(response.getContent()).trim();
            }

            LOG.warning("Birthdate found: " + birthdate);

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return birthdate;
    }
}

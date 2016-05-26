package edu.yale.library.logservice;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.tika.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * Tests Http Services
 * @author Osman Din
 */
public class LogServiceIT extends AbstractWarTest {

    private static final Logger logger = getLogger(LogServiceIT.class);

    private static final String HTTP_SERVICE = "log/hello";

    @BeforeClass
    public static void setup() throws MalformedURLException {
        try {
            AbstractWarTest.setupContainer();
        } catch (RuntimeException e) {
            logger.error("Error starting container", e);
            fail("Error starting container");
        }
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void testGET() throws Exception {
        HttpServiceTestUtil httpServiceTestUtil = new HttpServiceTestUtil();
        final HttpGet getMethod0 = httpServiceTestUtil.doGET(HTTP_SERVICE);
        final HttpResponse response0 = httpServiceTestUtil.httpClient.execute(getMethod0);
        assertNotNull(response0);
        assertEquals(IOUtils.toString(response0.getEntity().getContent()), 200,
                response0.getStatusLine().getStatusCode());
    }


    @Test
    public void testPOST() throws Exception {
        HttpServiceTestUtil httpServiceTestUtil = new HttpServiceTestUtil();
        final HttpPost post = httpServiceTestUtil.doPOST(HTTP_SERVICE);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("yale", "secret"));
        post.setEntity(new UrlEncodedFormEntity(nvps));
        final HttpResponse response0 = httpServiceTestUtil.httpClient.execute(post);
        assertNotNull(response0);
        assertEquals(IOUtils.toString(response0.getEntity().getContent()), 500,
                response0.getStatusLine().getStatusCode());
    }

}

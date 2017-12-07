package io.acari.asyncrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.Future;

@RestController
public class RestEndpoint {
    private RestTemplate restTemplate;
    private PoorMansExecutor poorMansExecutor;
    private static final Logger logger = LoggerFactory.getLogger(RestEndpoint.class);

    @Autowired
    public RestEndpoint(RestTemplate restTemplate, PoorMansExecutor poorMansExecutor) {
        this.restTemplate = restTemplate;
        this.poorMansExecutor = poorMansExecutor;
    }

    @RequestMapping("/")
    public String doStuff() {
        String forObject = restTemplate.getForObject("http://localhost/alpha", String.class);
        logger.info(forObject);
        return forObject;
    }

    @RequestMapping("/alpha")
    public String getAlpha() throws Exception {
        String alpha = getMessage("Alpha");
        Future<String> bravo = poorMansExecutor.submit(() -> restTemplate.getForObject("http://localhost/bravo", String.class));
        Future<String> charlie = poorMansExecutor.submit(() -> restTemplate.getForObject("http://localhost/charlie", String.class));
        return concatMessage(alpha, bravo.get(), charlie.get());
    }

    @RequestMapping("/bravo")
    public String getBravo() throws Exception {
        String bravo = getMessage("Bravo");
        Future<String> yankee = poorMansExecutor.submit(() -> restTemplate.getForObject("http://localhost/yankee", String.class));
        return concatMessage(bravo, yankee.get());
    }

    @RequestMapping("/charlie")
    public String getCharlie() throws Exception {
        String charlie = getMessage("Charlie");
        Future<String> xray = poorMansExecutor.submit(() -> restTemplate.getForObject("http://localhost/xray", String.class));
        Future<String> zulu = poorMansExecutor.submit(() -> restTemplate.getForObject("http://localhost/zulu", String.class));
        return concatMessage(charlie, xray.get(), zulu.get());
    }

    @RequestMapping("/xray")
    public String getXRay() {
        return getMessage("XRay");
    }

    @RequestMapping("/yankee")
    public String getYankee() {
        return getMessage("Yankee");
    }

    @RequestMapping("/zulu")
    public String getZulu() {
        return getMessage("Zulu");
    }

    private String getMessage(String serviceName) {
        return "Hello from " + serviceName + " @ " + Instant.now();
    }

    private String concatMessage(String... messages) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = messages.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(messages[i]);
            if (i < length - 1) {
                stringBuilder.append(" and ");
            }
        }
        return stringBuilder.toString();
    }
}

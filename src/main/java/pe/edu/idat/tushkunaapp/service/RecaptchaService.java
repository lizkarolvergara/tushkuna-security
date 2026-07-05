package pe.edu.idat.tushkunaapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class RecaptchaService {

    private final RestClient restClient = RestClient.create();

    @Value("${recaptcha.secret-key}")
    private String secret;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    public boolean verify(String token, String remoteIp) {
        if (token == null || token.isBlank()) return false;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secret);
        params.add("response", token);
        if (remoteIp != null && !remoteIp.isBlank())
            params.add("remoteip", remoteIp);

        Map<?, ?> resp = restClient.post()
                .uri(verifyUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(Map.class);

        if (resp == null) return false;
        Object obj = resp.get("success");
        return (obj instanceof Boolean rpta) && rpta;
    }
}
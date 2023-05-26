package cts.auth.aadauth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import cts.auth.aadauth.dto.AADException;
import cts.auth.aadauth.dto.AADResponse;
import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final OkHttpClient okHttpClient;

    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    String clientId;
    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    String clientSecret;

    @Value("${tenant-id}")
    String tenantId;

    public String getToken(LoginDTO loginDTO) throws IOException, ApiException {
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", loginDTO.getUsername())
                .add("password", loginDTO.getPassword())
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("scope", "openid")
                .build();

        Request request = new Request.Builder()
                .url("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();


        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            AADResponse data = new ObjectMapper().readValue(response.body().bytes(), AADResponse.class);
            response.close();
            return data.getIdToken();
        } else {
            AADException aadException = new ObjectMapper().readValue(response.body().bytes(), AADException.class);
            String message = aadException.getErrorDescription();
            throw new ApiException(message.substring(message.indexOf(':') + 1));
        }
    }
}

package cts.auth.aadauth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import cts.auth.aadauth.dto.AADException;
import cts.auth.aadauth.dto.AADResponse;
import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LoginService {
    private final OkHttpClient okHttpClient;
    public LoginService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getToken(LoginDTO loginDTO) throws IOException, ApiException {
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", loginDTO.getUsername())
                .add("password", loginDTO.getPassword())
                .add("client_id", "1c2a541b-e170-46d6-937c-223c553c1494")
                .add("client_secret", "c6L8Q~sRZUyuN254zroz02KYGIV1HOTrX93Bydg3")
                .add("scope", "openid")
                .build();

        Request request = new Request.Builder()
                .url("https://login.microsoftonline.com/bf58d208-4c98-44e6-9038-f51ff05cdc41/oauth2/v2.0/token")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();


        Response response = okHttpClient.newCall(request).execute();
        if(response.isSuccessful()) {
            AADResponse data = new ObjectMapper().readValue(response.body().bytes(), AADResponse.class);
            response.close();
            return data.getIdToken();
        }
        else{
            AADException aadException = new ObjectMapper().readValue(response.body().bytes(), AADException.class);
            String message = aadException.getErrorDescription();
            throw new ApiException(message.substring(message.indexOf(':') + 1));
        }
    }
}

package cts.auth.aadauth.services;

import com.azure.spring.cloud.core.implementation.util.AzureStringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import cts.auth.aadauth.dto.AADException;
import cts.auth.aadauth.dto.AADResponse;
import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AadResourceOwnerPasswordCredentialsImpl implements AadResourceOwnerPasswordCredentials {
    private final OkHttpClient okHttpClient;
    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    String clientId;
    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    String clientSecret;
    @Value("${tenant-id}")
    String tenantId;
    @Value("${scope}")
    String scope;

    @Override
    public String getToken(LoginDTO loginDTO) throws IOException, ApiException, MethodArgumentNotValidException, NoSuchMethodException {
        validateDTO(loginDTO);
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", loginDTO.getUsername())
                .add("password", loginDTO.getPassword())
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("scope", scope)
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

    public void validateDTO(LoginDTO loginDTO) throws MethodArgumentNotValidException, NoSuchMethodException {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        BindingResult bindingResult = new BindException(loginDTO, "loginDTO");

        if(username == null || StringUtils.isBlank(username) ||
                !username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            bindingResult.addError(new FieldError("loginDTO", "username", "Invalid Email"));

        }
        if(password == null && password.length() < 8){
            bindingResult.addError(new FieldError("loginDTO", "password", "Invalid Password"));
        }
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(getClass().getMethod("validateDTO", LoginDTO.class), 0);

            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
    }
}

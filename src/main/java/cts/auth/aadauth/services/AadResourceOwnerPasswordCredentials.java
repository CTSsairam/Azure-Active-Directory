package cts.auth.aadauth.services;

import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
@Service
public interface AadResourceOwnerPasswordCredentials {
    String getToken(LoginDTO loginDTO) throws IOException, ApiException, MethodArgumentNotValidException, NoSuchMethodException;


}

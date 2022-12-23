package com.example.cuckoolandback.user.jwt;

import com.example.cuckoolandback.majority.common.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException {

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getOutputStream().println(
                    "{ " +
                            "\"error\":\"" + authException.getMessage() + "\"," +
                            "\"msg\":\"" + Message.AUTHENTICATION_FAIL.getMsg() + "\"" +
                            " }"
            );
        }
    }

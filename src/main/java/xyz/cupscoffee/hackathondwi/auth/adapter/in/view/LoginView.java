package xyz.cupscoffee.hackathondwi.auth.adapter.in.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.LoginRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.response.JwtResponse;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.View;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;

@View
@RequiredArgsConstructor
public class LoginView {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String error;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public void login() {
        LoginRequest loginRequest = new LoginRequest(username, password);
        var response = restClient.post("/auth/login", loginRequest, JwtResponse.class);

        if (response.getStatus() != 200) {
            FailureResponse failureResponse = (FailureResponse) objectMapper.convertValue(
                    response.getBody(),
                    FailureResponse.class);

            error = failureResponse.getMessage();

            return;
        }

        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        // Save JWT in a cookie
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        Cookie jwtCookie = new Cookie("jwt", jwtResponse.getContent().getJwt());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        httpResponse.addCookie(jwtCookie);
    }
}

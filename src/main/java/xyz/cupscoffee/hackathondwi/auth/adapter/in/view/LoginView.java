package xyz.cupscoffee.hackathondwi.auth.adapter.in.view;

import static org.fusesource.jansi.Ansi.ansi;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.LoginRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.response.JwtResponse;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.View;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;

@Slf4j
@View
@RequiredArgsConstructor
public class LoginView {
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String error;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public void login() {
        LoginRequest loginRequest = new LoginRequest(code, password);
        var response = restClient.post("/auth/login", loginRequest, JwtResponse.class);

        if (response.getStatus() != 200) {
            log.error("Failed to login user: {}",
                    ansi().fgRed().a(response.getBody()).reset());

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

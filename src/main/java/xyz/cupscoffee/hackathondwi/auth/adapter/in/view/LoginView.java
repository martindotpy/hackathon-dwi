package xyz.cupscoffee.hackathondwi.auth.adapter.in.view;

import static org.fusesource.jansi.Ansi.ansi;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.LoginRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.response.JwtResponse;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;

@Slf4j
@ViewScoped
@Named
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

    public void login() {
        LoginRequest loginRequest = new LoginRequest(code, password);
        var response = restClient.post("/auth/login", loginRequest, JwtResponse.class);

        if (response.getStatus() != 200) {
            log.error("Failed to login user: {}",
                    ansi().fgRed().a(response.getBody()).reset());

            if (response.getStatus() == 401) {
                FaceShortcuts.showFailureMessage("auth.failure.invalid_credentials", "Invalid credentials");
            }

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

        // Redirection to home
        facesContext.getApplication().getNavigationHandler()
                .handleNavigation(facesContext, null, "/home.xhtml?faces-redirect=true");
    }
}

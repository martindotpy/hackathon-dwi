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
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;

/**
 * Login view.
 */
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
    private final RestClient restClient;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    public void login() {
        // Login request
        LoginRequest loginRequest = new LoginRequest(code, password);
        var response = restClient.post("/auth/login", loginRequest, JwtResponse.class);

        // Check response status
        if (response.getStatus() != 200) {
            // Invalid credentials
            if (response.getStatus() == 401) {
                log.error("Invalid credentials");

                FaceShortcuts.showFailureMessage("auth.failure.invalid_credentials", "Invalid credentials");
            }

            // Other cases
            log.error("Failed to login user: {}",
                    ansi().fgRed().a(response.getBody()).reset());
            return;
        }

        // Save JWT in a cookie
        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse httpResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Cookie jwtCookie = new Cookie("jwt", jwtResponse.getContent().getJwt());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");

        httpResponse.addCookie(jwtCookie);

        // Redirection to home
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null,
                "/home.xhtml?faces-redirect=true");
        FaceShortcuts.showOkMessage("auth.success.login", "Successfully logged in");
    }

    /**
     * Get the authenticated user.
     *
     * @return The authenticated user
     */
    public User getUser() {
        return getAuthenticatedUserPort.get().orElse(null);
    }

    /**
     * Logout the user.
     */
    public void logout() {
        // Delete all the cookies
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse httpResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        for (Object obj : facesContext.getExternalContext().getRequestCookieMap().values()) {
            Cookie cookie = (Cookie) obj;
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
        }

        log.debug("All cookies deleted");

        // Redirect to login and invalidate the session
        facesContext.getExternalContext().invalidateSession();

        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null,
                "/login.xhtml?faces-redirect=true");

        log.info("Successfully logged out");
    }
}

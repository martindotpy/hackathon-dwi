package xyz.cupscoffee.hackathondwi.auth.adapter.in.view;

import static org.fusesource.jansi.Ansi.ansi;
import static xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts.showFailureMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.request.RegisterRequest;
import xyz.cupscoffee.hackathondwi.auth.adapter.in.response.JwtResponse;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.ObjectMapperShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;
import xyz.cupscoffee.hackathondwi.user.core.domain.payload.RegisterUserPayload;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public class RegisterView {
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String confirmPassword;
    private final RestClient restClient;

    public void register() {
        // Register request
        RegisterUserPayload request = RegisterRequest.builder()
                .code(code)
                .name(name)
                .password(password)
                .build();
        var response = restClient.post("/auth/register", request, JwtResponse.class);

        // Check response status
        if (response.getStatus() != 200) {
            // Bad request
            if (response.getStatus() == 400) {
                log.error("Failed to register user, bad request");

                DetailedFailureResponse failure = (DetailedFailureResponse) ObjectMapperShortcuts
                        .map(response.getBodyAsString(), DetailedFailureResponse.class);

                showFailureMessage(failure);
                return;
            }

            // Other cases
            FailureResponse failure = (FailureResponse) ObjectMapperShortcuts
                    .map(response.getBodyAsString(), FailureResponse.class);

            showFailureMessage(failure);

            log.error("Failed to register user: {}",
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

        FaceShortcuts.showOkMessage("auth.register.success", "User registered successfully!");

        // Redirect to home
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null,
                "/home.xhtml?faces-redirect=true");
    }
}

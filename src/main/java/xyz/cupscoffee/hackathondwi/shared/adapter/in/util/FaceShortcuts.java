package xyz.cupscoffee.hackathondwi.shared.adapter.in.util;

import org.springframework.stereotype.Component;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;

/**
 * Faces shortcuts.
 */
@Slf4j
@Component
public final class FaceShortcuts {
    /**
     * Show a failure message to the user.
     *
     * @param failureResponse The failure response to show.
     */
    public static void showFailureMessage(DetailedFailureResponse failureResponse) {
        StringBuilder sb = new StringBuilder();

        for (String detail : failureResponse.getDetails()) {
            String detailMessage = detail.split(": ")[1];

            sb.append("* ").append(detailMessage);

            if (!detailMessage.endsWith("."))
                sb.append(". ");

            sb.append("\n");
        }

        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                failureResponse.getMessage(),
                sb.toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Show a failure message to the user.
     *
     * @param failureResponse The failure response to show.
     */
    public static void showFailureMessage(FailureResponse failureResponse) {
        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                failureResponse.getMessage(),
                null);

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void showFailureMessage(String code, String defaultMessage) {
        FacesMessage facesMessage = getFacesMessage(FacesMessage.SEVERITY_ERROR, code, defaultMessage);

        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public static void showOkMessage(String code, String defaultMessage) {
        FacesMessage facesMessage = getFacesMessage(FacesMessage.SEVERITY_INFO, code, defaultMessage);

        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    private static FacesMessage getFacesMessage(
            Severity severity,
            String code, String defaultMessage) {
        String message = MessagesShortcuts.getMessage(code, defaultMessage);

        return new FacesMessage(severity, message, null);
    }

    public static String getQueryParam(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            log.error("Error redirecting to {}", url, e);
        }
    }
}

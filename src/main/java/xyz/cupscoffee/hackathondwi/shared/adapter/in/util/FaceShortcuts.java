package xyz.cupscoffee.hackathondwi.shared.adapter.in.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.application.response.DetailedFailureResponse;
import xyz.cupscoffee.hackathondwi.shared.application.response.FailureResponse;

@Slf4j
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
}

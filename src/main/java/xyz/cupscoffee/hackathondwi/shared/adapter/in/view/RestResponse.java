package xyz.cupscoffee.hackathondwi.shared.adapter.in.view;

import lombok.Builder;
import lombok.Getter;

/**
 * Rest response.
 */
@Builder
@Getter
public final class RestResponse<T> {
    private int status;
    private T body;

    /**
     * Create a new {@link RestResponse}.
     *
     * @param <T>    the type of the body.
     * @param status the status.
     * @param body   the body.
     * @return the response
     */
    public static <T> RestResponse<T> of(int status, T body) {
        return RestResponse.<T>builder()
                .status(status)
                .body(body)
                .build();
    }

    /**
     * Create a new {@link RestResponse}.
     *
     * @param status the status.
     * @return the response
     */
    public static RestResponse<Void> of(int status) {
        return RestResponse.<Void>builder()
                .status(status)
                .build();
    }

    /**
     * Get the body as a string.
     *
     * @return the body as a string
     */
    public String getBodyAsString() {
        return (String) body;
    }
}

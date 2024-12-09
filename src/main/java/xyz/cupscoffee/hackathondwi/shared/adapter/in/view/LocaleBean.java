package xyz.cupscoffee.hackathondwi.shared.adapter.in.view;

import java.util.Locale;

import org.springframework.web.servlet.LocaleResolver;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public class LocaleBean {
    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public String getLanguage() {
        String language = localeResolver.resolveLocale(request).getLanguage();

        FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.forLanguageTag(language));

        return language;
    }

    public void setLanguage(String language) {
        changeLanguage(language);
    }

    public void changeLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);

        localeResolver.setLocale(request, response, locale);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

        log.debug("Language changed to: {}", language);
    }
}

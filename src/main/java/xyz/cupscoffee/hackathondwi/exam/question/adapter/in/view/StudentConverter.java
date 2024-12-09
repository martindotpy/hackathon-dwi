package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

@FacesConverter("studentConverter")
public class StudentConverter implements Converter<StudentDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StudentDto getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return objectMapper.readValue(value, StudentDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, StudentDto value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

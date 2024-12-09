package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.view;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request.CreateAnswerRequest;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.request.UpdateAnswerRequest;
import xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.response.AnswersContentResponse;
import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.CreateAnswerPayload;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.UpdateAnswerPayload;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.request.CreateQuestionRequest;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.request.UpdateQuestionRequest;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response.QuestionContentResponse;
import xyz.cupscoffee.hackathondwi.exam.question.adapter.in.response.QuestionsContentResponse;
import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.UpdateQuestionPayload;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.util.FaceShortcuts;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.view.RestClient;
import xyz.cupscoffee.hackathondwi.student.core.adapter.in.response.StudentsContentResponse;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;

@Slf4j
@ViewScoped
@Named
@RequiredArgsConstructor
public final class QuestionView {
    // Rest client
    private final RestClient restClient;

    @PostConstruct
    public void init() {
        try {
            examId = FaceShortcuts.getQueryParam("exam_id");
            Integer.parseInt(examId);
            courseId = FaceShortcuts.getQueryParam("course_id");
            Long.parseLong(courseId);
            semesterId = FaceShortcuts.getQueryParam("semester_id");
            Long.parseLong(semesterId);
        } catch (NumberFormatException e) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        if (!loadStudents()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        if (!loadQuestions()) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }
    }

    // Return to previous page or home
    @Getter
    @Setter
    private String examId;
    @Getter
    @Setter
    private String courseId;
    @Getter
    @Setter
    private String semesterId;

    public void back() {
        try {
            Integer.parseInt(examId);
            Integer.parseInt(semesterId);
            Integer.parseInt(courseId);
        } catch (NumberFormatException e) {
            FaceShortcuts.redirect("/home.xhtml");
            return;
        }

        FaceShortcuts.redirect(
                "/exam.xhtml?"
                        + "course_id=" + courseId + "&"
                        + "semester_id=" + semesterId);
    }

    // Student view
    @Getter
    @Setter
    private List<StudentDto> students;
    @Getter
    @Setter
    private StudentDto selectedStudent;

    public boolean loadStudents() {
        var response = restClient.get("/course/" + courseId + "/student", StudentsContentResponse.class);

        if (response.getStatus() == 200) {
            students = ((StudentsContentResponse) response.getBody()).getContent();
            if (!students.isEmpty())
                selectedStudent = students.get(0);
            return true;
        }

        return false;
    }

    // Question view
    @Getter
    @Setter
    private List<QuestionAnswer> questionAnswers;

    public boolean loadQuestions() {
        var response = restClient.get("/exam/" + examId + "/question", QuestionsContentResponse.class);

        if (response.getStatus() == 200) {
            var questions = ((QuestionsContentResponse) response.getBody()).getContent();

            questionAnswers = new ArrayList<>(questions.size());

            for (QuestionDto question : questions) {
                questionAnswers.add(new QuestionAnswer(
                        question.getOrder(),
                        question.getMaxValue(),
                        null,
                        question.getId(),
                        null));
            }

            loadAnswers();
            return true;
        }

        return true;
    }

    // Answer view
    public void loadAnswers() {
        if (selectedStudent == null) {
            log.error("Selected Student is null");
            return;
        }

        var response = restClient.get("/exam/" + examId + "/student/" + selectedStudent.getId() + "/answer",
                AnswersContentResponse.class);

        if (response.getStatus() == 200) {
            var answers = ((AnswersContentResponse) response.getBody()).getContent();

            // Clean up previous answers
            for (QuestionAnswer questionAnswer : questionAnswers) {
                questionAnswer.setValue(null);
                questionAnswer.setAnswerId(null);
            }

            for (AnswerDto answer : answers) {
                Long questionId = answer.getQuestion().getId();

                for (QuestionAnswer questionAnswer : questionAnswers) {
                    if (questionAnswer.getQuestionId().equals(questionId)) {
                        questionAnswer.setValue(answer.getValue());
                        questionAnswer.setAnswerId(answer.getId());
                        break;
                    }
                }
            }
            return;
        }

        return;
    }

    // Creation
    @Getter
    @Setter
    private String createQuestionOrder;
    @Getter
    @Setter
    private String createQuestionMaxValue;

    public void create() {
        Integer order;
        Integer maxValue;

        try {
            order = Integer.parseInt(createQuestionOrder);
        } catch (NumberFormatException e) {
            FaceShortcuts.showFailureMessage("question.create.request.order.positive", "Order can only be a number");
            return;
        }

        try {
            maxValue = Integer.parseInt(createQuestionMaxValue);
        } catch (NumberFormatException e) {
            FaceShortcuts.showFailureMessage("question.create.request.max-value.positive",
                    "Max value can only be a number");
            return;
        }

        CreateQuestionRequest request = CreateQuestionRequest.builder()
                .order(order)
                .maxValue(maxValue)
                .examId(Long.parseLong(examId))
                .build();

        var response = restClient.post("/question", request, QuestionContentResponse.class);

        if (response.getStatus() == 200) {
            FaceShortcuts.redirect("/question.xhtml?"
                    + "exam_id=" + examId + "&"
                    + "course_id=" + courseId + "&"
                    + "semester_id=" + semesterId);
            return;
        }
    }

    // Update
    @Getter
    @Setter
    private QuestionAnswer selectedQuestion;
    @Getter
    @Setter
    private String updatedQuestionOrder;
    @Getter
    @Setter
    private String updatedQuestionMaxValue;

    public void prepareUpdate(QuestionAnswer question) {
        this.selectedQuestion = question;
        this.updatedQuestionOrder = question.getOrder().toString();
        this.updatedQuestionMaxValue = question.getMaxValue().toString();
    }

    public void update() {
        if (selectedQuestion == null) {
            log.error("Selected Question is null");
            return;
        }

        Integer order;
        Integer maxValue;

        try {
            order = Integer.parseInt(updatedQuestionOrder);
        } catch (NumberFormatException e) {
            FaceShortcuts.showFailureMessage("question.create.request.order.positive", "Order can only be a number");
            return;
        }

        try {
            maxValue = Integer.parseInt(updatedQuestionMaxValue);
        } catch (NumberFormatException e) {
            FaceShortcuts.showFailureMessage("question.create.request.max-value.positive",
                    "Max value can only be a number");
            return;
        }

        UpdateQuestionPayload request = UpdateQuestionRequest.builder()
                .id(selectedQuestion.getQuestionId())
                .order(order)
                .maxValue(maxValue)
                .build();

        var response = restClient.put("/question/" + selectedQuestion.getQuestionId(), request,
                QuestionContentResponse.class);

        if (response.getStatus() == 200) {
            FaceShortcuts.redirect("/question.xhtml?"
                    + "exam_id=" + examId + "&"
                    + "course_id=" + courseId + "&"
                    + "semester_id=" + semesterId);
        }
    }

    // Delete
    public void delete(Long questionId) {
        var response = restClient.delete("/question/" + questionId);

        if (response.getStatus() == 200) {
            FaceShortcuts.redirect("/question.xhtml?"
                    + "exam_id=" + examId + "&"
                    + "course_id=" + courseId + "&"
                    + "semester_id=" + semesterId);
        }
    }

    // Add answer
    public void saveAnswer(QuestionAnswer questionAnswer) {
        log.info("Updating answer for question {}", questionAnswer.getQuestionId());

        if (questionAnswer.getAnswerId() == null) {
            createAnswer(questionAnswer);
        } else {
            updateAnswer(questionAnswer);
        }
    }

    public void createAnswer(QuestionAnswer questionAnswer) {
        CreateAnswerPayload request = CreateAnswerRequest.builder()
                .value(questionAnswer.getValue())
                .questionId((long) questionAnswer.getQuestionId())
                .studentId(selectedStudent.getId())
                .build();

        var response = restClient.post("/answer", request, AnswersContentResponse.class);

        if (response.getStatus() == 200) {
            questionAnswer.setAnswerId(((AnswerDto) response.getBody()).getId());
        }
    }

    public void updateAnswer(QuestionAnswer questionAnswer) {
        UpdateAnswerPayload request = UpdateAnswerRequest.builder()
                .id(questionAnswer.getAnswerId())
                .value(questionAnswer.getValue())
                .build();

        var response = restClient.put("/answer/" + questionAnswer.getAnswerId(), request, AnswersContentResponse.class);

        if (response.getStatus() == 200) {
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class QuestionAnswer {
        private Integer order;
        private Integer maxValue;
        private Integer value;
        private Long questionId;
        private Long answerId;
    }
}

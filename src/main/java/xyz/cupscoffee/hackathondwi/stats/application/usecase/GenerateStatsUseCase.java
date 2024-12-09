package xyz.cupscoffee.hackathondwi.stats.application.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.auth.application.port.out.GetAuthenticatedUserPort;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.repository.AnswerRepository;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.repository.QuestionRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.UseCase;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.FilterOperator;
import xyz.cupscoffee.hackathondwi.stats.application.dto.StatsDto;
import xyz.cupscoffee.hackathondwi.stats.application.dto.StatsDto.ThreePositions;
import xyz.cupscoffee.hackathondwi.stats.application.port.in.GenerateStatsPort;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.application.mapper.StudentMapper;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class GenerateStatsUseCase implements GenerateStatsPort {
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final StudentMapper studentMapper;
    private final AnswerRepository answerRepository;
    private final ExamRepository examRepository;
    private final GetAuthenticatedUserPort getAuthenticatedUserPort;

    @Override
    public Optional<StatsDto> generateStats(Long examId) {
        var examResult = examRepository.matchOne(
                Criteria.ofMatchOne(
                        Filter.of("id", FilterOperator.EQUAL, examId),
                        Filter.of("course.semester.user.id", FilterOperator.EQUAL,
                                getAuthenticatedUserPort.get().get().getId())));

        if (examResult.isFailure()) {
            log.error("Exam with id {} not found", examId);

            return Optional.empty();
        }

        Exam exam = examResult.getSuccess();
        Map<Student, List<Answer>> studentAnswers = new HashMap<>();
        Long courseId = exam.getCourse().getId();
        List<Student> students = studentRepository.findAllByCoursesId(courseId);
        List<Question> questions = questionRepository.findAllByExamId(examId);

        for (Student student : students) {
            var answers = answerRepository.findAllByExamIdAndStudentId(examId, student.getId());

            studentAnswers.put(student, answers);
        }

        int passed = calculatePassed(studentAnswers);
        int failed = calculateFailed(studentAnswers);
        Map<Integer, Integer> scores = calculateScores(studentAnswers);
        ThreePositions threePositions = calculateThreePositions(studentAnswers);
        Map<Integer, Map<Integer, Integer>> scoresByQuestion = calculateScoresByQuestion(studentAnswers, questions);

        return Optional.of(StatsDto.builder()
                .passed(passed)
                .failed(failed)
                .scores(scores)
                .threePositions(threePositions)
                .scoresByQuestion(scoresByQuestion)
                .build());
    }

    private int calculatePassed(Map<Student, List<Answer>> studentAnswers) {
        final int minValueToPass = 12;
        int passed = 0;

        for (Student student : studentAnswers.keySet()) {
            int score = 0;

            for (Answer answer : studentAnswers.get(student)) {
                score += answer.getValue();
            }

            if (minValueToPass <= score) {
                passed++;
            }
        }

        return passed;
    }

    private int calculateFailed(Map<Student, List<Answer>> studentAnswers) {
        final int minValueToPass = 12;
        int failed = 0;

        for (Student student : studentAnswers.keySet()) {
            int score = 0;

            for (Answer answer : studentAnswers.get(student)) {
                score += answer.getValue();
            }

            if (minValueToPass > score) {
                failed++;
            }
        }

        return failed;
    }

    private Map<Integer, Integer> calculateScores(Map<Student, List<Answer>> studentAnswers) {
        var scores = new HashMap<Integer, Integer>();

        for (Student student : studentAnswers.keySet()) {
            int score = 0;

            for (Answer answer : studentAnswers.get(student)) {
                score += answer.getValue();
            }

            if (scores.containsKey(score)) {
                scores.put(score, scores.get(score) + 1);
            } else {
                scores.put(score, 1);
            }
        }

        return scores;
    }

    private ThreePositions calculateThreePositions(Map<Student, List<Answer>> studentAnswers) {
        Map<Student, Integer> scores = new HashMap<>();

        for (Student student : studentAnswers.keySet()) {
            int score = 0;

            for (Answer answer : studentAnswers.get(student)) {
                score += answer.getValue();
            }

            scores.put(student, score);
        }

        var sortedScores = scores.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();

        if (sortedScores.isEmpty()) {
            return ThreePositions.builder().build();
        }

        // First place
        int firstPositionScore = sortedScores.get(sortedScores.size() - 1).getValue();
        List<StudentDto> firstPositionCodes = sortedScores.stream()
                .filter(entry -> entry.getValue() == firstPositionScore)
                .map(Map.Entry::getKey)
                .map(studentMapper::toDto)
                .toList();

        sortedScores = sortedScores.stream()
                .filter(entry -> entry.getValue() != firstPositionScore)
                .toList();

        if (sortedScores.isEmpty()) {
            return ThreePositions.builder()
                    .firstPositionCodes(firstPositionCodes)
                    .build();
        }

        // Second place
        int secondPositionScore = sortedScores.get(sortedScores.size() - 1).getValue();
        List<StudentDto> secondPositionCodes = sortedScores.stream()
                .filter(entry -> entry.getValue() == secondPositionScore)
                .map(Map.Entry::getKey)
                .map(studentMapper::toDto)
                .toList();
        sortedScores = sortedScores.stream()
                .filter(entry -> entry.getValue() != secondPositionScore)
                .toList();

        if (sortedScores.isEmpty()) {
            return ThreePositions.builder()
                    .firstPositionCodes(firstPositionCodes)
                    .secondPositionCodes(secondPositionCodes)
                    .build();
        }

        // Third place
        int thirdPositionScore = sortedScores.get(sortedScores.size() - 1).getValue();
        List<StudentDto> thirdPositionCodes = sortedScores.stream()
                .filter(entry -> entry.getValue() == thirdPositionScore)
                .map(Map.Entry::getKey)
                .map(studentMapper::toDto)
                .toList();

        return ThreePositions.builder()
                .firstPositionCodes(firstPositionCodes)
                .secondPositionCodes(secondPositionCodes)
                .thirdPositionCodes(thirdPositionCodes)
                .build();

    }

    private Map<Integer, Map<Integer, Integer>> calculateScoresByQuestion(Map<Student, List<Answer>> studentAnswers,
            List<Question> questions) {
        // Load all the order of the questions
        Map<Integer, Map<Integer, Integer>> scoresByQuestion = new HashMap<>();

        for (Question question : questions) {
            scoresByQuestion.put(question.getOrder(), new HashMap<>());
        }

        for (Student student : studentAnswers.keySet()) {
            for (Answer answer : studentAnswers.get(student)) {
                int questionOrder = answer.getQuestion().getOrder();
                int score = answer.getValue();

                if (scoresByQuestion.get(questionOrder).containsKey(score)) {
                    scoresByQuestion.get(questionOrder).put(score, scoresByQuestion.get(questionOrder).get(score) + 1);
                } else {
                    scoresByQuestion.get(questionOrder).put(score, 1);
                }
            }
        }

        return scoresByQuestion;
    }
}

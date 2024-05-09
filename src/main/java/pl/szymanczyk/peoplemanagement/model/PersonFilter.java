package pl.szymanczyk.peoplemanagement.model;


import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import pl.szymanczyk.peoplemanagement.model.student.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Slf4j
public class PersonFilter {

    private String mail;
    private String personalId;
    private String firstName;
    private String lastName;
    private Integer minHeight;
    private Integer maxHeight;
    private Integer minWeight;
    private Integer maxWeight;
    private Boolean blocked;
    private LocalDate startDateOfEmployment;
    private String position;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String academyName;
    private Integer studyYear;
    private String studyField;
    private BigDecimal minScholarship;
    private BigDecimal maxScholarship;
    private Integer pensionerMinPension;
    private Integer pensionerMaxPension;
    private Integer pensionerMinEmploymentYears;
    private Integer pensionerMaxEmploymentYears;

    public Specification<Person> buildSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (mail != null && !mail.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("mail"), mail));
            }
            if (personalId != null && !personalId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("personalId"), personalId));
            }
            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("firstName"), firstName));
            }
            if (lastName != null && !lastName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), lastName));
            }
            if (minHeight != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("height"), minHeight));
            }
            if (maxHeight != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("height"), maxHeight));
            }
            if (minWeight != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), minWeight));
            }
            if (maxWeight != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weight"), maxWeight));
            }
            if (startDateOfEmployment != null) {
                predicates.add(criteriaBuilder.equal(root.get("startDateOfEmployment"), startDateOfEmployment));
            }
            if (position != null && !position.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("position"), position));
            }
            if (minSalary != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }
            if (maxSalary != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }
            if (academyName != null && !academyName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("academyName")), "%" + academyName.toLowerCase() + "%"));
            }
            if (studyYear != null) {
                predicates.add(criteriaBuilder.equal(root.get("studyYear"), studyYear));
            }
            if (studyField != null && !studyField.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("studyField")), "%" + studyField.toLowerCase() + "%"));
            }
            if (minScholarship != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scholarship"), minScholarship));
            }
            if (maxScholarship != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scholarship"), maxScholarship));
            }
            if (pensionerMinPension != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pension"), pensionerMinPension));
            }
            if (pensionerMaxPension != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pension"), pensionerMaxPension));
            }
            if (pensionerMinEmploymentYears != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("employmentYears"), pensionerMinEmploymentYears));
            }
            if (pensionerMaxEmploymentYears != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("employmentYears"), pensionerMaxEmploymentYears));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    public boolean isValidField() {
        return mail != null || personalId != null || firstName != null || lastName != null ||
                minHeight != null || maxHeight != null || minWeight != null || maxWeight != null ||
                startDateOfEmployment != null || position != null || minSalary != null || maxSalary != null ||
                academyName != null || studyYear != null || studyField != null ||
                minScholarship != null || maxScholarship != null ||
                pensionerMinPension != null || pensionerMaxPension != null ||
                pensionerMinEmploymentYears != null || pensionerMaxEmploymentYears != null;
    }
}

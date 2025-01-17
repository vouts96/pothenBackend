package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Submission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]{9}$")
    private String afm;

    @NotNull
    private String adt;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private String fatherName;

    @NotNull
    private LocalDate acquisitionDate;

    private LocalDate lossDate;

    @NotNull
    private String organizationUnit;

    private String newOrganizationUnit;

    @NotNull
    private String protocolNumber;

    @NotNull
    private LocalDate decisionDate;

    @NotNull
    private Boolean previousSubmission;

    private PositionDTO position;

    private GradeDTO grade;

    private CommitteeDTO committeeName;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAdt() {
        return adt;
    }

    public void setAdt(String adt) {
        this.adt = adt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public LocalDate getLossDate() {
        return lossDate;
    }

    public void setLossDate(LocalDate lossDate) {
        this.lossDate = lossDate;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getNewOrganizationUnit() {
        return newOrganizationUnit;
    }

    public void setNewOrganizationUnit(String newOrganizationUnit) {
        this.newOrganizationUnit = newOrganizationUnit;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public LocalDate getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Boolean getPreviousSubmission() {
        return previousSubmission;
    }

    public void setPreviousSubmission(Boolean previousSubmission) {
        this.previousSubmission = previousSubmission;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    public GradeDTO getGrade() {
        return grade;
    }

    public void setGrade(GradeDTO grade) {
        this.grade = grade;
    }

    public CommitteeDTO getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(CommitteeDTO committeeName) {
        this.committeeName = committeeName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionDTO)) {
            return false;
        }

        SubmissionDTO submissionDTO = (SubmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + getId() +
            ", afm='" + getAfm() + "'" +
            ", adt='" + getAdt() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", acquisitionDate='" + getAcquisitionDate() + "'" +
            ", lossDate='" + getLossDate() + "'" +
            ", organizationUnit='" + getOrganizationUnit() + "'" +
            ", newOrganizationUnit='" + getNewOrganizationUnit() + "'" +
            ", protocolNumber='" + getProtocolNumber() + "'" +
            ", decisionDate='" + getDecisionDate() + "'" +
            ", previousSubmission='" + getPreviousSubmission() + "'" +
            ", position=" + getPosition() +
            ", grade=" + getGrade() +
            ", committeeName=" + getCommitteeName() +
            ", user=" + getUser() +
            "}";
    }
}

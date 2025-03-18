package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.SubmissionAudit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionAuditDTO implements Serializable {

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

    @NotNull
    private Instant modifiedDate;

    @NotNull
    private String modifiedBy;

    @NotNull
    private String changeType;

    private SubmissionDTO originalSubmission;

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

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public SubmissionDTO getOriginalSubmission() {
        return originalSubmission;
    }

    public void setOriginalSubmission(SubmissionDTO originalSubmission) {
        this.originalSubmission = originalSubmission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionAuditDTO)) {
            return false;
        }

        SubmissionAuditDTO submissionAuditDTO = (SubmissionAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionAuditDTO{" +
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
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", changeType='" + getChangeType() + "'" +
            ", originalSubmission=" + getOriginalSubmission() +
            "}";
    }
}

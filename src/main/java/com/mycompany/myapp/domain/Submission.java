package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^[0-9]{9}$")
    @Column(name = "afm", nullable = false)
    private String afm;

    @NotNull
    @Column(name = "adt", nullable = false)
    private String adt;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "father_name", nullable = false)
    private String fatherName;

    @NotNull
    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "loss_date")
    private LocalDate lossDate;

    @NotNull
    @Column(name = "organization_unit", nullable = false)
    private String organizationUnit;

    @Column(name = "new_organization_unit")
    private String newOrganizationUnit;

    @NotNull
    @Column(name = "protocol_number", nullable = false)
    private String protocolNumber;

    @NotNull
    @Column(name = "decision_date", nullable = false)
    private LocalDate decisionDate;

    @NotNull
    @Column(name = "previous_submission", nullable = false)
    private Boolean previousSubmission;

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Committee committeeName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Submission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAfm() {
        return this.afm;
    }

    public Submission afm(String afm) {
        this.setAfm(afm);
        return this;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAdt() {
        return this.adt;
    }

    public Submission adt(String adt) {
        this.setAdt(adt);
        return this;
    }

    public void setAdt(String adt) {
        this.adt = adt;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Submission lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Submission firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public Submission fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public LocalDate getAcquisitionDate() {
        return this.acquisitionDate;
    }

    public Submission acquisitionDate(LocalDate acquisitionDate) {
        this.setAcquisitionDate(acquisitionDate);
        return this;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public LocalDate getLossDate() {
        return this.lossDate;
    }

    public Submission lossDate(LocalDate lossDate) {
        this.setLossDate(lossDate);
        return this;
    }

    public void setLossDate(LocalDate lossDate) {
        this.lossDate = lossDate;
    }

    public String getOrganizationUnit() {
        return this.organizationUnit;
    }

    public Submission organizationUnit(String organizationUnit) {
        this.setOrganizationUnit(organizationUnit);
        return this;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getNewOrganizationUnit() {
        return this.newOrganizationUnit;
    }

    public Submission newOrganizationUnit(String newOrganizationUnit) {
        this.setNewOrganizationUnit(newOrganizationUnit);
        return this;
    }

    public void setNewOrganizationUnit(String newOrganizationUnit) {
        this.newOrganizationUnit = newOrganizationUnit;
    }

    public String getProtocolNumber() {
        return this.protocolNumber;
    }

    public Submission protocolNumber(String protocolNumber) {
        this.setProtocolNumber(protocolNumber);
        return this;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public LocalDate getDecisionDate() {
        return this.decisionDate;
    }

    public Submission decisionDate(LocalDate decisionDate) {
        this.setDecisionDate(decisionDate);
        return this;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Boolean getPreviousSubmission() {
        return this.previousSubmission;
    }

    public Submission previousSubmission(Boolean previousSubmission) {
        this.setPreviousSubmission(previousSubmission);
        return this;
    }

    public void setPreviousSubmission(Boolean previousSubmission) {
        this.previousSubmission = previousSubmission;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Submission position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Grade getGrade() {
        return this.grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Submission grade(Grade grade) {
        this.setGrade(grade);
        return this;
    }

    public Committee getCommitteeName() {
        return this.committeeName;
    }

    public void setCommitteeName(Committee committee) {
        this.committeeName = committee;
    }

    public Submission committeeName(Committee committee) {
        this.setCommitteeName(committee);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Submission user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission)) {
            return false;
        }
        return getId() != null && getId().equals(((Submission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Submission{" +
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
            "}";
    }
}

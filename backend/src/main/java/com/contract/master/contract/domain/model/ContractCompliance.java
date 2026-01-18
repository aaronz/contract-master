package com.contract.master.contract.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ContractCompliance {
    @Column(name = "legal_review_flag")
    private Boolean legalReviewFlag;

    @Column(name = "legal_review_opinion", columnDefinition = "TEXT")
    private String legalReviewOpinion;

    @Column(name = "dispute_resolution", length = 64)
    private String disputeResolution;

    @Column(name = "governing_law", length = 64)
    private String governingLaw;

    protected ContractCompliance() {}

    public ContractCompliance(Boolean legalReviewFlag, String legalReviewOpinion, String disputeResolution, String governingLaw) {
        this.legalReviewFlag = legalReviewFlag;
        this.legalReviewOpinion = legalReviewOpinion;
        this.disputeResolution = disputeResolution;
        this.governingLaw = governingLaw;
    }

    public Boolean getLegalReviewFlag() { return legalReviewFlag; }
    public void setLegalReviewFlag(Boolean legalReviewFlag) { this.legalReviewFlag = legalReviewFlag; }
    public String getLegalReviewOpinion() { return legalReviewOpinion; }
    public void setLegalReviewOpinion(String legalReviewOpinion) { this.legalReviewOpinion = legalReviewOpinion; }
    public String getDisputeResolution() { return disputeResolution; }
    public void setDisputeResolution(String disputeResolution) { this.disputeResolution = disputeResolution; }
    public String getGoverningLaw() { return governingLaw; }
    public void setGoverningLaw(String governingLaw) { this.governingLaw = governingLaw; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractCompliance that = (ContractCompliance) o;
        return Objects.equals(legalReviewFlag, that.legalReviewFlag) && Objects.equals(legalReviewOpinion, that.legalReviewOpinion) && Objects.equals(disputeResolution, that.disputeResolution) && Objects.equals(governingLaw, that.governingLaw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legalReviewFlag, legalReviewOpinion, disputeResolution, governingLaw);
    }
}

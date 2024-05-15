package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class ParentAllocationParams {
    private String enfantResidence;
    private boolean parent1ActiviteLucrative;
    private String parent1Residence;
    private boolean parent2ActiviteLucrative;
    private String parent2Residence;
    private boolean parentsEnsemble;
    private boolean parent1AutoriteParentale;
    private boolean parent2AutoriteParentale;
    private BigDecimal parent1Salaire;
    private BigDecimal parent2Salaire;
    private String parent1WorkCanton;
    private String parent2WorkCanton;

    // Constructors
    public ParentAllocationParams() {
    }

    public ParentAllocationParams(
            String enfantResidence,
            boolean parent1ActiviteLucrative,
            String parent1Residence,
            boolean parent2ActiviteLucrative,
            String parent2Residence,
            boolean parentsEnsemble,
            boolean parent1AutoriteParentale,
            boolean parent2AutoriteParentale,
            BigDecimal parent1Salaire,
            BigDecimal parent2Salaire,
            String parent1WorkCanton,
            String parent2WorkCanton) {
        this.enfantResidence = enfantResidence;
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
        this.parent1Residence = parent1Residence;
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
        this.parent2Residence = parent2Residence;
        this.parentsEnsemble = parentsEnsemble;
        this.parent1AutoriteParentale = parent1AutoriteParentale;
        this.parent2AutoriteParentale = parent2AutoriteParentale;
        this.parent1Salaire = parent1Salaire;
        this.parent2Salaire = parent2Salaire;
        this.parent1WorkCanton = parent1WorkCanton;
        this.parent2WorkCanton = parent2WorkCanton;
    }

    public String getEnfantResidence() {
        return enfantResidence;
    }

    public void setEnfantResidence(String enfantResidence) {
        this.enfantResidence = enfantResidence;
    }

    public boolean isParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public void setParent1ActiviteLucrative(boolean parent1ActiviteLucrative) {
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
    }

    public String getParent1Residence() {
        return parent1Residence;
    }

    public void setParent1Residence(String parent1Residence) {
        this.parent1Residence = parent1Residence;
    }

    public boolean isParent2ActiviteLucrative() {
        return parent2ActiviteLucrative;
    }

    public void setParent2ActiviteLucrative(boolean parent2ActiviteLucrative) {
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
    }

    public String getParent2Residence() {
        return parent2Residence;
    }

    public void setParent2Residence(String parent2Residence) {
        this.parent2Residence = parent2Residence;
    }

    public boolean isParentsEnsemble() {
        return parentsEnsemble;
    }

    public void setParentsEnsemble(boolean parentsEnsemble) {
        this.parentsEnsemble = parentsEnsemble;
    }

    public boolean isParent1AutoriteParentale() {
        return parent1AutoriteParentale;
    }

    public void setParent1AutoriteParentale(boolean parent1AutoriteParentale) {
        this.parent1AutoriteParentale = parent1AutoriteParentale;
    }

    public boolean isParent2AutoriteParentale() {
        return parent2AutoriteParentale;
    }

    public void setParent2AutoriteParentale(boolean parent2AutoriteParentale) {
        this.parent2AutoriteParentale = parent2AutoriteParentale;
    }

    public BigDecimal getParent1Salaire() {
        return parent1Salaire;
    }

    public void setParent1Salaire(BigDecimal parent1Salaire) {
        this.parent1Salaire = parent1Salaire;
    }

    public BigDecimal getParent2Salaire() {
        return parent2Salaire;
    }

    public void setParent2Salaire(BigDecimal parent2Salaire) {
        this.parent2Salaire = parent2Salaire;
    }

    public String getParent1WorkCanton() {
        return parent1WorkCanton;
    }

    public void setParent1WorkCanton(String parent1WorkCanton) {
        this.parent1WorkCanton = parent1WorkCanton;
    }

    public String getParent2WorkCanton() {
        return parent2WorkCanton;
    }

    public void setParent2WorkCanton(String parent2WorkCanton) {
        this.parent2WorkCanton = parent2WorkCanton;
    }


    // Getters and setters (implement all getters and setters here)

    // Additional logic to handle specific rules in the schema
    public BigDecimal determineHighestAVSIncome() {
        if (parent1Salaire.compareTo(parent2Salaire) >= 0) {
            return parent1Salaire;
        } else {
            return parent2Salaire;
        }
    }
}

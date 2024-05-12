package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class ParentAllocationPartie4 {
    private String enfantResidence;
    private boolean parent1ActiviteLucrative;
    private boolean parent1Independant;
    private String parent1Residence;
    private boolean parent2ActiviteLucrative;
    private boolean parent2Independant;
    private String parent2Residence;
    private boolean parentsEnsemble;
    private BigDecimal parent1Salaire;
    private BigDecimal parent2Salaire;

    // Constructors
    public ParentAllocationPartie4() {
    }

    public ParentAllocationPartie4(
            String enfantResidence,
            boolean parent1ActiviteLucrative,
            boolean parent1Independant,
            String parent1Residence,
            boolean parent2ActiviteLucrative,
            boolean parent2Independant,
            String parent2Residence,
            boolean parentsEnsemble,
            BigDecimal parent1Salaire,
            BigDecimal parent2Salaire) {
        this.enfantResidence = enfantResidence;
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
        this.parent1Independant = parent1Independant;
        this.parent1Residence = parent1Residence;
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
        this.parent2Independant = parent2Independant;
        this.parent2Residence = parent2Residence;
        this.parentsEnsemble = parentsEnsemble;
        this.parent1Salaire = parent1Salaire;
        this.parent2Salaire = parent2Salaire;
    }

    // Getters and Setters
    public String getEnfantResidence() {
        return enfantResidence;
    }

    public boolean isParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public boolean isParent1Independant() {
        return parent1Independant;
    }

    public String getParent1Residence() {
        return parent1Residence;
    }

    public boolean isParent2ActiviteLucrative() {
        return parent2ActiviteLucrative;
    }

    public boolean isParent2Independant() {
        return parent2Independant;
    }

    public String getParent2Residence() {
        return parent2Residence;
    }

    public boolean isParentsEnsemble() {
        return parentsEnsemble;
    }

    public BigDecimal getParent1Salaire() {
        return parent1Salaire;
    }

    public BigDecimal getParent2Salaire() {
        return parent2Salaire;
    }
}

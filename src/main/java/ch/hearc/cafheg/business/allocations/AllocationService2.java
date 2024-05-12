package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.math.BigDecimal;
import java.util.List;

public class AllocationService2 {
  private static final String PARENT_1 = "Parent 1";
  private static final String PARENT_2 = "Parent 2";
  private static final String NO_RIGHT = "Aucun droit";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService2(
          AllocataireMapper allocataireMapper,
          AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public AllocationService2() {
    this.allocataireMapper = new AllocataireMapper();
    this.allocationMapper = new AllocationMapper();
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    System.out.println("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentAllocationPartie4 params) {
    // Logique basée sur le diagramme
    if (params.isParent1ActiviteLucrative() ^ params.isParent2ActiviteLucrative()) {
      // Un seul parent avec activité lucrative
      if (params.isParent1ActiviteLucrative()) {
        return PARENT_1;
      } else {
        return PARENT_2;
      }
    } else if (params.isParent1ActiviteLucrative() && params.isParent2ActiviteLucrative()) {
      // Deux parents avec activité lucrative
      if (params.isParentsEnsemble()) {
        // Les parents vivent ensemble
        if (!params.isParent1Independant() && !params.isParent2Independant()) {
          // Les deux parents sont salariés
          return params.getParent1Salaire().compareTo(params.getParent2Salaire()) > 0 ? PARENT_1 : PARENT_2;
        } else if (params.isParent1Independant() && params.isParent2Independant()) {
          // Les deux parents sont indépendants
          return params.getParent1Salaire().compareTo(params.getParent2Salaire()) > 0 ? PARENT_1 : PARENT_2;
        } else {
          // Un parent est salarié et l'autre indépendant
          return params.isParent1Independant() ? PARENT_2 : PARENT_1;
        }
      } else {
        // Les parents vivent séparés
        if (params.getParent1Residence().equals(params.getEnfantResidence())) {
          return PARENT_1;
        } else if (params.getParent2Residence().equals(params.getEnfantResidence())) {
          return PARENT_2;
        } else {
          return NO_RIGHT; // Situation hypothétique où aucun parent ne vit avec l'enfant
        }
      }
    } else {
      return NO_RIGHT; // Aucun parent avec activité lucrative
    }
  }
}
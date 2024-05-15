package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllocationService {

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  public AllocationService(AllocataireMapper allocataireMapper, AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public List<Allocataire> findAllAllocataires(String nom) {
    return allocataireMapper.findAll(nom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentAllocationParams params) {
    boolean p1Active = params.isParent1ActiviteLucrative();
    boolean p2Active = params.isParent2ActiviteLucrative();
    boolean p1Authority = params.isParent1AutoriteParentale();
    boolean p2Authority = params.isParent2AutoriteParentale();
    BigDecimal p1Salary = params.getParent1Salaire();
    BigDecimal p2Salary = params.getParent2Salaire();
    String p1WorkCanton = params.getParent1WorkCanton();
    String p2WorkCanton = params.getParent2WorkCanton();
    String enfantResidence = params.getEnfantResidence();

    if (!p1Active && !p2Active) {
      return "PARENT_1_Et_PARENT_2";
    }

    if (p1Active && !p2Active) {
      return "PARENT_1";
    } else if (!p1Active && p2Active) {
      return "PARENT_2";
    }

    if (params.isParentsEnsemble()) {
      if (p1Salary.compareTo(p2Salary) >= 0) {
        return p1WorkCanton.equals(enfantResidence) ? "PARENT_1" : "PARENT_2";
      } else {
        return p2WorkCanton.equals(enfantResidence) ? "PARENT_2" : "PARENT_1";
      }
    } else {
      if (p1Authority && !p2Authority) {
        return "PARENT_1";
      } else if (!p1Authority && p2Authority) {
        return "PARENT_2";
      }
    }

    if (params.getParent1Residence().equals(params.getEnfantResidence())) {
      return "PARENT_1";
    } else if (params.getParent2Residence().equals(params.getEnfantResidence())) {
      return "PARENT_2";
    }

    return "PARENT_1_Et_PARENT_2";  // Default case, could be adjusted depending on additional rules
  }
}

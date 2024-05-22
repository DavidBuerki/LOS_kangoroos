package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AllocationService {

  private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  public AllocationService(AllocataireMapper allocataireMapper, AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public List<Allocataire> findAllAllocataires(String nom) {
    logger.info("Fetching all allocataires with name filter: {}", nom);
    return allocataireMapper.findAll(nom);
  }

  public List<Allocation> findAllocationsActuelles() {
    logger.info("Fetching all current allocations");
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(ParentAllocationParams params) {
    logger.debug("Determining allocation rights for params: {}", params);
    boolean p1Active = params.isParent1ActiviteLucrative();
    boolean p2Active = params.isParent2ActiviteLucrative();
    boolean p1Authority = params.isParent1AutoriteParentale();
    boolean p2Authority = params.isParent2AutoriteParentale();
    BigDecimal p1Salary = params.getParent1Salaire();
    BigDecimal p2Salary = params.getParent2Salaire();
    String p1WorkCanton = params.getParent1WorkCanton();
    String p2WorkCanton = params.getParent2WorkCanton();
    String enfantResidence = params.getEnfantResidence();

    String result = "PARENT_1_Et_PARENT_2"; // Default case, could be adjusted depending on additional rules
    if (!p1Active && !p2Active) {
      result = "PARENT_1_Et_PARENT_2";
    } else if (p1Active && !p2Active) {
      result = "PARENT_1";
    } else if (!p1Active && p2Active) {
      result = "PARENT_2";
    } else if (params.isParentsEnsemble()) {
      if (p1Salary.compareTo(p2Salary) >= 0) {
        result = p1WorkCanton.equals(enfantResidence) ? "PARENT_1" : "PARENT_2";
      } else {
        result = p2WorkCanton.equals(enfantResidence) ? "PARENT_2" : "PARENT_1";
      }
    } else if (p1Authority && !p2Authority) {
      result = "PARENT_1";
    } else if (!p1Authority && p2Authority) {
      result = "PARENT_2";
    } else if (params.getParent1Residence().equals(params.getEnfantResidence())) {
      result = "PARENT_1";
    } else if (params.getParent2Residence().equals(params.getEnfantResidence())) {
      result = "PARENT_2";
    }

    logger.info("Allocation right determined as: {}", result);
    return result;
  }
}

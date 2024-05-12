package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.util.List;

public class AllocationService {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public AllocationService() {
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

  public String getParentDroitAllocation(ParentAllocationParams params) {
    System.out.println("Déterminer quel parent a le droit aux allocations");

    boolean p1AL = params.isParent1ActiviteLucrative();
    boolean p2AL = params.isParent2ActiviteLucrative();
    BigDecimal salaireP1 = params.getParent1Salaire();
    BigDecimal salaireP2 = params.getParent2Salaire();

    if (p1AL && !p2AL) {
      return PARENT_1;
    }

    if (p2AL && !p1AL) {
      return PARENT_2;
    }

    return salaireP1.compareTo(salaireP2) > 0 ? PARENT_1 : PARENT_2;
  }
}

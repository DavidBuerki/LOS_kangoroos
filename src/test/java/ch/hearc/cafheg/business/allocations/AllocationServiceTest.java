package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }

  @Test
  public void getParentDroitAllocation_Given_ActiveLucrativeSingleParent_shouldBe_Parent1() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            true, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            false, // parent2ActiviteLucrative
            null, // parent2Residence
            false, // parentsEnsemble
            true, // parent1AutoriteParentale
            true, // parent2AutoriteParentale
            BigDecimal.valueOf(5000), // parent1Salaire
            BigDecimal.ZERO, // parent2Salaire
            "CantonX", // parent1WorkCanton
            null  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_1", resultat);
  }

  @Test
  public void getParentDroitAllocation_Given_TwoParentsActiveAndOnlyParent1Autority_shouldBe_Parent1() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            true, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            false, // parent2ActiviteLucrative
            null, // parent2Residence
            false, // parentsEnsemble
            true, // parent1AutoriteParentale
            false, // parent2AutoriteParentale
            BigDecimal.valueOf(5000), // parent1Salaire
            BigDecimal.valueOf(6000), // parent2Salaire
            "CantonX", // parent1WorkCanton
            "CantonY"  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_1", resultat);
  }

  @Test
  public void getParentDroitAllocation_Given_TwoParentsActive_shouldBe_ParentResidingWithChild() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            true, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            true, // parent2ActiviteLucrative
            "CantonY", // parent2Residence
            false, // parentsEnsemble
            true, // parent1AutoriteParentale
            true, // parent2AutoriteParentale
            BigDecimal.valueOf(6000), // parent1Salaire
            BigDecimal.valueOf(4000), // parent2Salaire
            "CantonX", // parent1WorkCanton
            "CantonY"  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_1", resultat);
  }

  @Test
  public void getParentDroitAllocation_Given_TwoSalariedParentsLivingTogetherInSameCanton_shouldBe_HighestEarning() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            true, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            true, // parent2ActiviteLucrative
            "CantonX", // parent2Residence
            true, // parentsEnsemble
            true, // parent1AutoriteParentale
            true, // parent2AutoriteParentale
            BigDecimal.valueOf(8000), // parent1Salaire
            BigDecimal.valueOf(10000), // parent2Salaire
            "CantonX", // parent1WorkCanton
            "CantonX"  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_2", resultat);
  }
  @Test
  public void getParentDroitAllocation_Given_TwoSalariedParentsLivingTogetherNotInTheSameCanton_shouldBe_ParentWorkCantonSameThatEnfantResidence() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            true, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            true, // parent2ActiviteLucrative
            "CantonX", // parent2Residence
            true, // parentsEnsemble
            true, // parent1AutoriteParentale
            true, // parent2AutoriteParentale
            BigDecimal.valueOf(8000), // parent1Salaire
            BigDecimal.valueOf(10000), // parent2Salaire
            "CantonX", // parent1WorkCanton
            "CantonY"  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_1", resultat);
  }

  @Test
  public void getParentDroitAllocation_Given_IndependentParentsLivingTogether_shouldBe_BothParents() {
    ParentAllocationParams params = new ParentAllocationParams(
            "CantonX", // enfantResidence
            false, // parent1ActiviteLucrative
            "CantonX", // parent1Residence
            false, // parent2ActiviteLucrative
            "CantonX", // parent2Residence
            true, // parentsEnsemble
            true, // parent1AutoriteParentale
            true, // parent2AutoriteParentale
            BigDecimal.valueOf(5000), // parent1Salaire
            BigDecimal.valueOf(5000), // parent2Salaire
            null, // parent1WorkCanton
            null  // parent2WorkCanton
    );
    String resultat = allocationService.getParentDroitAllocation(params);
    assertEquals("PARENT_1_Et_PARENT_2", resultat);
  }





}
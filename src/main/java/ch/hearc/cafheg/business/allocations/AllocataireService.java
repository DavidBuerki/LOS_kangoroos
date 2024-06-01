package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

public class AllocataireService {

    private static final Logger logger = LoggerFactory.getLogger(AllocataireService.class);

    private AllocataireMapper allocataireMapper;
    private VersementMapper versementMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.versementMapper = versementMapper;
    }

    public AllocataireMapper getAllocataireMapper() {
        return allocataireMapper;
    }

    public void setAllocataireMapper(AllocataireMapper allocataireMapper) {
        this.allocataireMapper = allocataireMapper;
    }

    public VersementMapper getVersementMapper() {
        return versementMapper;
    }

    public void setVersementMapper(VersementMapper versementMapper) {
        this.versementMapper = versementMapper;
    }

    public String deleteAllocataireIfNoVersements(int id) {
        logger.debug("Attempting to delete allocataire with ID: {}", id);
        if (!this.versementMapper.findVersementByParentId(id)) {
            this.allocataireMapper.delete(id);
            logger.info("Allocataire deleted successfully with ID: {}", id);
            return "Allocataire supprimé";
        } else {
            logger.warn("Failed to delete allocataire with ID: {} due to existing versements", id);
            return "Impossible de supprimer l'allocataire, il a des versements";
        }
    }

    public Allocataire findAllocataireById(int id) {
        logger.info("Finding allocataire by ID: {}", id);
        return this.allocataireMapper.findById(id);
    }

    public String updateAllocataire(Allocataire allocataire, int id) {
        logger.info("Updating allocataire with ID: {}", id);
        Allocataire currentAllocataire = this.allocataireMapper.findById(id);
        if (currentAllocataire == null) {
            logger.warn("No allocataire found with ID: {}", id);
            return "Allocataire non trouvé";
        } else {
            boolean isNomSame = allocataire.getNom().equals(currentAllocataire.getNom());
            boolean isPrenomSame = allocataire.getPrenom().equals(currentAllocataire.getPrenom());

            if (isNomSame && isPrenomSame) {
                logger.debug("No changes made to allocataire with ID: {}", id);
                return "Aucune modification apportée";
            } else {
                this.allocataireMapper.updateNomPrenom(id, allocataire.getNom(), allocataire.getPrenom());
                logger.info("Allocataire modified successfully with ID: {}", id);
                return "Allocataire modifié";
            }
        }
    }
}

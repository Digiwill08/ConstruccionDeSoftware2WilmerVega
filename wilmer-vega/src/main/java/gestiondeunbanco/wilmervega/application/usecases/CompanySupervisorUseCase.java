package gestiondeunbanco.wilmervega.application.usecases;

import gestiondeunbanco.wilmervega.domain.models.Transfer;
import gestiondeunbanco.wilmervega.domain.models.TransferStatus;
import gestiondeunbanco.wilmervega.domain.services.ApproveTransferService;
import gestiondeunbanco.wilmervega.domain.services.FindTransfer;
import gestiondeunbanco.wilmervega.domain.services.RejectTransferService;

import java.util.List;

/**
 * Use case for the COMPANY_SUPERVISOR role.
 * Responsible for: approving/rejecting high-value transfers from the company.
 */
public class CompanySupervisorUseCase {

    private final ApproveTransferService approveTransferService;
    private final RejectTransferService rejectTransferService;
    private final FindTransfer findTransfer;

    public CompanySupervisorUseCase(ApproveTransferService approveTransferService,
                                    RejectTransferService rejectTransferService,
                                    FindTransfer findTransfer) {
        this.approveTransferService = approveTransferService;
        this.rejectTransferService = rejectTransferService;
        this.findTransfer = findTransfer;
    }

    public List<Transfer> findAllTransfers() { return findTransfer.findAll(); }

    public Transfer findTransferById(Long id) { return findTransfer.findById(id); }

    public List<Transfer> findPendingTransfers() {
        return findTransfer.findByStatus(TransferStatus.AWAITING_APPROVAL);
    }

    public Transfer approveTransfer(Long transferId, Long supervisorUserId, String role) {
        return approveTransferService.approve(transferId, supervisorUserId, role);
    }

    public Transfer rejectTransfer(Long transferId, Long supervisorUserId, String role, String reason) {
        return rejectTransferService.reject(transferId, supervisorUserId, role, reason);
    }
}

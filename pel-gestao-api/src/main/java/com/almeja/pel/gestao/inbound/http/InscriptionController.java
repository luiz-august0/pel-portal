package com.almeja.pel.gestao.inbound.http;

import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.domain.usecase.inscription.*;
import com.almeja.pel.gestao.core.dto.record.PaymentFormRecord;
import com.almeja.pel.gestao.inbound.http.interfaces.IInscriptionController;
import com.almeja.pel.gestao.infra.context.AuthContext;
import com.almeja.pel.gestao.infra.dto.*;
import com.almeja.pel.gestao.infra.dto.mapper.GradeByQuarterMapper;
import com.almeja.pel.gestao.infra.dto.mapper.InscriptionGroupedByYearMapper;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class InscriptionController implements IInscriptionController {

    private final GetInscriptionsGroupedByYearUC getInscriptionsGroupedByYearUC;
    private final GetPaymentsFormUC getPaymentsFormUC;
    private final GetInscriptionUC getInscriptionUC;
    private final GetDuplicateReceivablesUC getDuplicateReceivablesUC;
    private final DownloadDuplicateReceivableUC downloadDuplicateReceivableUC;
    private final RegisterInscriptionUC registerInscriptionUC;
    private final UpdateContractWithPaymentFormUC updateContractWithPaymentFormUC;
    private final GetInscriptionImageTermUC getInscriptionImageTermUC;
    private final FinalizeInscriptionRegisterUC finalizeInscriptionRegisterUC;
    private final GetDependentInscriptionsGroupedByYearUC getDependentInscriptionsGroupedByYearUC;
    private final CancelInscriptionUC cancelInscriptionUC;
    private final DownloadInscriptionContractPdfUC downloadInscriptionContractPdfUC;
    private final GetLastInscriptionUC getLastInscriptionUC;
    private final GetGradesUC getGradesUC;
    private final GetAttendanceUC getAttendanceUC;
    private final DownloadInscriptionCertificatePdfUC downloadInscriptionCertificatePdfUC;
    private final DownloadDeclarationPdfUC downloadDeclarationPdfUC;
    private final GetInscriptionsActiveToTransferUC getInscriptionsActiveToTransferUC;

    @Override
    public List<InscriptionGroupedByYearDTO> getGroupedByYear() {
        return InscriptionGroupedByYearMapper.toDTO(getInscriptionsGroupedByYearUC.execute(AuthContext.getUser()));
    }

    @Override
    public List<InscriptionDTO> getActiveToTransfer() {
        return ConverterEntityToDTOUtil.convert(getInscriptionsActiveToTransferUC.execute(AuthContext.getUser()), InscriptionDTO.class);
    }

    @Override
    public InscriptionDTO getLastInscription() {
        return ConverterEntityToDTOUtil.convert(getLastInscriptionUC.execute(AuthContext.getUser()), InscriptionDTO.class);
    }

    @Override
    public List<InscriptionGroupedByYearDTO> getDependentInscriptionsGroupedByYear(String dependentCpf) {
        return InscriptionGroupedByYearMapper.toDTO(getDependentInscriptionsGroupedByYearUC.execute(AuthContext.getUser(), dependentCpf));
    }

    @Override
    public List<PaymentFormRecord> getPaymentsForm() {
        return getPaymentsFormUC.execute(AuthContext.getUser());
    }

    @Override
    public List<DuplicateReceivableDTO> getDuplicateReceivables(Integer id) {
        return ConverterEntityToDTOUtil.convert(getDuplicateReceivablesUC.execute(AuthContext.getUser(), id), DuplicateReceivableDTO.class);
    }

    @Override
    public InscriptionDTO getDetails(Integer id) {
        return ConverterEntityToDTOUtil.convert(getInscriptionUC.execute(AuthContext.getUser(), id), InscriptionDTO.class);
    }

    @Override
    public byte[] downloadDuplicateReceivable(Integer id, Integer duplicateReceivableId) {
        return downloadDuplicateReceivableUC.execute(AuthContext.getUser(), duplicateReceivableId);
    }

    @Override
    public Integer register(Integer classId) {
        return registerInscriptionUC.execute(AuthContext.getUser(), classId);
    }

    @Override
    public String updateContractWithPaymentForm(Integer id, EnumInscriptionPayment paymentForm) {
        return updateContractWithPaymentFormUC.execute(AuthContext.getUser(), id, paymentForm);
    }

    @Override
    public String getImageTerm(Integer id) {
        return getInscriptionImageTermUC.execute(id);
    }

    @Override
    public void finalizeRegister(Integer id, EnumInscriptionPayment paymentForm, Boolean acceptContract, Boolean acceptImageAuthorization) {
        finalizeInscriptionRegisterUC.execute(AuthContext.getUser(), id, paymentForm, acceptContract, acceptImageAuthorization);
    }

    @Override
    public void cancel(Integer id) {
        cancelInscriptionUC.execute(AuthContext.getUser(), id);
    }

    @Override
    public byte[] downloadContractPdf(Integer id) {
        return downloadInscriptionContractPdfUC.execute(AuthContext.getUser(), id);
    }

    @Override
    public byte[] downloadCertificatePdf(Integer id) {
        return downloadInscriptionCertificatePdfUC.execute(AuthContext.getUser(), id);
    }

    @Override
    public byte[] downloadDeclarationPdf(Integer id) {
        return downloadDeclarationPdfUC.execute(AuthContext.getUser(), id);
    }

    @Override
    public List<GradeByQuarterDTO> getGrades(Integer id) {
        return GradeByQuarterMapper.toDTO(getGradesUC.execute(AuthContext.getUser(), id));
    }

    @Override
    public List<AttendanceDTO> getAttendance(Integer id) {
        return ConverterEntityToDTOUtil.convert(getAttendanceUC.execute(AuthContext.getUser(), id), AttendanceDTO.class);
    }

}


package com.almeja.pel.gestao.inbound.http.interfaces;

import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.dto.record.PaymentFormRecord;
import com.almeja.pel.gestao.infra.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.almeja.pel.gestao.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IInscriptionController.PATH)
public interface IInscriptionController {

    String PATH = PREFIX_PATH + "/inscription";

    @GetMapping("/grouped-by-year")
    List<InscriptionGroupedByYearDTO> getGroupedByYear();

    @GetMapping("/active-to-transfer")
    List<InscriptionDTO> getActiveToTransfer();

    @GetMapping("/last")
    InscriptionDTO getLastInscription();

    @GetMapping("/dependent/grouped-by-year")
    List<InscriptionGroupedByYearDTO> getDependentInscriptionsGroupedByYear(@RequestParam(name = "cpf") String dependentCpf);

    @GetMapping("/payments-form")
    List<PaymentFormRecord> getPaymentsForm();

    @GetMapping("/{id}/duplicate-receivables")
    List<DuplicateReceivableDTO> getDuplicateReceivables(@PathVariable("id") Integer id);

    @GetMapping("/{id}")
    InscriptionDTO getDetails(@PathVariable("id") Integer id);

    @GetMapping("/{id}/duplicate-receivable/{duplicateReceivableId}/download")
    byte[] downloadDuplicateReceivable(@PathVariable("id") Integer id, @PathVariable("duplicateReceivableId") Integer duplicateReceivableId);

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Integer register(@RequestParam(name = "classId") Integer classId);

    @PostMapping("/{id}/payment-form")
    String updateContractWithPaymentForm(@PathVariable("id") Integer id,
                                         @RequestParam(name = "paymentForm") EnumInscriptionPayment paymentForm);

    @GetMapping("/{id}/image-term")
    String getImageTerm(@PathVariable("id") Integer id);

    @PostMapping("/{id}/finalize-register")
    void finalizeRegister(@PathVariable("id") Integer id,
                          @RequestParam(name = "paymentForm") EnumInscriptionPayment paymentForm,
                          @RequestParam(name = "acceptContract") Boolean acceptContract,
                          @RequestParam(name = "acceptImageAuthorization") Boolean acceptImageAuthorization);

    @PostMapping("/{id}/cancel")
    void cancel(@PathVariable("id") Integer id);

    @GetMapping("/{id}/contract-pdf")
    byte[] downloadContractPdf(@PathVariable("id") Integer id);

    @GetMapping("/{id}/certificate-pdf")
    byte[] downloadCertificatePdf(@PathVariable("id") Integer id);

    @GetMapping("/{id}/declaration-pdf")
    byte[] downloadDeclarationPdf(@PathVariable("id") Integer id);

    @GetMapping("/{id}/grades")
    List<GradeByQuarterDTO> getGrades(@PathVariable("id") Integer id);

    @GetMapping("/{id}/attendance")
    List<AttendanceDTO> getAttendance(@PathVariable("id") Integer id);

}

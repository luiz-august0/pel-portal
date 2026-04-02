package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.AttendanceEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.AttendanceRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer>, AttendanceRepositoryGTW {

    @Override
    default AttendanceEntity findAndValidate(Integer attendanceId) {
        Optional<AttendanceEntity> attendanceOptional = findById(attendanceId);
        if (attendanceOptional.isEmpty()) throw new ValidatorException("Frequência não encontrada.");
        return attendanceOptional.get();
    }

    @Override
    @Query("" +
            "select attendance " +
            "  from AttendanceEntity attendance " +
            " where attendance.inscription.id = :inscriptionId" +
            " order by attendance.lessonPlan.completedDate desc")
    List<AttendanceEntity> findAllByInscriptionId(Integer inscriptionId);

}

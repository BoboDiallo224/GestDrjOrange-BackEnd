package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.Division
import com.orange.gesdrj.gesdrj.models.ProcessValidation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProcessRepository : JpaRepository<ProcessValidation,Long> {

    @Query("select p from processValidation p where p.demandeProcess = :x order by id asc")
    fun findProcessValidationByDemande(@Param ("x")demandeProcess:Demande):List<ProcessValidation>


    //fun findByDivisionAndStatutValidationAndDemandeProcess(division: String,statutValidation: Boolean, demandeProcess: Demande):ProcessValidation?
    fun findByIdAndDivisionAndStatutValidation(id: Long,division: String,statutValidation: Boolean):ProcessValidation?

    fun findByDivisionAndDemandeProcess(division: String,demandeProcess:Demande):ProcessValidation?

    fun findByidAndStatutValidation(id:Long ,statutValidation: Boolean):ArrayList<ProcessValidation>

    //@Query("select p from processValidation p where p.demandeProcess = :x and p.statutValidation = :y")
    fun findByDemandeProcessAndStatutValidation(demandeProcess: Demande ,statutValidation: Boolean):ArrayList<ProcessValidation>



}
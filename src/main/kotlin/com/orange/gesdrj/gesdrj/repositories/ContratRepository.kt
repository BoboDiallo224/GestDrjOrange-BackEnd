package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Contrat
import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.Document
import com.orange.gesdrj.gesdrj.models.ProcessValidation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface ContratRepository : JpaRepository<Contrat, Long> {

    @Query("select d from contrats d where d.contratDemande = :x")
    fun getContratByIdDemande(@Param("x")demande: Demande):Contrat

    fun findByContratDemande(contratDemande: Demande):Contrat?
}


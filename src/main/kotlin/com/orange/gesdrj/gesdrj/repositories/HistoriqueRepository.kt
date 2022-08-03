package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.HistoriqueRenouvellement
import org.springframework.data.jpa.repository.JpaRepository

interface HistoriqueRepository : JpaRepository<HistoriqueRenouvellement, Long> {

    fun findByDemandeRenouvellementOrderByIdDesc(demande: Demande):List<HistoriqueRenouvellement>
}
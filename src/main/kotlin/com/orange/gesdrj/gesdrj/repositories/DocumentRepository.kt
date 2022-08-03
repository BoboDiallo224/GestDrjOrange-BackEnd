package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface DocumentRepository : JpaRepository<Document, Long> {

    @Query("select d from documents d where d.documentDemande = :x")
    fun getDocumentByIdDemande(@Param("x")demande: Demande):List<Document>
}


package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Division
import com.orange.gesdrj.gesdrj.models.TypeContrat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DivisionRepository : JpaRepository<Division, Long> {

    @Query("select d from divisions d order by id desc")
    fun findOrderByIdDesc():List<Division>
}
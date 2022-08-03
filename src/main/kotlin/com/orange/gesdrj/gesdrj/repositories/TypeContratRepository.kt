package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.TypeContrat
import com.orange.gesdrj.gesdrj.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface TypeContratRepository : JpaRepository<TypeContrat, Long> {

}
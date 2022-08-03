package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.DemandeStatus



import org.springframework.data.jpa.repository.JpaRepository

interface DemandeStatusRepository : JpaRepository<DemandeStatus, Long> {
}
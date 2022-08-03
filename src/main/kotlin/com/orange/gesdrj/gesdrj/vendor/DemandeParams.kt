package com.orange.gesdrj.gesdrj.vendor

import com.orange.gesdrj.gesdrj.models.Demande
import org.springframework.web.multipart.MultipartFile

data class DemandeParams(val demande: Demande, val files: List<MultipartFile>)
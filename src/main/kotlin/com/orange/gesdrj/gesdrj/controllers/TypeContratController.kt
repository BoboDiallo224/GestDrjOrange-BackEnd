package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.models.TypeContrat
import com.orange.gesdrj.gesdrj.repositories.TypeContratRepository
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/typeContrats")
@CrossOrigin("*")

class TypeContratController( var typeContratRepository: TypeContratRepository) {

    /*@Autowired
    lateinit var typeContratRepository: TypeContratRepository*/

    @GetMapping("/{id}")
    fun findTypeContrat(@PathVariable id: Long): Optional<TypeContrat> {
        return typeContratRepository.findById(id)
    }

    @GetMapping
    fun findAllTypeContrat(): List<TypeContrat> = typeContratRepository.findAll()


    @PostMapping
    fun saveTypeContrat(@RequestBody typeContrat: TypeContrat): TypeContrat {


        return typeContratRepository.save(typeContrat)

    }

    @PutMapping
    fun updateTypeContrat(@RequestBody typeContrat: TypeContrat): TypeContrat {

        return typeContratRepository.save(typeContrat)
    }
}

package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.models.Division
import com.orange.gesdrj.gesdrj.repositories.DivisionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/divisions")
@CrossOrigin("*")
class DivisionController {

    @Autowired
    lateinit var divisionRepository: DivisionRepository

    @GetMapping("/{id}")
    fun findDivision(@PathVariable id: Long): Optional<Division> {
        return divisionRepository.findById(id)
    }

    @GetMapping()
    fun findAllDivision(): List<Division> = divisionRepository.findOrderByIdDesc()

    @PostMapping
    fun saveDivision(@RequestBody Division: Division): Division {
        return  divisionRepository.save(Division)
    }

    @PutMapping
    fun updateDivision(@RequestBody Division: Division): Division = divisionRepository.save(Division)

    /*@GetMapping("/distinct")
    fun findByDistincDivision(): List<String> = userRepository.findByDistincDivision()*/
}
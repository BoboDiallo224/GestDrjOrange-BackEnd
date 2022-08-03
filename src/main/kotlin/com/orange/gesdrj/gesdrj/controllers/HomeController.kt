package com.orange.gesdrj.gesdrj.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import java.io.File
import java.io.FileInputStream
import java.util.logging.Level.parse
import java.io.FileReader
import java.io.BufferedReader



@RestController
@CrossOrigin("*")
class HomeController{

    @GetMapping("/delete")
    fun index() {
        try {

            val file = File("D:\\OcrAppAndroid")
            file.setWritable(true)

            file.delete()

            if (file.delete() ) {

                println(file.name + " is deleted!")
            } else {
                println("Delete operation is failed. ${file.name}")
            }

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }
}
package com.orange.gesdrj.gesdrj

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class GesdrjApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    //runApplication<GesdrjApplication>(*args)
    SpringApplication.run(GesdrjApplication::class.java,*args)

}

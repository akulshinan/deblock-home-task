package org.deblock.exercise.infrastructure.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidation(e: Exception): ResponseEntity<Any> {
        return ResponseEntity.badRequest().build()
    }

    @ExceptionHandler(Throwable::class)
    fun handleGeneric(e: Throwable) = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
}

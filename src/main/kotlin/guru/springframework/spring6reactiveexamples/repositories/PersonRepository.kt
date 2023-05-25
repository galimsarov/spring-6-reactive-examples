package guru.springframework.spring6reactiveexamples.repositories

import guru.springframework.spring6reactiveexamples.domain.Person
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PersonRepository {
    fun getById(id: Int): Mono<Person>
    fun findAll(): Flux<Person>
}
package guru.springframework.spring6reactiveexamples.repositories

import guru.springframework.spring6reactiveexamples.domain.Person
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class PersonRepositoryImpl : PersonRepository {
    private val michael = Person(id = 1, firstname = "Michael", lastname = "Weston")
    private val fiona = Person(id = 2, firstname = "Fiona", lastname = "Glenanne")
    private val sam = Person(id = 3, firstname = "Sam", lastname = "Axe")
    private val jesse = Person(id = 4, firstname = "Jesse", lastname = "Porter")
    private val persons = listOf(michael, fiona, sam, jesse)

    override fun getById(id: Int): Mono<Person> {
        return findAll().filter { person -> person.id == id }.next()
    }

    override fun findAll(): Flux<Person> {
        return Flux.just(*persons.toTypedArray())
    }
}
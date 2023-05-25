package guru.springframework.spring6reactiveexamples.repositories

import guru.springframework.spring6reactiveexamples.domain.Person
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class PersonRepositoryImplTest {

    private val personRepository: PersonRepository = PersonRepositoryImpl()

    @Test
    fun testMonoByIdBlock() {
        val personMono: Mono<Person> = personRepository.getById(1)

        val person: Person = personMono.block() ?: Person()

        println(person.toString())
    }

    @Test
    fun testGetByIdSubscriber() {
        val personMono: Mono<Person> = personRepository.getById(1)

        personMono.subscribe { person -> println(person.toString()) }
    }

    @Test
    fun testMapOperation() {
        val personMono: Mono<Person> = personRepository.getById(1)

        personMono.map { person -> person.firstname }.subscribe { firstname -> println(firstname) }
    }

    @Test
    fun testFluxBlockFirst() {
        val personFlux: Flux<Person> = personRepository.findAll()

        val person: Person = personFlux.blockFirst() ?: Person()

        println(person.toString())
    }

    @Test
    fun testFluxSubscriber() {
        val personFlux: Flux<Person> = personRepository.findAll()

        personFlux.subscribe { person -> println(person.toString()) }
    }

    @Test
    fun testFluxMap() {
        val personFlux: Flux<Person> = personRepository.findAll()

        personFlux.map { person -> person.firstname }.subscribe { firstname -> println(firstname) }
    }

    @Test
    fun testFluxToList() {
        val personFlux: Flux<Person> = personRepository.findAll()

        val listMono: Mono<List<Person>> = personFlux.collectList()

        listMono.subscribe { list ->
            list.forEach { person ->
                println(person.firstname)
            }
        }
    }

    @Test
    fun testFilterOnName() {
        personRepository.findAll()
            .filter { person -> person.firstname == "Fiona" }
            .subscribe { person -> println(person.firstname) }
    }

    @Test
    fun testGetById() {
        val fionaMono: Mono<Person> = personRepository.findAll()
            .filter { person -> person.firstname == "Fiona" }
            .next()

        fionaMono.subscribe { person -> println(person.firstname) }
    }

    @Test
    fun testFindPersonByIdNotFound() {
        val personFlux: Flux<Person> = personRepository.findAll()

        val id = 8

        val personMono: Mono<Person> = personFlux.filter { person -> person.id == id }
            .single()
            .doOnError { throwable ->
                println("Error occurred in the flux")
                println(throwable.toString())
            }

        personMono.subscribe(
            { person -> println(person.toString()) },
            { throwable ->
                println("Error occurred in the mono")
                println(throwable.toString())
            }
        )
    }

    @Test
    fun testGetByIdFound() {
        val personMono: Mono<Person> = personRepository.getById(3)

        assertTrue(personMono.hasElement().block() ?: false)
    }

    @Test
    fun testGetByIdNotFound() {
        val personMono: Mono<Person> = personRepository.getById(6)

        assertFalse(personMono.hasElement().block() ?: true)
    }

    @Test
    fun testGetByIdFoundStepVerifier() {
        val personMono: Mono<Person> = personRepository.getById(3)

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete()

        personMono.subscribe { person -> println(person.firstname) }
    }

    @Test
    fun testGetByIdNotFoundStepVerifier() {
        val personMono: Mono<Person> = personRepository.getById(6)

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete()

        personMono.subscribe { person -> println(person.firstname) }
    }
}
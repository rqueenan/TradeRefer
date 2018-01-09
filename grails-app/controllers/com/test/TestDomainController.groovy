package com.test

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TestDomainController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TestDomain.list(params), model:[testDomainCount: TestDomain.count()]
    }

    def show(TestDomain testDomain) {
        respond testDomain
    }

    def create() {
        respond new TestDomain(params)
    }

    @Transactional
    def save(TestDomain testDomain) {
        if (testDomain == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testDomain.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testDomain.errors, view:'create'
            return
        }

        testDomain.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testDomain.label', default: 'TestDomain'), testDomain.id])
                redirect testDomain
            }
            '*' { respond testDomain, [status: CREATED] }
        }
    }

    def edit(TestDomain testDomain) {
        respond testDomain
    }

    @Transactional
    def update(TestDomain testDomain) {
        if (testDomain == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testDomain.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testDomain.errors, view:'edit'
            return
        }

        testDomain.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'testDomain.label', default: 'TestDomain'), testDomain.id])
                redirect testDomain
            }
            '*'{ respond testDomain, [status: OK] }
        }
    }

    @Transactional
    def delete(TestDomain testDomain) {

        if (testDomain == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        testDomain.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'testDomain.label', default: 'TestDomain'), testDomain.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testDomain.label', default: 'TestDomain'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

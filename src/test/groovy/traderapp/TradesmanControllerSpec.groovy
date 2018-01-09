package traderapp

import grails.test.mixin.*
import spock.lang.*
import traderapp.model.Tradesman
import traderapp.model.TradesmanController

@TestFor(TradesmanController)
@Mock(Tradesman)
class TradesmanControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        assert false, "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.tradesmanList
            model.tradesmanCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.tradesman!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def tradesman = new Tradesman()
            tradesman.validate()
            controller.save(tradesman)

        then:"The create view is rendered again with the correct model"
            model.tradesman!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            tradesman = new Tradesman(params)

            controller.save(tradesman)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/tradesman/show/1'
            controller.flash.message != null
            Tradesman.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def tradesman = new Tradesman(params)
            controller.show(tradesman)

        then:"A model is populated containing the domain instance"
            model.tradesman == tradesman
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def tradesman = new Tradesman(params)
            controller.edit(tradesman)

        then:"A model is populated containing the domain instance"
            model.tradesman == tradesman
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/tradesman/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def tradesman = new Tradesman()
            tradesman.validate()
            controller.update(tradesman)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.tradesman == tradesman

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            tradesman = new Tradesman(params).save(flush: true)
            controller.update(tradesman)

        then:"A redirect is issued to the show action"
            tradesman != null
            response.redirectedUrl == "/tradesman/show/$tradesman.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/tradesman/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def tradesman = new Tradesman(params).save(flush: true)

        then:"It exists"
            Tradesman.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(tradesman)

        then:"The instance is deleted"
            Tradesman.count() == 0
            response.redirectedUrl == '/tradesman/index'
            flash.message != null
    }
}

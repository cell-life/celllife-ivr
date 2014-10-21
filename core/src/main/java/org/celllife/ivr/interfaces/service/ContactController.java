package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.contact.ContactDto;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.exception.InvalidMsisdnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class ContactController {

    private static Logger log = LoggerFactory.getLogger(ContactController.class);

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    @Autowired
    ContactService contactService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/service/campaigns/{campaignId}/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ContactDto createContact(HttpServletResponse response, @RequestBody Collection<ContactDto> contactDtos, @PathVariable Long campaignId) {

        ContactDto contactDto = contactDtos.iterator().next();
        try {
            Contact contact = new Contact(contactDto.getMsisdn(), contactDto.getPassword(), campaignId, 0);
            contact = contactService.saveContact(contact);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return contact.getContactDto();
        } catch (InvalidMsisdnException e) {
            log.warn("Could not save contact because msisdn " + contactDto.getMsisdn() + "already exists.", e);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return new ContactDto();
        } catch (ContactExistsException e) {
            log.warn("Could not save contact because it already exists.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return new ContactDto();
        }

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value = "/service/campaigns/{campaignId}/contacts/{contactId}")
    public ContactDto updateContact(HttpServletResponse response, @RequestBody Collection<ContactDto> contactDtos, @PathVariable Long campaignId, @PathVariable Long contactId) {

        ContactDto contactDto = contactDtos.iterator().next();
        Contact contact = contactService.getContactById(contactId);

        if (contactDto.getMsisdn() != null)  {
            log.warn("You cannot change the msisdn of contact " + contactDto.getMsisdn() + ". Rather add a new contact.");
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return new ContactDto();
        }
        if (contactDto.getPassword() != null)
            contact.setPassword(contactDto.getPassword());
        if (contactDto.getProgress() != null)
            contact.setProgress(contactDto.getProgress());
        if (contactDto.getVoided() != null)
            contact.setVoided(contactDto.getVoided());
        if (contactDto.getCampaignId() != null) {
            log.warn("You cannot change the campaign id of a contact " + contactDto.getMsisdn() + ". Rather add a new contact.");
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return new ContactDto();
        }

        try {
            contact = contactService.saveContact(contact);
        } catch (ContactExistsException e) {
            log.warn("Contact already exists.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ContactDto();
        }

        return contact.getContactDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/service/campaigns/{campaignId}/contacts/{contactId}")
    public ContactDto deleteContact(HttpServletResponse response, @PathVariable Long campaignId, @PathVariable Long contactId){

        try {
            Contact contact = contactService.getContactById(contactId);
            contact.setVoided(true);
            contact = contactService.saveContact(contact);
            return contact.getContactDto();
        } catch (ContactExistsException e) {
            log.warn("This contact already exists.", e);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return new ContactDto();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/service/campaigns/{campaignId}/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ContactDto> getContactsInCampaign(@PathVariable Long campaignId) {

        List<Contact> contacts = contactService.findContactsInCampaign(campaignId);

        Collection<ContactDto> contactDtos = new ArrayList<>();

        for (Contact contact : contacts) {
            contactDtos.add(contact.getContactDto());
        }

        return contactDtos;

    }

}

package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.domain.verboice.contact.Contacts;
import org.celllife.ivr.domain.verboice.contact.ContactsDto;
import org.celllife.ivr.domain.verboice.contact.ContactsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ContactController {

    private static Logger log = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    ContactsRepository verboiceContactRepository;

    @RequestMapping(method = RequestMethod.POST, value= "/service/campaigns/{campaignId}/contacts")
    public ResponseEntity<String> createContact(@RequestBody List<ContactsDto> verboiceContactDtos, @PathVariable Integer campaignId){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date createdAt = null;
        Date updatedAt = null;

        try {
            createdAt = (Date) formatter.parse(verboiceContactDtos.get(0).getCreatedAt());
            updatedAt = (Date) formatter.parse(verboiceContactDtos.get(0).getUpdatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Contacts verboiceContact = new Contacts(7, new Date(), new Date());

        verboiceContact = verboiceContactRepository.save(verboiceContact);

        return new ResponseEntity<String>("Successfully added contacts.", HttpStatus.OK);

    }



    }

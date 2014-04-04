package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.verboice.VerboiceApplicationService;
import org.celllife.ivr.domain.campaign.CampaignDto;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.contact.FailedContactDto;
import org.celllife.ivr.domain.exception.IvrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
public class CsvUploadController {

    private static Logger log = LoggerFactory.getLogger(CsvUploadController.class);

    @Value("${ivr.validation_regex}")
    String validationRegex;

    @Autowired
    ContactService contactService;

    @Autowired
    VerboiceApplicationService verboiceApplicationService;

    @ResponseBody
    @RequestMapping(value = "/service/campaign/{campaignId}/contacts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<FailedContactDto> upload(@RequestParam("file") MultipartFile uploadedFile, @PathVariable Long campaignId) throws IOException, IvrException {

        CsvMapReader mapReader = new CsvMapReader(new InputStreamReader(uploadedFile.getInputStream()), CsvPreference.STANDARD_PREFERENCE);

        List<FailedContactDto> failedContactDtos = new ArrayList<FailedContactDto>();

        final String[] header = {"msisdn", "password"};
        final CellProcessor[] processors = getProcessors();

        Map<String, Object> contactMap;
        List<Contact> contactList = new ArrayList<>();

        contactMap = mapReader.read(header, processors);
        if ( (!contactMap.get("msisdn").toString().equalsIgnoreCase("msisdn")) || (!contactMap.get("password").toString().equalsIgnoreCase("password"))) {
            throw new IvrException("The column headers must be 'msisdn' and 'password'.");
        }

        while ((contactMap = mapReader.read(header, processors)) != null) {
            String msisdn = contactMap.get("msisdn").toString();
            String password = contactMap.get("password").toString();
            if (msisdn.matches(validationRegex)) {
                Contact contact = new Contact(msisdn, password, campaignId, 0);
                contactList.add(contact);
            } else {
                failedContactDtos.add(new FailedContactDto(msisdn, "Number format is invalid. The number should start with 27."));
            }
        }

        /*List<String> failedNumbers = verboiceApplicationService.createContactsAndSave(contactList, campaignId);
        for (String number : failedNumbers) {
            failedContactDtos.add(new FailedContactDto(number));
            contactList.remove(findIndexOfContactWithMsisdn(contactList, number));
        } */

        List<String> locallyFailedNumbers = contactService.saveContacts(contactList);
        for (String number : locallyFailedNumbers) {
            log.warn("The number " + number + " could not be added to the local database.");
            failedContactDtos.add(new FailedContactDto(number, "Possibly this number already exists in the campaign."));
        }

        mapReader.close();
        return failedContactDtos;

    }

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(),
                new NotNull()
        };

        return processors;
    }

    /*private int findIndexOfContactWithMsisdn(List<Contact> contactList, String value) {

        int counter = 0;

        for(Contact contact: contactList) {
            if(contact.getMsisdn().trim().contains(value))
                return counter;
            counter = counter + 1;
        }

        return -1;

    }*/
}

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class CsvUploadController {

    private static Logger log = LoggerFactory.getLogger(CsvUploadController.class);

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

        while ((contactMap = mapReader.read(header, processors)) != null) {
            Contact contact = new Contact(contactMap.get("msisdn").toString(), contactMap.get("password").toString(), campaignId, 0);
            contactList.add(contact);
        }

        List<String> failedNumbers = verboiceApplicationService.createContactsAndSave(contactList, campaignId);
        for (String number : failedNumbers) {
            log.warn("The number " + number + " could not be added to the Verboice database, possibly because it already exists.");
            failedContactDtos.add(new FailedContactDto(number));
            contactList.remove(findIndexOfContactWithMsisdn(contactList, number));
        }

        List<String> locallyFailedNumbers = contactService.saveContacts(contactList);
        for (String number : locallyFailedNumbers) {
            log.warn("The number " + number + " could not be added to the local database.");
            failedContactDtos.add(new FailedContactDto(number));
        }

        mapReader.close();
        return failedContactDtos;

    }

    /**
     * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
     * columns are read as null (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // firstName
                new NotNull()
        };

        return processors;
    }

    private int findIndexOfContactWithMsisdn(List<Contact> contactList, String value) {

        int counter = 0;

        for(Contact contact: contactList) {
            if(contact.getMsisdn().trim().contains(value))
                return counter;
            counter = counter + 1;
        }

        return -1;

    }
}

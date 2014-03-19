package org.celllife.ivr.interfaces.service;

import org.celllife.ivr.application.ContactService;
import org.celllife.ivr.application.VerboiceApplicationService;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.IvrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Map;

@Controller
public class CsvUploadController {

    private static Logger log = LoggerFactory.getLogger(CsvUploadController.class);

    @Autowired
    ContactService contactService;

    @Autowired
    VerboiceApplicationService verboiceApplicationService;

    @RequestMapping(value = "/service/campaign/{campaignId}/contacts", method = RequestMethod.POST)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile uploadedFile, @PathVariable Long campaignId) throws IOException {

        ICsvMapReader mapReader = new CsvMapReader(new InputStreamReader(uploadedFile.getInputStream()), CsvPreference.STANDARD_PREFERENCE);

        try {

            final String[] header = {"msisdn", "password"};
            final CellProcessor[] processors = getProcessors();

            Map<String, Object> contactMap;
            List<Contact> contactList = new ArrayList<>();

            while ((contactMap = mapReader.read(header, processors)) != null) {
                Contact contact = new Contact(contactMap.get("msisdn").toString(), contactMap.get("password").toString(), campaignId, 0);
                contactList.add(contact);
            }

            List<String> failedNumbers = verboiceApplicationService.createContactsFromCelllifeContactsAndSave(contactList, campaignId);
            for (String number : failedNumbers) {
                log.warn("The number " + number + " could not be added to the Verboice database, possibly because it already exists.");
                contactList.remove(findIndexOfContactWithMsisdn(contactList, number));
            }

            failedNumbers = contactService.saveContacts(contactList);
            // TODO: Send failed numbers back.

        } catch(IvrException | IOException e) {
            log.warn("Could not add contacts. Reason: " + e.getLocalizedMessage(), e);
            mapReader.close();
            return new ResponseEntity<String>("An error occurred while trying to add contacts. " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        mapReader.close();
        return new ResponseEntity<String>("Successfully added contacts.", HttpStatus.OK);

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

package org.celllife.ivr.application.verboice;

import org.celllife.ivr.application.campaign.CampaignService;
import org.celllife.ivr.application.contact.ContactService;
import org.celllife.ivr.application.utils.JsonUtils;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallLogRepository;
import org.celllife.ivr.domain.campaign.Campaign;
import org.celllife.ivr.domain.contact.Contact;
import org.celllife.ivr.domain.exception.ContactExistsException;
import org.celllife.ivr.domain.exception.IvrException;
import org.celllife.ivr.domain.exception.VerboiceDatabaseException;
import org.celllife.ivr.domain.verboice.contactaddresses.ContactAddresses;
import org.celllife.ivr.domain.verboice.contactaddresses.ContactAddressesRepository;
import org.celllife.ivr.domain.verboice.contacts.Contacts;
import org.celllife.ivr.domain.verboice.contacts.ContactsRepository;
import org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariables;
import org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariablesRepository;
import org.celllife.ivr.domain.verboice.projectvariables.ProjectVariables;
import org.celllife.ivr.domain.verboice.projectvariables.ProjectVariablesRepository;
import org.celllife.ivr.integration.verboice.VerboiceService;
import org.dozer.util.IteratorUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VerboiceApplicationServiceImpl implements VerboiceApplicationService {

    private static Logger log = LoggerFactory.getLogger(VerboiceApplicationServiceImpl.class);

    @Autowired
    VerboiceService verboiceService;

    @Autowired
    ContactService contactService;

    @Autowired
    CampaignService campaignService;

    JsonUtils jsonUtils = new JsonUtils();

    @Autowired
    CallLogRepository callLogRepository;

    @Autowired
    ContactsRepository verboiceContactsRepository;

    @Autowired
    ContactAddressesRepository contactAddressesRepository;

    @Autowired
    PersistedVariablesRepository persistedVariablesRepository;

    @Autowired
    ProjectVariablesRepository projectVariablesRepository;

    @Override
    public void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, int messageNumber) throws IvrException {

        String response = "";

        try {
            response = verboiceService.enqueueCall(channelName, callFlowName, scheduleName, msisdn, messageNumber);
        } catch (Exception e) {
            throw new IvrException("Could not enqueue call to verboice.");
        }

        Map<String, String> responseVariables;

        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response, e);
            throw new IvrException("Unrecognized Response from Verboice Server.");
        }

        if (responseVariables.containsKey("call_id")) {
            callLogRepository.save(new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), msisdn,
                    0, channelName, callFlowName, scheduleName, responseVariables.get("state"), messageNumber));
        } else {
            log.warn("No call ID returned from Verboice server.");
            throw new IvrException("No call ID returned from Verboice server.");
        }

    }

    @Override
    @Transactional("transactionManagerVerboice")
    public Contacts saveVerboiceContact(Contacts contact) {
        return verboiceContactsRepository.save(contact);
    }

    @Transactional("transactionManagerVerboice")
    private Integer createContactFromCelllifeContactAndSave(Contact contact, Campaign campaign, Integer projectVariableId) throws VerboiceDatabaseException {

        Long existingContacts = contactAddressesRepository.findTotalContactsWithMsisdn(contact.getMsisdn());

        if (existingContacts == 0) {

            Contacts verboiceContact = new Contacts(campaign.getVerboiceProjectId().intValue(), new Date(), new Date());
            verboiceContact = verboiceContactsRepository.save(verboiceContact);
            if (verboiceContact.getId() == null) {
                throw new VerboiceDatabaseException("Error saving contact to Verboice Database. This contact has not been added.");
            }

            ContactAddresses contactAddresses = new ContactAddresses(contact.getMsisdn(), verboiceContact.getId(), campaign.getVerboiceProjectId().intValue(), new Date(), new Date());
            contactAddressesRepository.save(contactAddresses);

            PersistedVariables persistedVariables = new PersistedVariables(contact.getPassword(), new Date(), new Date(), verboiceContact.getId(), projectVariableId);
            persistedVariablesRepository.save(persistedVariables);
            return verboiceContact.getId();

        } else {

            throw new VerboiceDatabaseException("Error saving contact to Verboice Database. Contact " + contact.getMsisdn() + " already exists in the verboice database. ");

        }

    }

    @Override
    @Transactional("transactionManagerVerboice")
    public List<String> createContactsAndSave(List<Contact> contacts, Long campaignId) throws IvrException {

        Campaign campaign = campaignService.getCampaign(campaignId);

        Iterable<ProjectVariables> projectVariables = projectVariablesRepository.findByNameAndProject(campaign.getVerboiceProjectId().intValue(), "password");
        List<ProjectVariables> projectVariablesList = IteratorUtils.toList(projectVariables.iterator());

        if (projectVariablesList.size() != 1) {
            throw new IvrException("Cannot save contacts to verboice because password variable does not exist on Verboice server.");
        }

        List<String> failedNumbers = new ArrayList<>();

        for (Contact contact : contacts) {
            try {
                Integer returnedId = createContactFromCelllifeContactAndSave(contact, campaign, projectVariablesList.get(0).getId());
                contact.setVerboiceContactId(returnedId);
                contactService.saveContact(contact);
            } catch (VerboiceDatabaseException e) {
                failedNumbers.add(contact.getMsisdn());
            } catch (ContactExistsException e) {
                log.warn("An error occurred while trying to save contact with msisdn " + contact.getMsisdn(), e);
            }
        }

        return failedNumbers;

    }

}

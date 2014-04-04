package org.celllife.ivr.domain.contact;

import junit.framework.Assert;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class ContactTest extends TestConfiguration{

    Contact contact = new Contact();

    @Test
    public void TestIsMsisdnNumeric() {

        Assert.assertEquals(true,contact.isMsisdnNumeric("27724194158"));

        Assert.assertEquals(false,contact.isMsisdnNumeric("2772419415b"));

        Assert.assertEquals(false,contact.isMsisdnNumeric("+27724194158"));

    }

}

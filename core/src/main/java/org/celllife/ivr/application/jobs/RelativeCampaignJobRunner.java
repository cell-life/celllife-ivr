package org.celllife.ivr.application.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class RelativeCampaignJobRunner extends QuartzJobBean {
	
	public static final String PROP_CAMPAIGN_ID = "campaignId";
	public static final String PROP_MSGSLOT = "msgSlot";
	public static final String PROP_MSGTIME = "msgTime";

	private Long campaignId;
	private Integer msgSlot;
	private Date msgTime;
	private ApplicationContext applicationContext;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		RelativeCampaignJob job = (RelativeCampaignJob) applicationContext.getBean(RelativeCampaignJob.NAME);
		job.sendMessagesForCampaign(campaignId, msgSlot, msgTime);
	}
	
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setMsgSlot(Integer msgSlot) {
		this.msgSlot = msgSlot;
	}
	
	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}
}

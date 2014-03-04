package org.celllife.ivr.domain;

import java.util.HashMap;
import java.util.Map;

public enum CampaignStatus {
	
	INACTIVE,
	/**
	 * only applies to fixed campaigns 
	 */
	SCHEDULED,
	/**
	 * only applies to relative and generic campaigns
	 */
	ACTIVE,
	/**
	 * only applies to fixed campaigns
	 */
	RUNNING,
	/**
	 * only applies to relative and generic campaigns
	 */
	STOPPING,
	FINISHED,
	SCHEDULE_ERROR;

	public boolean isActiveState(){
		return SCHEDULED.equals(this) || RUNNING.equals(this)
		|| ACTIVE.equals(this) || STOPPING.equals(this);
	}
}

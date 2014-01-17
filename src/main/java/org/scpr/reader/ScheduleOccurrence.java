package org.scpr.reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class ScheduleOccurrence extends Entity
{

    private String mTitle;
    private String mUrl;
    private Date mStartDate;
    private Date mEndDate;
    private boolean mIsRecurring;
    private Program mProgram;


    private static ScheduleOccurrence buildFromJson(JSONObject jsonSchedule)
    {
        ScheduleOccurrence schedule = new ScheduleOccurrence();

        try
        {
            schedule.setTitle(jsonSchedule.getString("title"));
            schedule.setUrl(jsonSchedule.getString("public_url"));
            schedule.setIsRecurring(jsonSchedule.getBoolean("is_recurring"));
            schedule.setStartDate(parseISODate(jsonSchedule.getString("starts_at")));
            schedule.setEndDate(parseISODate(jsonSchedule.getString("ends_at")));

            if (jsonSchedule.has("program"))
            { schedule.setProgram(Program.buildFromJson(jsonSchedule.getJSONObject("program"))); }

        } catch(JSONException e) {
            // TODO: Handle error
            e.printStackTrace();
        }

        return schedule;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        this.mTitle = title;
    }


    public String getUrl()
    {
        return mUrl;
    }

    public void setUrl(String url)
    {
        this.mUrl = url;
    }


    public Date getStartDate()
    {
        return mStartDate;
    }

    public void setStartDate(Date startDate)
    {
        this.mStartDate = startDate;
    }


    public Date getEndDate()
    {
        return mEndDate;
    }

    public void setEndDate(Date endDate)
    {
        this.mEndDate = endDate;
    }


    public boolean isIsRecurring()
    {
        return mIsRecurring;
    }

    public void setIsRecurring(boolean isRecurring)
    {
        this.mIsRecurring = isRecurring;
    }


    public Program getProgram()
    {
        return mProgram;
    }

    public void setProgram(Program program)
    {
        this.mProgram = program;
    }

}

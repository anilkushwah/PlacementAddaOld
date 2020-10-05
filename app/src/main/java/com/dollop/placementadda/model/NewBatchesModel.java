package com.dollop.placementadda.model;

/**
 * Created by Kasim on 17-01-2018.
 */

public class NewBatchesModel {
   String batch_id;
    String batch_title;
    String batch_detail;
    String batch_start_date;
    String notificationType;
    String category;

    public String getNotificationorBatch() {
        return NotificationorBatch;
    }

    public void setNotificationorBatch(String notificationorBatch) {
        NotificationorBatch = notificationorBatch;
    }

    String NotificationorBatch;

    public String getDateChange() {
        return DateChange;
    }

    public void setDateChange(String dateChange) {
        DateChange = dateChange;
    }

    String DateChange;

    public String getNewBacthTime() {
        return newBacthTime;
    }

    public void setNewBacthTime(String newBacthTime) {
        this.newBacthTime = newBacthTime;
    }

    String newBacthTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    String topic;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    String subject;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    String start_time;

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getBatch_title() {
        return batch_title;
    }

    public void setBatch_title(String batch_title) {
        this.batch_title = batch_title;
    }

    public String getBatch_detail() {
        return batch_detail;
    }

    public void setBatch_detail(String batch_detail) {
        this.batch_detail = batch_detail;
    }

    public String getBatch_start_date() {
        return batch_start_date;
    }

    public void setBatch_start_date(String batch_start_date) {
        this.batch_start_date = batch_start_date;
    }
}
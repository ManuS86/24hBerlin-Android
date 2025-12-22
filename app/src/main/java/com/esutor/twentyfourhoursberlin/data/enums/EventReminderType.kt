package com.esutor.twentyfourhoursberlin.data.enums

enum class EventReminderType(val idOffset: Int) {

    THREE_DAYS_BEFORE(idOffset = 0),
    TWELVE_HOURS_BEFORE(idOffset = 1),
    THREE_HOURS_BEFORE(idOffset = 2);

    fun createAlarmId(eventId: String): Int {
        return (eventId.hashCode() * 1000) + this.idOffset
    }
}
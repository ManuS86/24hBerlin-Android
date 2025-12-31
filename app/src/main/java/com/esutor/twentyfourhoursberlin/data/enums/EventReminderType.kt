package com.esutor.twentyfourhoursberlin.data.enums

enum class EventReminderType(val idOffset: Int) {

    ONE_WEEK_BEFORE(idOffset = 0),
    ONE_DAY_BEFORE(idOffset = 1),
    ONE_HOUR_BEFORE(idOffset = 2);

    fun createAlarmId(eventId: String): Int {
        return (eventId.hashCode() * 1000) + this.idOffset
    }
}
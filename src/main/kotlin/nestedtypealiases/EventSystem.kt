package org.kotlin.nestedtypealiases

class EventSystem {
    typealias EventId = String
    typealias EventData = Map<String, Any?>
    typealias EventHandler = (EventData) -> Unit
    typealias EventFilter = (EventData) -> Boolean
    typealias EventMetadata = Triple<EventId, Long, String>

    private val handlers = mutableMapOf<EventId, MutableList<EventHandler>>()
    private val filters = mutableMapOf<EventId, MutableList<EventFilter>>()
    private val eventHistory = mutableListOf<Pair<EventMetadata, EventData>>()

    fun subscribe(eventId: EventId, handler: EventHandler) {
        handlers.getOrPut(eventId) { mutableListOf() }.add(handler)
    }

    fun addFilter(eventId: EventId, filter: EventFilter) {
        filters.getOrPut(eventId) { mutableListOf() }.add(filter)
    }

    fun emit(eventId: EventId, data: EventData, source: String = "system") {
        val metadata = EventMetadata(eventId, System.currentTimeMillis(), source)

        val eventFilters = filters[eventId] ?: emptyList()
        if (eventFilters.any { filter -> !filter(data) }) {
            println("Event $eventId filtered out")
            return
        }

        eventHistory.add(metadata to data)

        handlers[eventId]?.forEach { handler ->
            try {
                handler(data)
            } catch (e: Exception) {
                println("Error in event handler: ${e.message}")
            }
        }
    }

    fun getEventHistory(): List<Pair<EventMetadata, EventData>> = eventHistory.toList()
}
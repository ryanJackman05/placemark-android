package org.setu.placemark

object AppData { // created as an object, AppData acts as a singleton
    val placedMarks = PlacemarkMemStore() // val - immutable
}

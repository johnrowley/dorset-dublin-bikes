package com.aaademo.mapdemo.model

class Position (var lat: Double, var lng: Double )

class BikeStation (val number: Int, val address: String, var position: Position)

class Stations(val stations: List<BikeStation>)
package com.kel3.tabsy

data class Restaurant(
    var id: String? = null, // Identifier unik
    var name: String? = null,
    var description: String? = null,
    var price: String? = null,
    var location: String? = null,
    var imageResource: Int? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null, null, null, null,null)
}

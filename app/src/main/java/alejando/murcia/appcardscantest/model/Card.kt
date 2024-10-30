package alejando.murcia.appcardscantest.model

data class Card(
    val pan: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String
)

class Item(itemId: Int,
           itemName: String,
           itemDescription: String,
           itemPrice: Float,
           itemCondition: String,
           itemCategory: String,
           itemStatus: String) {
  val item_id: Int = itemId
  val item_name: String = itemName
  val item_description: String = itemDescription
  val item_price: Float = itemPrice
  val item_condition: String = itemCondition
  val item_category: String = itemCategory
  val item_status: String = itemStatus
  
  def GetFormatString(): String = {
    ""
  }
}

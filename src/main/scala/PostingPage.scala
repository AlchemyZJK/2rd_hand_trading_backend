import scala.annotation.tailrec
import scala.io.StdIn.readChar

object PostingPage {
  @tailrec
  def PostingPageLoop(user: User): Unit = {
    println("You could do the following Operations:\n" +
      "- View all the Postings: [1]\n" +
      "- Choose an item and Add it to your Shopping Cart: [2]\n" +
      "- Go Back to Main Page: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        println("TODO: Show All the postings.")
        // TODO: Show All the postings.
        PostingPageLoop(user)
      case 50 =>
        println("TODO: Choose an item and Add it to your Shopping Cart.")
        // TODO: Choose an item and Add it to your Shopping Cart.
        PostingPageLoop(user)
      case 48 =>
        println("Go Back to Main Page")
    }
  }

  def ShowAllPostings(): Unit = {

  }

  def AddToShoppingCart(): Boolean = {
    false
  }
}
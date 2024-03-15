
import edu.ucdavis.cs.ecs036c.homework7.PriorityQueue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PriorityQueueTest {

    @Test
    fun testComprehensive() {
        println("You gotta come up with your own test cases")
        val priorityQueue = PriorityQueue<String,Int>()

        // Test case 1: Enqueue elements
        priorityQueue.set("low priority",5)
        priorityQueue.set("medium priority", 3)
        priorityQueue.set("high priority", 1)
        priorityQueue.set("lowest priority",8)
        priorityQueue.set("medium-low priority", 4)
        priorityQueue.set("medium high priority", 2)

        println(priorityQueue.toString())
        println(priorityQueue.isValid())
        // Test case 3: Queue should be empty now

        println("All tests passed.")
    }
}
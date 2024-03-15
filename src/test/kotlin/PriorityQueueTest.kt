
import edu.ucdavis.cs.ecs036c.homework7.PriorityQueue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFails

class PriorityQueueTest {

    @Test
    fun testComprehensive() {
        val priorityQueue = PriorityQueue<String,Int>()

        priorityQueue.set("low priority",5)
        priorityQueue.set("medium priority", 3)
        priorityQueue.set("high priority", 1)
        priorityQueue.set("lowest priority",8)
        priorityQueue.set("medium-low priority", 4)
        priorityQueue.set("medium high priority", 2)
        priorityQueue.update("shadow", 0)
        priorityQueue.update("shadow", 9)
        priorityQueue.update("shadow", 2)
        println(priorityQueue.toString())

        println(priorityQueue.pop())
        println(priorityQueue.pop())
        println(priorityQueue.pop())
        println(priorityQueue.pop())
        println(priorityQueue.pop())
        println(priorityQueue.pop())
        println(priorityQueue.pop())
        assertFails(){priorityQueue.pop()}





        println(priorityQueue.toString())
        println(priorityQueue.isValid())

    }
}
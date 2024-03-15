

import edu.ucdavis.cs.ecs036c.homework7.PriorityQueue
//import edu.ucdavis.cs.ecs036c.testing.GradescopeAnnotation
//import edu.ucdavis.cs.ecs036c.testing.GradescopeTest
//import edu.ucdavis.cs.ecs036c.testing.GradescopeTestWatcher
//import edu.ucdavis.cs.ecs036c.testing.Visibility
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import javax.print.attribute.standard.PrinterResolution
import kotlin.test.assertFailsWith

fun <T, P: Comparable <P> > PriorityQueue<T, P>.isValid() : Boolean {
    if (size != locationData.size) return false
    for(i in 0..< size){
        val (data, priority) = priorityData[i]
        if (locationData[data] != i) return false
        val parentIndex = (i-1)/2
        if (i != 0){
            if (priorityData[parentIndex].second > priority) return false
        }
    }
    return true
}

//@ExtendWith(GradescopeTestWatcher::class)
class PriorityQueueTest  {

    //@GradescopeAnnotation("Bad Init", maxScore = 10, visible = Visibility.hidden)
    @Test
    fun testBadInit(){
        assertFailsWith<Exception> {
            PriorityQueue<String, Int>("1" to 1, "1" to 2)
            return
        }
    }

    //@GradescopeAnnotation("Bad Pop", maxScore = 10, Visibility.hidden)
    @Test
    fun testBadPop(){
        assertFailsWith<Exception> {
            val p = PriorityQueue("1" to 1, "2" to 2)
            p.pop()
            p.pop()
            p.pop()
        }
    }

    //@GradescopeAnnotation("Normal Init", 20, Visibility.hidden)
    @Test
    fun testNormal(){
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<10) {
                testData.add(Pair("$i", i))
            }
            println(testData.toString())

            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap = PriorityQueue(*testArray)
            println(testHeap.toString())
            //problem: first element isn't sinking during construction
            assert(testHeap.isValid())
            for (i in 0..<10) {
                val data = testHeap.pop() //issue here: popping a 4???
                println(testHeap.toString())
                assert(data == "$i")
                assert(testHeap.isValid())
                println(testHeap.size)
                assert(testHeap.size == 9 - i)
                assert(testHeap.isValid())
            }
        }
    }

   // @GradescopeAnnotation("Normal Update Addition", 20, Visibility.hidden)
    @Test
    fun testNormalUpdateAdd(){
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap = PriorityQueue<String, Int>()
            for ((t, p) in testArray){
                testHeap[t] = p
                assert(testHeap.isValid())
            }
            assert(testHeap.isValid())
            for (i in 0..<100) {
                val data = testHeap.pop()
                assert(data == "$i")
                assert(testHeap.isValid())
                assert(testHeap.size == 99 - i)
                assert(testHeap.isValid())
            }
            testArray.shuffle()
            for ((t, p) in testArray){
                testHeap[t] = p
                assert(testHeap.isValid())
            }
            assert(testHeap.isValid())
            for (i in 0..<100) {
                val data = testHeap.pop()
                assert(data == "$i")
                assert(testHeap.isValid())
                assert(testHeap.size == 99 - i)
                assert(testHeap.isValid())
            }

        }
    }


   // @GradescopeAnnotation("Changing Down", 20, visible = Visibility.hidden)
    @Test
    fun testChangingDown() {
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap2 = PriorityQueue(*testArray)
            testArray.shuffle()
            println(testHeap2.toString())
            for((i, j) in testArray){
                testHeap2.update(i, -j)
                assert(testHeap2.isValid())
            }
            println(testHeap2.toString())
            for (i in 99 downTo 0) {
                val data = testHeap2.pop()
                assert(data == "$i")
                assert(testHeap2.isValid())
            }
        }
    }

   // @GradescopeAnnotation("Changing Up", 20, visible = Visibility.hidden)
    @Test
    fun testChangingUp() {
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", -i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap2 = PriorityQueue(*testArray)
            testArray.shuffle()
            for((i, j) in testArray){
                testHeap2.update(i, -j)
                assert(testHeap2.isValid())
            }
            for (i in 0 ..<100) {
                val data = testHeap2.pop()
                assert(data == "$i")
                assert(testHeap2.isValid())
            }
        }
    }

}
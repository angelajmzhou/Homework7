package edu.ucdavis.cs.ecs036c.homework7
import kotlin.math.floor

/*
 * Class for a priority queue that supports the comparable trait
 * on elements.  It sets up to return the lowest value priority (a min heap),
 * if you want the opposite use a comparable object that is reversed.
 *
 * You could use this for implementing Dijkstra's in O(|V + E| log (V) ) time instead
 * of the default O(V^2) time.
 */
class PriorityQueue<T, P: Comparable<P>> {

    /*
     * Invariants that need to be maintained:
     *
     * priorityData must always be in heap order
     * locationData must map every data element to its
     * corresponding index in the priorityData, and
     * must not include any extraneous entries.
     *
     * You must NOT change these variable names and you MUST
     * maintain these invariants, as the autograder checks that
     * the internal structure is maintained.
     */
    val priorityData = mutableListOf<Pair<T, P>>()
    val locationData = mutableMapOf<T, Int>()

    fun iLeftChild(i: Int): Int{
        return (2*i+1)
    }
    fun iRightChild(i: Int) :Int{
        return (2*i+2)
    }
    fun iParent(i: Int) :Int{
        return (i-1)/2
    }


    /*
    * Size function is just the internal size of the priority queue...
    */
    val size : Int
        get() = priorityData.size



    /*
     * This is a secondary constructor that takes a series of
     * data/priority pairs.  It should put the pairs in the heap
     * and then call heapify/ensure the invariants are maintained
     */
    constructor (vararg init: Pair<T, P>) {
        priorityData.addAll(init)
        // put all data/priority pairs in heap
        var index = 0
        init.forEach{
            locationData[it.first] = index
            index++
            /*logs the index of the item in locationData (a map, with data as a key)
            makes later access easier
            for now it's just the order it appears in init,
            as it will likely change later
             */
        }
        heapify()
        //ensures invariants are maintained
    }
    fun swap (first: Int, second: Int){
        val temp = priorityData[first]
        priorityData[first]=priorityData[second]
        priorityData[second]=temp
    }

    /*
     * Heapify should ensure that the constraints are all updated.  This
     * is called by the secondary constructor.
     */
    fun heapify(){
        //start should be the first leaf nodes
        //if at top of valid heap, do nothing
        var start = iParent(priorityData.size-1)+1
        //otherwise sift down/sink as necessary: can cheat and start at halfway
        while (start > 0){
            start = start-1
            //move node to correct position:  no child<parent
            sink(start, priorityData.size)
        }
    }

    /*
     * We support ranged-sink so that this could also be
     * used for heapsort, so sink without it just specifies
     * the range.
     */
    fun sink(i : Int) {
        var index = i
        val range = priorityData.size
        val leftChildIndex = iLeftChild(index)
        val rightChildIndex =  iRightChild(index)

        if (leftChildIndex >= range) {
            // No children, stop sinking.
            return
        }

        if (rightChildIndex >= range) {
            // Only left child exists.
            if (priorityData[leftChildIndex].second > priorityData[index].second) {
                // Swap them
                swap(index, leftChildIndex)
            }
            return
        }

        // Now if we get to here that means both children exist
        if (priorityData[leftChildIndex].second > priorityData[rightChildIndex].second && priorityData[leftChildIndex].second > priorityData[index].second) {
            // Swap them and recursively call sink on left child
            swap(index, leftChildIndex)
            sink(leftChildIndex, range)
        } else if (priorityData[rightChildIndex].second > priorityData[leftChildIndex].second && priorityData[rightChildIndex].second > priorityData[index].second) {
            // Another if statement, but you do the same exact thing with the right child
            swap(index, rightChildIndex)
            sink(rightChildIndex, range)
        }
    }



    /*
     * The main sink function.  It accepts a range
     * argument, that by default is the full array, and
     * which considers that only indices < range are valid parts
     * of the heap.  This enables sink to be used for heapsort.
     */
    fun sink(index: Int, range: Int) {
        val leftChildIndex = iLeftChild(index)
        val rightChildIndex =  iRightChild(index)

        if (leftChildIndex >= range) {
            // No children, stop sinking.
            return
        }

        if (rightChildIndex >= range) {
            // Only left child exists.
            if (priorityData[leftChildIndex].second > priorityData[index].second) {
                // Swap them
                swap(index, leftChildIndex)
            }
            return
        }

        // Now if we get to here that means both children exist
        if (priorityData[leftChildIndex].second > priorityData[rightChildIndex].second && priorityData[leftChildIndex].second > priorityData[index].second) {
            // Swap them and recursively call sink on left child
            swap(index, leftChildIndex)
            sink(leftChildIndex, range)
        } else if (priorityData[rightChildIndex].second > priorityData[leftChildIndex].second && priorityData[rightChildIndex].second > priorityData[index].second) {
            // Another if statement, but you do the same exact thing with the right child
            swap(index, rightChildIndex)
            sink(rightChildIndex, range)
        }
    }


    /*
     * And the swim operation as well...
     */
    fun swim(i : Int) {
        var index = i
        if (index == 0) return
        //if alr first, can't swim any further up...

        while (index > 0) {
            val parent = iParent(index)
            if (priorityData[parent].second > priorityData[index].second) {
                // Swap parent and current index
                swap(parent, index)
                index = parent
            } else {
                return
            }
            index = iParent(index)
        }
    }


    /*
     * This pops off the data with the lowest priority.  It MUST
     * throw an exception if there is no data left.
     */
    fun pop() : T {
        if (size == 0) {throw NoSuchElementException()}
        val result = priorityData.first().first //first value of priorityData
        swap(0, size - 1)
        //moves last element to front
        priorityData.removeAt(size - 1)
        locationData.remove(result)
        sink(0)
        //then sinks the front element
        return result
    }

    /*
     * And this function enables updating the priority of something in
     * the queue.  It should sink or swim the element as appropriate to update
     * its new priority.
     *
     * If the key doesn't exist it should create a new one
     */
    fun update(data: T, newPriority: P ) {
        val index = locationData[data]
        if (index == null) {
            priorityData.add(Pair(data, newPriority))
            locationData[data] = size - 1
            swim(size - 1)
        } else {
            priorityData[index] = Pair(data, newPriority)
            swim(index)
            sink(index)
        }
    }

    /*
     * A convenient shortcut for update, allowing array assignment
     */
    operator fun set(data: T, newPriority: P) {
        update(data, newPriority)
    }

    override fun toString(): String {
        var retVal = "["
        priorityData.forEach{
            retVal+= "("+it.first + "," + it.second +") "
        }
        retVal += "]"
        return retVal
    }

    /*
     * You don't need to implement this function but it is
     * strongly advised that you do so for testing purposes, to check
     * that all invariants are correct.
     */
    fun isValid() : Boolean {
        priorityData.indices.forEach { index ->
            val leftChildIndex = 2 * index + 1
            val rightChildIndex = 2 * index + 2

            // Check left child
            if (leftChildIndex < priorityData.size && priorityData[index].second > priorityData[leftChildIndex].second) {
                return false // Heap property violated
            }

            // Check right child
            if (rightChildIndex < priorityData.size && priorityData[index].second > priorityData[rightChildIndex].second) {
                return false // Heap property violated
            }
        }

        // Check locationData consistency
        locationData.entries.forEach { (data, index) ->
            if (index >= priorityData.size || priorityData[index].first != data) {
                return false // locationData does not match the priorityData indices
            }
        }

        return true
    }

}

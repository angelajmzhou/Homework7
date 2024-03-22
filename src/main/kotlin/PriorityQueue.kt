package edu.ucdavis.cs.ecs036c.homework7

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

    fun iLeftChild(i: Int): Int {
        return (2 * i + 1)
    }

    fun iRightChild(i: Int): Int {
        return (2 * i + 2)
    }

    fun iParent(i: Int): Int {
        return (i - 1) / 2
    }


    /*
    * Size function is just the internal size of the priority queue...
    */
    val size: Int
        get() = priorityData.size


    /*
     * This is a secondary constructor that takes a series of
     * data/priority pairs.  It should put the pairs in the heap
     * and then call heapify/ensure the invariants are maintained
     */
    constructor (vararg init: Pair<T, P>) {
        priorityData.addAll(init)
        // put all data/priority pairs in heap
        heapify() //build minheap from input array O(n)
    }

    fun swap(i: Int, j: Int) {
        val temp = priorityData[i]
        priorityData[i] = priorityData[j]
        priorityData[j] = temp

        locationData[priorityData[i].first] = i
        locationData[priorityData[j].first] = j
    }

    /*
     * Heapify should ensure that the constraints are all updated.  This
     * is called by the secondary constructor.
     */
    //should build a min heap from unordered array
    fun heapify(){
        // set a counter
        var i = size - 1
        // for each element in priorityData backwards
        for (pair in priorityData.reversed()){
            // set the index in locationData
            locationData[pair.first] = i
            // call sink
            sink(i)
            // minus 1 to i
            i--
        }
    }

    /*
     * We support ranged-sink so that this could also be
     * used for heapsort, so sink without it just specifies
     * the range. this is sink for a MAX HEAP
     */
    fun sink(i : Int) {
        sink(i, priorityData.size)
    }



    /*
     * The main sink function.  It accepts a range
     * argument, that by default is the full array, and
     * which considers that only indices < range are valid parts
     * of the heap.  This enables sink to be used for heapsort.
     */
    fun sink(index: Int, range: Int) {

        if (iLeftChild(index) >= range) {
            // No children, do nothing.
            return
        }

        var child = iLeftChild(index)

        if (iRightChild(index) >= range) {
            // Only left child exists.
            if (priorityData[child].second < priorityData[index].second) {
                // Swap them
                swap(index, child)
            }
            return
        }

        // Now if we get to here that means both children exist
        //if the right child is less than the left child, we use right child as min

        if (priorityData[child].second>priorityData[child+1].second) {
            child = child+1
            //if the child is smaller than the parent
        } else if (priorityData[child].second > priorityData[index].second) {
            // Another if statement, but you do the same exact thing with the right child
            swap(index, child)
            sink(child, range)
        }
    }


    /*
     * And the swim operation as well...
     */
    fun swim(i: Int) {
        var index = i
        if (index == 0) return
        //if already first, can't swim any further up...

        while (index > 0) {
            val parent = iParent(index)
            //if the parent is bigger than the current index...
            if (priorityData[parent].second > priorityData[index].second) {
                // Swap parent and current index so heap is maintained
                swap(parent, index)
                index = parent
                //keep schmoovin up the tree by node
            } else {
                return
            }
        }
    }


    /*
     * This pops off the data with the lowest priority.  It MUST
     * throw an exception if there is no data left.
     */
    fun pop(): T {
        if (size == 0) {
            throw NoSuchElementException()
        }
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
    fun update(data: T, newPriority: P) {
        val index = locationData[data]
        if (index == null) {
            priorityData.add(Pair(data, newPriority))
            locationData[data] = size - 1
            swim(size - 1)
        } else {
            val oldPriority = priorityData[index].second
            priorityData[index] = Pair(data, newPriority)
            if (newPriority < oldPriority) {
                swim(index)
            } else {
                sink(index)
            }
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
        priorityData.forEach {
            retVal += "(" + it.first + "," + it.second + ") "
        }
        retVal += "]"
        return retVal
    }

    /*
     * You don't need to implement this function but it is
     * strongly advised that you do so for testing purposes, to check
     * that all invariants are correct.
     */
    fun isValid(): Boolean {
        return true
    }
}
package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.algorithmvisualizer.databinding.ActivityInformationSpecificBinding

class InformationSpecific : AppCompatActivity() {
    private lateinit var binding:ActivityInformationSpecificBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityInformationSpecificBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        setSupportActionBar(binding.tbAlgoInformationSpecific)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding.tbAlgoInformationSpecific.setNavigationOnClickListener {
            onBackPressed()
        }
        val props=intent.getStringExtra("EXTRA_PROPS")
        if(props=="dijkstra") {
            binding.textView.text = "Dijkstra's"
            binding.scrollableText.text =
                "Algorithm \n\n1)Create a set sptSet (shortest path tree set) that keeps track of vertices included in shortest path tree, i.e., whose minimum distance from source is calculated and finalized. Initially, this set is empty. \n\n2) Assign a distance value to all vertices in the input graph. Initialize all distance values as INFINITE. Assign distance value as 0 for the source vertex so that it is picked first. \n\n3) While sptSet doesn’t include all vertices \n\na) Pick a vertex u which is not there in sptSet and has minimum distance value. \nb) Include u to sptSet. \nc) Update distance value of all adjacent vertices of u. To update the distance values, iterate through all adjacent vertices. For every adjacent vertex v, if sum of distance value of u (from source) and weight of edge u-v, is less than the distance value of v, then update the distance value of v. "
            binding.codeText.text=
                "class ShortestPath {\nint minDistance(int dist[], Boolean sptSet[])\n{\nint min = Integer.MAX_VALUE, min_index = -1;\nfor (int v = 0; v < V; v++)\nif (sptSet[v] == false && dist[v] <= min) {\nmin = dist[v];\nmin_index = v;\n}\nreturn min_index;\n}\n\nvoid printSolution(int dist[])\n{\nSystem.out.println(\"Vertex \t\t Distance from Source\");\nfor (int i = 0; i < V; i++)\nSystem.out.println(i + \" \t\t \" + dist[i]);\n}\n\nvoid dijkstra(int graph[][], int src)\n{\nint dist[] = new int[V]; // The output array. dist[i] will hold\nBoolean sptSet[] = new Boolean[V];\nfor (int i = 0; i < V; i++) {\ndist[i] = Integer.MAX_VALUE;\nsptSet[i] = false;\n}\ndist[src] = 0;\nfor (int count = 0; count < V - 1; count++) {\nint u = minDistance(dist, sptSet);\nsptSet[u] = true;\nfor (int v = 0; v < V; v++)\nif (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v])\ndist[v] = dist[u] + graph[u][v];\n}\nprintSolution(dist);\n}\n}"
        }

        if(props=="astar") {
            binding.textView.text = "A * Search"
            binding.scrollableText.text=
                "1: Place the starting node in the OPEN list.\n\n2: Check if the OPEN list is empty or not, if the list is empty then return failure and stops.\n\n3: Select the node from the OPEN list which has the smallest value of evaluation function (g+h), if node n is goal node then return success and stop, otherwise\n\n4: Expand node n and generate all of its successors, and put n into the closed list. For each successor n', check whether n' is already in the OPEN or CLOSED list, if not then compute evaluation function for n' and place into Open list.\n\n5: Else if node n' is already in OPEN and CLOSED, then it should be attached to the back pointer which reflects the lowest g(n') value.\n\n6: Return to Step 2."
            binding.codeText.text="public static Node aStar(Node start, Node target){\nPriorityQueue<Node> closedList = new PriorityQueue<>();\nPriorityQueue<Node> openList = new PriorityQueue<>();\nstart.f = start.g + start.calculateHeuristic(target);\nopenList.add(start);\nwhile(!openList.isEmpty()){\nNode n = openList.peek();\nif(n == target){\nreturn n;\n}\nfor(Node.Edge edge : n.neighbors){\nNode m = edge.node;\ndouble totalWeight = n.g + edge.weight;\nif(!openList.contains(m) && !closedList.contains(m)){\nm.parent = n;\nm.g = totalWeight;\nm.f = m.g + m.calculateHeuristic(target);\nopenList.add(m);\n} else {\nif(totalWeight < m.g){\nm.parent = n;\nm.g = totalWeight;\nm.f = m.g + m.calculateHeuristic(target);\nif(closedList.contains(m)){\nclosedList.remove(m);\nopenList.add(m);\n}\n}\n}\n}\nopenList.remove(n);\nclosedList.add(n);\n}\nreturn null;\n}"
        }
        if(props=="bfs") {
            binding.textView.text = "BFS"
            binding.codeText.text="void BFS(int s)\n{\nboolean visited[] = new boolean[V];\nLinkedList<Integer> queue = new LinkedList<Integer>();\nvisited[s]=true;\nqueue.add(s);\nwhile (queue.size() != 0)\n{\ns = queue.poll();\nSystem.out.print(s+\" \")\nIterator<Integer> i = adj[s].listIterator();\nwhile (i.hasNext())\n{\nint n = i.next();\nif (!visited[n])\n{\nvisited[n] = true;\nqueue.add(n);\n}\n}\n}\n}"
            binding.scrollableText.text=
                "There are many ways to traverse graphs. BFS is the most commonly used approach.\n\nBFS is a traversing algorithm where you should start traversing from a selected node (source or starting node) and traverse the graph layerwise thus exploring the neighbour nodes (nodes which are directly connected to source node). You must then move towards the next-level neighbour nodes.\n\nAs the name BFS suggests, you are required to traverse the graph breadthwise as follows:\n\n1.First move horizontally and visit all the nodes of the current layer\n2.Move to the next layer"
        }
        if(props=="dfs") {
            binding.textView.text = "DFS"
            binding.scrollableText.text=
                "\n1:Create a recursive function that takes the index of node and a visited array.\n\nMark the current node as visited and print the node.\n\nTraverse all the adjacent and unmarked nodes and call the recursive function with index of adjacent node."
            binding.codeText.text=
                "void dfs(int n,int a[20][20],int u,int visited[],int e[20][2])\n{\nint v;\nint k=1;\nvisited[u]=1;\nfor(v=1;v<=n;v++)\n{\nif(a[u][v]==1 && visited[v]==0)\n{\ne[k][1]=u;\ne[k][2]=v;\nk=k+1;\ndfs(n,a,v,visited,e);\n}\n}\n}"
        }
        if(props=="bubblesort") {
            binding.textView.text = "Bubble Sort"
            binding.codeText.text=
                "void bubbleSort(int arr[])\n{\nint n = arr.length;\nfor (int i = 0; i < n-1; i++)\nfor (int j = 0; j < n-i-1; j++)\nif (arr[j] > arr[j+1])\n{\nint temp = arr[j];\narr[j] = arr[j+1];\narr[j+1] = temp;\n}\n}"
            binding.scrollableText.text=
                "Bubble sort, also referred to as comparison sort, is a simple sorting algorithm that repeatedly goes through the list, compares adjacent elements and swaps them if they are in the wrong order. This is the most simplest algorithm and inefficient at the same time. Yet, it is very much necessary to learn about it as it represents the basic foundations of sorting."
        }
        if(props=="quicksort") {
            binding.textView.text = "Quick Sort"
            binding.scrollableText.text=
                "It picks an element as pivot and partitions the given array around the picked pivot. There are many different versions of quickSort that pick pivot in different ways. \n\nAlways pick first element as pivot.\nAlways pick last element as pivot.\nPick a random element as pivot.\nPick median as pivot.\n\nThe key process in quickSort is partition(). Target of partitions is, given an array and an element x of array as pivot, put x at its correct position in sorted array and put all smaller elements (smaller than x) before x, and put all greater elements (greater than x) after x. All this should be done in linear time."
            binding.codeText.text=
                "int partition(int low,int high)\n{\nint pivot=arr[low];\nint i=low,j=high;\nwhile(i<j) {\ncomparisons+=1;\nwhile(i<high && arr[j]<=pivot) {\ncomparisons+=2;\ni=i+1;\n}\nwhile(j>low && arr[j]>=pivot) {\ncomparisons+=2;\nj=j-1;\n} if(i<j) {\ncomparisons+=1;\ninterchange(i,j);\n}}\narr[low]=arr[j];\narr[j]=pivot;\nreturn j;\n}\n\nvoid interchange(int i,int j) {\nint temp=arr[i];\narr[i]=arr[j];\narr[j]=temp;\n}\n\nstatic void quicksort(int low,int high)\n{\nif(low<high)\n{\ncomparisons+=1;\nint j=partition(low,high);\nquicksort(low,j-1);\nquicksort(j+1,high);\n}\n}"
        }
        if(props=="selectionsort") {
            binding.textView.text = "Selection Sort"
            binding.scrollableText.text=
                "The selection sort algorithm sorts an array by repeatedly finding the minimum element (considering ascending order) from unsorted part and putting it at the beginning. The algorithm maintains two subarrays in a given array.\n1) The subarray which is already sorted. \n2) Remaining subarray which is unsorted.\nIn every iteration of selection sort, the minimum element (considering ascending order) from the unsorted subarray is picked and moved to the sorted subarray. "
            binding.codeText.text=
                "void sort(int arr[])\n{\nint n = arr.length;\nfor (int i = 0; i < n-1; i++)\n{\nint min_idx = i;\nfor (int j = i+1; j < n; j++)\nif (arr[j] < arr[min_idx])\nmin_idx = j;\nint temp = arr[min_idx];\narr[min_idx] = arr[i];\narr[i] = temp;\n}\n}"
        }
        if(props=="radixsort") {
            binding.textView.text = "Radix Sort"
            binding.scrollableText.text=
                "Radix sort is a sorting algorithm that sorts the elements by first grouping the individual digits of the same place value. Then, sort the elements according to their increasing/decreasing order.\n\nSuppose, we have an array of 8 elements. First, we will sort elements based on the value of the unit place. Then, we will sort elements based on the value of the tenth place. This process goes on until the last significant place."
            binding.codeText.text=
                "void countSort(int arr[], int n, int exp)\n{\nint output[] = new int[n];\nint i;\nint count[] = new int[10];\nArrays.fill(count, 0);\nfor (i = 0; i < n; i++)\ncount[(arr[i] / exp) % 10]++;\nfor (i = 1; i < 10; i++)\ncount[i] += count[i - 1];\nfor (i = n - 1; i >= 0; i--) {\noutput[count[(arr[i] / exp) % 10] - 1] = arr[i];\ncount[(arr[i] / exp) % 10]--;\n}\nfor (i = 0; i < n; i++)\narr[i] = output[i];\n}\n\nvoid radixsort(int arr[], int n)\n{\nint m = getMax(arr, n);\nfor (int exp = 1; m / exp > 0; exp *= 10)\ncountSort(arr, n, exp);\n}"
        }
        if(props=="insertionsort") {
            binding.textView.text = "Insertion Sort"
            binding.scrollableText.text=
                "To sort an array of size n in ascending order: \n1: Iterate from arr[1] to arr[n] over the array. \n2: Compare the current element (key) to its predecessor. \n3: If the key element is smaller than its predecessor, compare it to the elements before. Move the greater elements one position up to make space for the swapped element."
            binding.codeText.text=
                "void sort(int arr[])\n{\nint n = arr.length;\nfor (int i = 1; i < n; ++i) {\nint key = arr[i];\nint j = i - 1;\nwhile (j >= 0 && arr[j] > key) {\narr[j + 1] = arr[j];\nj = j - 1;\n}\narr[j + 1] = key;\n}\n}"
        }
        if(props=="heapsort") {
            binding.textView.text = "Heap Sort"
            binding.scrollableText.text=
                "1. Build a max heap from the input data.\n2. At this point, the largest item is stored at the root of the heap. Replace it with the last item of the heap followed by reducing the size of heap by 1. Finally, heapify the root of the tree. \n3. Repeat step 2 while the size of the heap is greater than 1."
            binding.codeText.text=
                "public void sort(int arr[])\n{\nint n = arr.length;\nfor (int i = n / 2 - 1; i >= 0; i--)\nheapify(arr, n, i);\nfor (int i = n - 1; i > 0; i--) {\nint temp = arr[0];\narr[0] = arr[i];\narr[i] = temp;\nheapify(arr, i, 0);\n}\n}\n\nvoid heapify(int arr[], int n, int i)\n{\nint largest = i;\nint l = 2 * i + 1;\nint r = 2 * i + 2;\nif (l < n && arr[l] > arr[largest])\nlargest = l;\nif (r < n && arr[r] > arr[largest])\nlargest = r;\nif (largest != i) {\nint swap = arr[i];\narr[i] = arr[largest];\narr[largest] = swap;\nheapify(arr, n, largest);\n}\n}"
        }
        if(props=="mergesort") {
            binding.textView.text = "Merge Sort"
            binding.scrollableText.text=
                "It divides the input array into two halves, calls itself for the two halves, and then merges the two sorted halves. The merge() function is used for merging two halves. The merge(arr, l, m, r) is a key process that assumes that arr[l..m] and arr[m+1..r] are sorted and merges the two sorted sub-arrays into one"
            binding.codeText.text=
                "void mergesort(int low,int high)\n{\nif(low<high)\n{\ncomparisons+=1;\nint mid=(low+high)/2;\nmergesort(low,mid);\nmergesort(mid+1,high);\nmerge(low,mid,high);\n}\n}\n\nvoid merge(int low,int mid,int high)\n{\nint n=high-low+1;\nint [] t =new int [n];\nint i=low,j=mid+1,k=0;\nwhile((i<=mid) && (j<=high))\n{\ncomparisons+=2;\\nif(arr[i]<arr[j])\n{\ncomparisons+=1;\nt[k]=arr[i];\ni++;\n}\nelse {\ncomparisons+=1;\nt[k]=arr[j];\nj++;\n}\nk++;\n}\nwhile(i<=mid)\n{\ncomparisons+=1;\nt[k]=arr[i];\ni++;\nk++;\n}\nwhile(j<=high)\n{\ncomparisons+=1;\nt[k]=arr[j];\nj++;\nk++;\n} for(k=0;k<n;k++)\n{\ncomparisons+=1;\narr[low+k]=t[k];\n}\n}"

        }

    }
}

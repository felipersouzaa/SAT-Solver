package trash;

class MergeTriz {
	public static void merge(String arr[], int[][] arr2, int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        String L[] = new String [n1];
        String R[] = new String [n2];
        int L2[][] = new int [n1][];
        int R2[][] = new int [n2][];
 
        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i) {
            L[i] = arr[l + i];
            L2[i] = new int [arr2[l + i].length];
            for (int j = 0; j < arr2[l + i].length; j++)
            	L2[i][j] = arr2[l + i][j];
        }
        for (int j=0; j<n2; ++j) {
            R[j] = arr[m + 1+ j];
            R2[j] = new int[arr2[m + 1+ j].length];
            for (int k = 0; k < arr2[m + 1 + j].length; k++)
            	R2[j][k] = arr2[m + 1+ j][k];
        }
 
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (L[i].length() <= R[j].length())
            {
                arr[k] = L[i];
                arr2[k] = new int [L2[i].length];
                for (int w = 0; w < L2[i].length; w++)
                	arr2[k][w] = L2[i][w];
                i++;
            }
            else
            {
                arr[k] = R[j];
                arr2[k] = new int [R2[j].length];
                for (int w = 0; w < R2[j].length; w++)
                	arr2[k][w] = R2[j][w];
                j++;
            }
            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            arr[k] = L[i];
            arr2[k] = new int [L2[i].length];
            for (int w = 0; w < L2[i].length; w++)
            	arr2[k][w] = L2[i][w];
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2){
            arr[k] = R[j];
            arr2[k] = new int [R2[j].length];
            for (int w = 0; w < R2[j].length; w++)
            	arr2[k][w] = R2[j][w];
            j++;
            k++;
        }
    }
	
    public static void sort(String arr[], int [][] arr2, int l, int r) {
        if (l < r)
        {
            // Find the middle point
            int m = (l+r)/2;
 
            // Sort first and second halves
            sort(arr, arr2, l, m);
            sort(arr, arr2, m+1, r);
 
            // Merge the sorted halves
            merge(arr, arr2, l, m, r);
        }
    }
}
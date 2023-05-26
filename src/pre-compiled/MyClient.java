import java.util.Arrays;

public class yes{
  public static void main(String[] args) {
    String[] arr = {"JOBN 172 4 320 2 50 120","JOBN 172 4 100 2 50 120","JOBN 172 4 200 2 50 120", "JOBN 172 4 5000 2 50 120"};
    String[][] pls = new String[arr.length][7];

    for(int i = 0; i<arr.length;i++){
      for(int j = 0; j<pls[0].length;j++){
        pls[i][j] = arr[i].split(" ")[j];
        System.out.println(arr[i].split(" ")[j]);
      }
    }
    for(int i = 0; i<arr.length;i++){
      System.out.println("\n");
      for(int j = 0; j<pls[0].length;j++){
        System.out.print(pls[i][j] + " ");
      }
    }
  }
}
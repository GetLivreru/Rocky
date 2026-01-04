package generics;

public class task2 {
    public static void main(String[] args) {

        Integer[] nums = {1, 2, 3};
        String[] words = {"hi", "hello"};

        Integer firstNum = Utils.first(nums);
        String firstWord = Utils.first(words);

        System.out.println(firstNum);
        System.out.println(firstWord);
    }
}


class Utils {

    public static <T> T first(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        return items[0];
    }
}

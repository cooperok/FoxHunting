package ua.cooperok.foxhunting.helpers;

/**
 * Class that helps with russian or ukrainian morphology 
 */
public class Morphology {

    private static String default_first_end = "";

    private static String default_second_end = "а";

    private static String default_third_end = "ов";

    /**
     * Возвращает окончание для слова, в зависимости от количества
     * 1 - товар'', 2 товар'а', 5 товар'ов'
     * 
     * @param number
     * @param first_end
     * @param second_end
     * @param third_end
     * @return
     */
    public static String getWordEndingByNumber(int number, String first_end, String second_end, String third_end) {
        String result = third_end;
        String str_number = String.valueOf(number);
        if (str_number.matches("^(.*)(11|12|13|14|15|16|17|18|19)$")) {
            result = third_end;
        } else if (str_number.matches("^(.*)1$")) {
            result = first_end;
        } else if (str_number.matches("^(.*)(2|3|4)$")) {
            result = second_end;
        }
        return result;
    }

    /**
     * Возвращает окончание для слова, в зависимости от количества
     * 1 - товар'', 2 товар'а', 5 товар'ов'
     * 
     * @param number
     * @return
     */
    public static String getWordEndingByNumber(int number) {
        return getWordEndingByNumber(number, default_first_end, default_second_end, default_third_end);
    }

    /**
     * Возвращает окончание для слова, в зависимости от количества
     * 1 - товар'', 2 товар'а', 5 товар'ов'
     * 
     * @param number
     * @param first_end
     * @return
     */
    public static String getWordEndingByNumber(int number, String first_end) {
        return getWordEndingByNumber(number, first_end, default_second_end, default_third_end);
    }

    /**
     * Возвращает окончание для слова, в зависимости от количества
     * 1 - товар'', 2 товар'а', 5 товар'ов'
     * 
     * @param number
     * @param first_end
     * @param second_end
     * @return
     */
    public static String getWordEndingByNumber(int number, String first_end, String second_end) {
        return getWordEndingByNumber(number, first_end, second_end, default_third_end);
    }

}

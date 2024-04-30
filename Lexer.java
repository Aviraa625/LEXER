package lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lexer {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Orolt: ");
            String code = scanner.nextLine();
            List<Token> result = lexer(code);
            System.out.println("Niit tokenuud: " + result);

            List<String> types = new ArrayList<>();
            for (Token token : result) {
                types.add(token.type);
            }
            System.out.println("Turuluud: " + types);

            List<Integer> nums = new ArrayList<>();
            for (Token token : result) {
                if ("num".equals(token.type)) {
                    nums.add(Integer.parseInt(token.value));
                }
            }
            System.out.println("Toonuud: " + nums);

            String operation = "";
            for (Token token : result) {
                if ("OPERATION".equals(token.type)) {
                    operation = token.value;
                    break;
                }
            }
            System.out.println("Uildel: " + operation);

            int num1 = nums.get(0);
            int num2 = nums.get(1);
            int resultValue = 0;
            if ("-".equals(operation)) {
                resultValue = num1 - num2;
            } else if ("+".equals(operation)) {
                resultValue = num1 + num2;
            } else if ("*".equals(operation)) {
                resultValue = num1 * num2;
            } else if ("/".equals(operation)) {
                resultValue = num1 / num2;
            } else {
                System.out.println("Tanihgui uildel baina");
            }
            System.out.println("Garalt: " + resultValue);
        }
    }

    public static List<Token> lexer(String line) {
        List<Token> tokens = new ArrayList<>();
        int counter = 0;
        while (counter < line.length()) {
            char ch = line.charAt(counter);
            if (Character.isDigit(ch)) {
                Token token = lexNum(line.substring(counter));
                counter += token.consumed;
                tokens.add(token);
            } else if (ch == '"' || ch == '\'') {
                Token token = lexStr(line.substring(counter));
                counter += token.consumed;
                tokens.add(token);
            } else if (Character.isLetter(ch)) {
                Token token = lexId(line.substring(counter));
                counter += token.consumed;
                tokens.add(token);
            } else if (ch == '=') {
                tokens.add(new Token("ASSIGN", "=", 1));
                counter++;
            } else if (ch == '+' || ch == '-') {
                tokens.add(new Token("OPERATION", Character.toString(ch), 1));
                counter++;
            } else {
                counter++;
            }
        }
        return tokens;
    }

    public static Token lexNum(String line) {
        StringBuilder num = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (!Character.isDigit(c)) {
                break;
            }
            num.append(c);
        }
        return new Token("num", num.toString(), num.length());
    }

    public static Token lexStr(String line) {
        char delimiter = line.charAt(0);
        StringBuilder string = new StringBuilder();
        for (int i = 1; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == delimiter) {
                break;
            }
            string.append(c);
        }
        return new Token("str", string.toString(), string.length());
    }

    public static Token lexId(String line) {
        String[] keywords = {"print", "int", "while", "if", "elif", "else"};
        StringBuilder id = new StringBuilder();
        for (char c : line.toCharArray()) {
            if ((!Character.isDigit(c) && !Character.isLetter(c)) || c == ' ' || c == '=') {
                break;
            }
            id.append(c);
        }
        String idString = id.toString();
        for (String keyword : keywords) {
            if (idString.equals(keyword)) {
                return new Token("keyword", idString, id.length());
            }
        }
        return new Token("ID", idString, id.length());
    }

    static class Token {
        String type;
        String value;
        int consumed;

        Token(String type, String value, int consumed) {
            this.type = type;
            this.value = value;
            this.consumed = consumed;
        }

        @Override
        public String toString() {
            return "(" + type + ", " + value + ")";
        }
    }
}

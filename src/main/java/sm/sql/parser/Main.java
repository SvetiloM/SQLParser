package sm.sql.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sm.sql.parser.entity.Select;

import java.util.Scanner;

public class Main {
    public static void main(String[] arg) throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        ParseService service = new ParseService();
        Select select = service.parse(input);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(select);
        System.out.println(json);
    }
}
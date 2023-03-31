package sm.sql.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sm.sql.parser.entity.Select;
import sm.sql.parser.parser.SelectQueryParser;

import java.util.Scanner;

@SpringBootApplication
public class Main
        implements CommandLineRunner {
    @Autowired
    private SelectQueryParser service;

    public static void main(String[] arg) {
        SpringApplication.run(Main.class, arg);
    }

    @Override
    public void run(String... args) throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Select select = service.parse(input).get();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(select);
        System.out.println(json);
    }
}
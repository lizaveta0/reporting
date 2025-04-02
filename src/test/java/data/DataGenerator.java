package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DataGenerator {

    private DataGenerator() {
    }

    public static String generatePhone(String locale) {
        return new Faker(new Locale(locale)).phoneNumber().phoneNumber();
    }

    public static String generateAddress(String locale) {
        return new Faker(new Locale(locale)).address().city();
    }

    public static String generateName(String locale) {
        return new Faker(new Locale(locale)).name().name();
    }

    public static String generateDate(int days) {
        Date date = new Faker().date().future(365, days,TimeUnit.DAYS);
        LocalDate futureDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return futureDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
        Boolean accept;
    }

    public static class DeliveryCard {
        private DeliveryCard() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateAddress(locale), generateName(locale),generatePhone(locale), true);
        }
    }

}
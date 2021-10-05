import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Main {
    public static void main(String[] args) throws Exception {
        //Прочитаем JSON файл
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/tickets.json"));
        JSONObject jsonObject = (JSONObject) obj;
        //Создадим массив из всех билетов
        JSONArray tickets = (JSONArray) jsonObject.get("tickets");
        //Форматирование даты
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

        //Создадим массив времени каждого нужного нам рейса, будем использовать для вывовда среднего времени
        ArrayList<Long> flightTimeList = new ArrayList<>();

        for (int i = 0; i < tickets.size(); i++) {
            JSONObject jsonObjectInArray = (JSONObject) tickets.get(i);
            String r = (String) jsonObjectInArray.get("destination_name");
            //Выберем только перелеты из Владивостока в Тель-Авив
            if (jsonObjectInArray.get("origin_name").equals("Владивосток") && jsonObjectInArray.get("destination_name").equals("Тель-Авив")) {
                //Дата вылета
                String departureDate = (String) jsonObjectInArray.get("departure_date");
                //Время вылета
                String departureTime = (String) jsonObjectInArray.get("departure_time");
                //Преобразуем в дату со времем
                Date depature = simpleDateFormat.parse(departureDate + " " + departureTime);
                //Теперь проделаем тоже самое с датой и временем прибытия
                String arrivalDate = (String) jsonObjectInArray.get("arrival_date");
                String arrivalTime = (String) jsonObjectInArray.get("arrival_time");
                Date arrival = simpleDateFormat.parse(arrivalDate + " " + arrivalTime);
                //Теперь посчитаем длину полета в минутах для каждого рейса и занесем в наш массив
                long flightTime = (arrival.getTime() - depature.getTime()) / (1000 * 60);
                flightTimeList.add(flightTime);
            }
        }
        //Посчитаем среднее время полета
        int countOfFlights = 0;
        int totalFlightTime = 0;
        for (int i = 0; i < flightTimeList.size(); i++) {
            totalFlightTime += flightTimeList.get(i);
            countOfFlights++;
        }
        System.out.println("Среднее время рейса из Владивостока в Тель-Авив: " + totalFlightTime / countOfFlights);

        //Посчитаем 90-й процентиль времени полета между городами  Владивосток и Тель-Авив
        Collections.sort(flightTimeList);
        int index = (int) Math.ceil(0.9 * flightTimeList.size());
        System.out.println("90-й процентиль времени полета между городами  Владивосток и Тель-Авив: " + flightTimeList.get(index - 1));
    }
}
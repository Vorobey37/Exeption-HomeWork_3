package finalTask;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, 
разделенные пробелом:
Фамилия Имя Отчество дата рождения номер телефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, 
вернуть код ошибки, обработать его и показать пользователю сообщение, 
что он ввел меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. 
Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. 
Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, 
пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, 
в него в одну строку должны записаться полученные данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, 
пользователь должен увидеть стектрейс ошибки.
 */
public class finalTask {

    public static void main(String[] args)  {
        
        Scanner input = new Scanner(System.in);
        String text = "";
        List<String> familyArray = new ArrayList<String>();
        while (!text.equals("stop")) {
            System.out.println("Введите данные через пробел, или наберите stop для выхода: ");
            try {
            text = input.nextLine();
            String []array = text.split(" ");

            if (text.equals("stop")) {
                break;
            }
            if (array.length != 6) {
                throw new RuntimeException("Мало или много данных для ввода");   
            }
            
            
            String date = verifyDateFormat(array[3]);
            System.out.println(date);
            
            if (!date.equals(array[3])) {
                throw new RuntimeException("Ввод даты не прошел проверку на валидность");
            }

            Long number = Long.parseLong(array[4]);

            if (!array[5].equals("f") && !array[5].equals("m")) {
                throw new RuntimeException("В России у людей существует только два пола!");
            }

            String family = array[0];
            
            if (isFamilyInArray(family, familyArray) == false) {
            familyArray.add(family);
            }

            try (FileWriter writer = new FileWriter(family + ".txt", isFamilyInArray(family, familyArray))) {
                String textInFile = array[0] + " " + array[1] + " " + array[2] + " " + array[3] + " " + array[4] + " " + array[5];
                writer.write(textInFile);
                writer.append("\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(Arrays.toString(array));
            //System.out.println(familyArray);
            
            } catch (NumberFormatException e) {
                System.out.println("Неверный  формат номера телефона");
            } catch (RuntimeException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
      
        input.close();

    }

    public static String verifyDateFormat(String text) {
        String resultText = "";
        try {
            String []dateArray = text.split("\\.");
            if (dateArray.length != 3) throw new RuntimeException("Неверное количество данных");
            int day = Integer.parseInt(dateArray[0]);
            int month = Integer.parseInt(dateArray[1]);
            int year = Integer.parseInt(dateArray[2]);
            
            if (isValidFormat(day, month, year) == false) {
                throw new RuntimeException("Неверно задана дата");
            }
            StringBuilder textBuild = new StringBuilder();       
            textBuild = textBuild.append(dateArray[0]).append(".").append(dateArray[1]).append(".").append(dateArray[2]);
            resultText = textBuild.toString();

        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода чисел даты");
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        
        
        return resultText;
    }

    public static boolean isValidFormat (int day, int month, int year) {
        if ((day<1 || day>31) || (month<1 || month>12) || (year<1922 || year>2022)) {
            return false;
        } else {
            if (day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) {
                return false;
            } else {
                if (day == 30 && month == 2) {
                    return false;
                } else {
                   if ((day == 29) && (month == 2) && (year %4 != 0)) {
                    return false;
                   } else {
                    return true;
                   }
                }
                
            }
            
        }
    }

    public static boolean isFamilyInArray (String text, List<String> textArray) {
        int count = 0;
        for (int i = 0; i < textArray.size(); i++) {
            if (text.equals(textArray.get(i))) {
                count++;
            }
        }

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}
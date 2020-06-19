import storage.Manufactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;

public class OnServerStart implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            FileReader fileReader = new FileReader("pathes.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                String[] words = line.split(" ");
                Manufactory.pathes.put(words[0], words[1]);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

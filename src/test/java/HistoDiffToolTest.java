
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.junit.Test;

import com.landon30.profiler.tools.Histo;

/**
 * 工具单元测试
 *
 * @date 2019-01-15
 * @author landon30
 */
public class HistoDiffToolTest {

    @Test
    public void testReadHisto() throws Exception {
        Path histoPath = Paths.get("src/main/resources", "pre-fgc-histo.log");
        List<String> histos = Files.readAllLines(histoPath, Charset.forName("UTF-8"));
        String fourLine = histos.get(3);

        String strNum = fourLine.substring(0, 4);
        System.out.println(strNum);
        long longNum = Long.parseLong(strNum.trim());
        System.out.println(longNum);

        String instances = fourLine.substring(5, 19);
        System.out.println(instances);
        long longInstances = Long.parseLong(instances.trim());
        System.out.println(longInstances);

        String bytes = fourLine.substring(19, 34);
        System.out.println(bytes);
        long longBytes = Long.parseLong(bytes.trim());
        System.out.println(longBytes);

        String className = fourLine.substring(36, fourLine.length());
        System.out.println(className);
    }

    @Test
    public void testReadHisto2() throws Exception {
        Path histoPath = Paths.get("src/main/resources", "pre-fgc-histo.log");
        List<String> histos = Files.readAllLines(histoPath, Charset.forName("UTF-8"));

        for (int i = 3; i < 10; i++) {
            String line = histos.get(i);

            String strNum = line.substring(0, 4);
            long longNum = Long.parseLong(strNum.trim());
            System.out.print(longNum);

            System.out.print("\t");

            String instances = line.substring(5, 19);
            long longInstances = Long.parseLong(instances.trim());
            System.out.print(longInstances);

            System.out.print("\t");

            String bytes = line.substring(19, 34);
            long longBytes = Long.parseLong(bytes.trim());
            System.out.print(longBytes);

            System.out.print("\t");

            String className = line.substring(36, line.length());
            System.out.print(className);

            System.out.println();
        }
    }

    @Test
    public void testHistoBuild() {
        Histo histo = new Histo.HistoBuilder().num(1).instances(2).bytes(3).className("landon")
                .build();
        System.out.println(histo);
    }

    @Test
    public void testTreeMap() {
        TreeMap<Integer, Integer> map = new TreeMap<>();

        map.put(10084, 2);
        map.put(200, 3);
        map.put(16000, 3);

        System.out.println(map);
    }

    @Test
    public void testFormat() {
        float d = 0.2879f;
        DecimalFormat format = new DecimalFormat("0.00");
        System.out.println(format.format(d));
    }

    @Test
    public void testDivision() {
        long i = 100;
        long j = 179;
        System.out.println(i / j);
        System.out.println(i * 1.0 / j);

        DecimalFormat format = new DecimalFormat("0.00000");
        System.out.println(format.format(i * 1.0 / j));

        System.out.println(-3 * 1.0f / 33397);
        System.out.println(format.format(-3 * 1.0f / 33397));
    }

    @Test
    public void testResourceBundle() throws Exception {
        InputStream in = new BufferedInputStream(
                new FileInputStream("src/main/resources/config.properties"));
        Properties p = new Properties();
        p.load(in);

        System.out.println(p.getProperty("preGCHistoName"));
    }
}

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class CallWebService {

    public int post(String urlStr, File file, String output,String ext) throws IOException {
        System.out.println("post！！"+file.getName());
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);    // 可以发送数据
        conn.setDoInput(true);    // 可以接收数据
        conn.setRequestMethod("POST");    // POST方法
        // 必须注意此处需要设置UserAgent，否则google会返回403
        conn.setRequestProperty
                ("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();

        //    写入的POST数据
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//        String content= FileUtils.file2String(file);
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
//        System.out.println(content);
//        osw.write("query="+content);
        osw.write("query="+ URLEncoder.encode(content, "UTF-8"));
        osw.flush();
        osw.close();
        int code = conn.getResponseCode();
        if( code != 200 ) {
        	// error from the server side;
        	conn.disconnect();
        	return code;
        }
        // 读取响应数据

        BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
        String s;
        StringBuilder sb=new StringBuilder();
        FileWriter fileWriter=new FileWriter(output + file.getName().replace(".txt",ext));
        while ((s = in.readLine()) != null){
            sb.append(s);
            //或者这里直接写入到文本中
            fileWriter.write(s);
        }
        fileWriter.close();
        conn.disconnect();
        return code;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
//        String s=main();
//        System.out.println(s);
        String indir = "corpus/oriText/10notes/";
        String outdir = "corpus/oriText/10notes_output/";
        for(File file: new File(indir).listFiles()){
            if (file.getName().startsWith(".")){
                continue;
            }
            if (new File(outdir + file.getName().replace(".txt", ".json")).exists()){
                continue;
            }
            
            CallWebService service = new CallWebService();
            int code = service.post( "http://localhost:8080/query/json", file, outdir,".json");
            System.out.println( code );
            TimeUnit.MILLISECONDS.sleep(5000L);
        }
    }
}

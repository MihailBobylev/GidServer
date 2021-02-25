import org.json.JSONArray;
import org.json.JSONObject;
import sun.rmi.runtime.Log;

import javax.naming.Context;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConnectionWorker implements Runnable {
    private static final String TAG = "OcrCaptureActivity";
    private Calendar calendar = Calendar.getInstance();
    /* сокет, через который происходит обмен данными с клиентом*/
    private Socket clientSocket = null;
    /* входной поток, через который получаем данные с сокета */
    private InputStream inputStream = null;
    public ConnectionWorker(Socket socket) {
        clientSocket = socket;
    }
    @Override
    public void run() {
        /* получаем входной поток */
        try {
            inputStream = clientSocket.getInputStream();
        }
        catch (IOException e) {
            System.out.println("Cant get input stream");
        }
        /* создаем буфер для данных */
        byte[] buffer = new byte[1024*4];
        String text;
        while(true) {
            try {
                /* получаем очередную порцию данных, в переменной count хранится реальное количество байт, которое получили */
                int count = inputStream.read(buffer,0,buffer.length);
                /* проверяем, какое количество байт к нам пришло */
                if (count > 0) {
                    String tmp = new String(buffer,0,count);
                    text = tmp.substring(1, tmp.length()-2);
                    text = text.replace(" ", "");

                    if (text != null) {
                        System.out.println("text data is being spoken! " + text);

                        LessonOrder lessonOrder = new LessonOrder();

                        String fileName = text;
                        //String result = readObjJSON(fileName);

                        if((calendar.getTimeInMillis() - dateModify(fileName).getTimeInMillis()) > (7*24*60*60*1000)){
                            LessonInfo l = getCurrentClass(text, 1, 2, 0); //text, lessonOrder.getDayWeek(), lessonOrder.getOrder(),lessonOrder.getWeek()
                            System.out.println(l.className);
                            System.out.println(l.groupName);
                            System.out.println(l.teacherName);
                            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                            toClient.println(l.className + " " + l.groupName + " " + l.teacherName);
                        }

                    }
                    else {
                        System.out.println("text data is null");
                    }
                    //System.out.println(new Integer(String.valueOf(buffer)));
                    //System.out.println(new String(buffer,0,count));
                }
                else
                    /* если мы получили -1, значит прервался наш поток с данными  */
                    if (count == -1 ) {
                        System.out.println("close socket");
                        clientSocket.close();
                        break;
                    }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public String getUrl(){

        return "https://ksu.edu.ru/timetable/" + calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH)/8 + 1) + "/timetable.php";

    }
    public LessonInfo getCurrentClass(String room, int dayOfWeek, int lessonNumber, int week) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action","getauds");
        params.put("id", "5");// корпус Е

        String roomsJSON = doPostQuery(getUrl(), params); //"https://ksu.edu.ru/timetable/2020_1/timetable.php"
        JSONArray jObj = new JSONArray(roomsJSON);
        String post_id = null;
        for (int i = 0; i < jObj.length(); i++){
            if (jObj.getJSONObject(i).getString("title").equals("Е-" + room)) {
                post_id = jObj.getJSONObject(i).getString("id");
                break;
            }
        }
        if (post_id == null)
            return null;

        params.clear();
        params.put("action", "gettimetable");
        params.put("mode","aud");
        params.put("id", post_id);

        String ttJSON = doPostQuery(getUrl(), params); //"https://ksu.edu.ru/timetable/2020_1/timetable.php"
        jObj = new JSONArray(ttJSON);

        String x = String.valueOf(dayOfWeek);
        String y = String.valueOf(lessonNumber);
        String n = String.valueOf(week);
        String className = null;
        String groupName = null;
        String teacherName = null;
        for (int i = 0; i < jObj.length(); i++){
            JSONObject item = jObj.getJSONObject(i);
            if (item.getString("x").equals(x) && item.getString("y").equals(y) && item.getString("n").equals(n)){
                className = item.getString("subject1");
                groupName = item.isNull("subgroup")
                        ? item.getString("subject2")
                        : item.getString("subgroup");
                teacherName = item.getString("subject3");
                break;
            }

        }
        if (className == null)
            return null;
        else
            return new LessonInfo(className, groupName, teacherName);
    }

    private static String doPostQuery(String url, Map<String, String> params) throws IOException {
        URL buildingUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) buildingUrl.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
        for (Map.Entry<String, String> entry: params.entrySet()) {
            out.writeBytes(URLEncoder.encode(entry.getKey(), "UTF-8"));
            out.writeBytes("=");
            out.writeBytes(URLEncoder.encode(entry.getValue(), "UTF-8"));
            out.writeBytes("&");
        }
        out.flush();
        out.close();
        httpURLConnection.setConnectTimeout(1000);
        httpURLConnection.setReadTimeout(1000);

        int status = httpURLConnection.getResponseCode();
        if (status != 200){
            throw new IOException("status is not 200 OK");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }
        in.close();
        httpURLConnection.disconnect();
        return content.toString();
    }

    public Calendar dateModify(String fileName){

        File file = new File(fileName);
        Date lastModDate = new Date(file.lastModified());
        Calendar calendarM = Calendar.getInstance();
        calendarM.setTime(lastModDate);

        return calendarM;
    }
}


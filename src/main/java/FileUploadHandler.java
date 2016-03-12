
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import java.util.Iterator;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(name = "FileUploadHandler", urlPatterns = {"/FileUploadHandler"})public class FileUploadHandler extends HttpServlet{



	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

		OSConnector conn = new OSConnector();
		
		String filename = null;
		Payload upfile = null;
		
		conn.createContainer("sample");
			
		if(ServletFileUpload.isMultipartContent(request)){
			try{
				 FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                try {

                    List<FileItem> fields = upload.parseRequest(request);
                    Iterator<FileItem> it = fields.iterator();
                    while (it.hasNext()) {
                        FileItem fileItem = it.next();
                        boolean isFormField = fileItem.isFormField();
                        if (isFormField) {
                        } else {
                            filename = fileItem.getName();
                            upfile = Payloads.create(fileItem.getInputStream());
                        }
				

                    }

                    if (!filename.isEmpty() && !(upfile == null)) {
                        conn.uploadFile("sample", filename, upfile); //upload file to storage
                    }
					
					S2TConnector conn1 = new S2TConnector();
					SpeechToText service = new SpeechToText();
					service.setUsernameAndPassword(conn1.getUsername(),conn1.getPassword());

					File audio = (File)conn.getFile(filename,"sample"); //get the uploaded file from storage
			
					JSONParser parser = new JSONParser();
					SpeechResults output = service.recognize(audio,"audio/wav"); //produce the text in json format
					
					String outputs = output.toString();
					Object speechoutput = parser.parse(outputs);
					JSONObject object = (JSONObject) speechoutput;
					JSONArray text = (JSONArray) object.get("results");
					String finaltext=null;
					
					for (int i = 0; i < text.size(); i++) {
						JSONObject objects = (JSONObject) text.get(i);
						JSONArray alternative= (JSONArray) objects.get("alternatives");
						for (int j = 0; j < alternative.size(); j++) {
							JSONObject innerObj = (JSONObject) alternative.get((j));
								finaltext = " " + innerObj.get("transcript");  //get the translated text
								
						}
					}
					
					request.setAttribute("result",finaltext);
					request.getRequestDispatcher("convert.jsp").forward(request,response);
                } catch (Exception e) {
                }
			} catch(Exception e){}
			

		}
		
	}

	
	

}
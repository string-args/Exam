import java.util.List;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.storage.object.SwiftAccount;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.openstack.OSFactory;



public class S2TConnector{

	private String username;
	private String password;

    public S2TConnector() {
        setCredentials();
    }

    private void setCredentials() {
        Map<String, String> env = System.getenv();

        if (env.containsKey("VCAP_SERVICES")) {

            try {
                JSONParser parser = new JSONParser();
                JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
                JSONObject service = null;

                for (Object key : vcap.keySet()) {
                    String keyStr = (String) key;
                    if (keyStr.toLowerCase().contains("speech_to_text")) {
                        service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
                        break;
                    }
                }

                if (service != null) {
                    JSONObject creds = (JSONObject) service.get("credentials");
                    String username = (String) creds.get("username");
					String password = (String) creds.get("password");
                    this.username = username;
					this.password = password;
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public String getUsername() {
        return username;
        //return "6bd67e0c-f64f-475c-828e-62d028bf9ffb";
    }

	public String getPassword() {
        return password;
        //return "5M9IkxbMmZjg";
    }
	
	


}
package uz.pdp.rentseekerlongpolling.model.locationModels;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;

@Generated("com.asif.gsonpojogenerator")
public class Info implements Serializable {

    private int statuscode;

    private Copyright copyright;

    private List<Object> messages;

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setCopyright(Copyright copyright) {
        this.copyright = copyright;
    }

    public Copyright getCopyright() {
        return copyright;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    public List<Object> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return
                "Info{" +
                        "statuscode = '" + statuscode + '\'' +
                        ",copyright = '" + copyright + '\'' +
                        ",messages = '" + messages + '\'' +
                        "}";
    }
}
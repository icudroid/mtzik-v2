package fr.k2i.adbeback.webapp.bean;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class PersonBean implements Serializable{
    private Long id;
	private String firstName;
	private String lastName;
    private String photo;

    public String getFullName(){
        if(StringUtils.isEmpty(firstName)){
            return lastName;
        }else{
            return firstName + " " + lastName;
        }

    }

	public PersonBean(Long id, String firstName, String lastName,String photo) {
		super();
        this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
        this.photo = photo;
	}
}

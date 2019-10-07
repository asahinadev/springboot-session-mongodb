package com.example.spring.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.spring.validation.UniqueDocument;
import com.example.spring.validation.group.Create;
import com.example.spring.validation.group.Save;

import lombok.Data;

@Data
@UniqueDocument.List({
		@UniqueDocument(field = "email", groups = { Save.class, Create.class }),
		@UniqueDocument(field = "username", groups = { Save.class, Create.class }),
})
public class UserForm {

	String id;

	@NotEmpty(groups = { Save.class, Create.class })
	@Pattern(regexp = "[\\w]+", groups = { Save.class, Create.class })
	@Length(min = 4, max = 16, groups = { Save.class, Create.class })
	String username;

	@NotEmpty(groups = { Save.class, Create.class })
	@Email(groups = { Save.class, Create.class })
	@Length(min = 4, max = 255, groups = { Save.class, Create.class })
	String email;

	@NotEmpty(groups = { Create.class })
	@Pattern(regexp = "[\\w]+", groups = { Save.class, Create.class })
	@Length(min = 8, max = 16, groups = { Save.class, Create.class })
	String password;

}

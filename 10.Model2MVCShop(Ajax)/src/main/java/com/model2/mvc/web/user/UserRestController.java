package com.model2.mvc.web.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method ���� ����
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");
		
		//Business Logic
		return userService.getUser(userId);
	}

	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(	@RequestBody User user,
									HttpSession session ) throws Exception{
	
		System.out.println("/user/json/login : POST");
		//Business Logic
		System.out.println("����� ���������� ���߰ڴ�::"+user);
		User dbUser=null;
		dbUser=userService.getUser(user.getUserId());
		System.out.println("dbUser : "+dbUser);
		if(dbUser!=null && user.getPassword().equals(dbUser.getPassword())){
			System.out.println("��й�ȣ ��ġ");
			session.setAttribute("user", dbUser);
			return dbUser;
		}else if(dbUser!=null && !(user.getPassword().equals(dbUser.getPassword()))) {
			System.out.println("��й�ȣ�� �߸� �Է��� ���");
			return user;
		}else{
			System.out.println("���̵���� �߸� �Է��� ���");
			return new User();
		}
	}
}
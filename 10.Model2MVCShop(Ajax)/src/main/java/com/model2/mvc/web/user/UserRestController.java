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


//==> 회원관리 RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
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
		System.out.println("여기로 들어오는지나 봐야겠다::"+user);
		User dbUser=null;
		dbUser=userService.getUser(user.getUserId());
		System.out.println("dbUser : "+dbUser);
		if(dbUser!=null && user.getPassword().equals(dbUser.getPassword())){
			System.out.println("비밀번호 일치");
			session.setAttribute("user", dbUser);
			return dbUser;
		}else if(dbUser!=null && !(user.getPassword().equals(dbUser.getPassword()))) {
			System.out.println("비밀번호를 잘못 입력한 경우");
			return user;
		}else{
			System.out.println("아이디부터 잘못 입력한 경우");
			return new User();
		}
	}
}
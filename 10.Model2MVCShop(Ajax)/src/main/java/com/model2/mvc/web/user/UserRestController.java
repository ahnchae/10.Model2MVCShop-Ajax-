package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
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
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping("/json/addCoupon")
	public Map addCoupon(@RequestParam("couponId") String couponId, HttpSession session) throws Exception{
		System.out.println("/user/addCoupon");
			
		User user = (User)session.getAttribute("user");
		System.out.println("addCoupon user : "+ user);
		userService.addCoupon(couponId, user);
			
		session.setAttribute("user", userService.getUser(((User)session.getAttribute("user")).getUserId()));
		
		Map map = new HashMap();
		if(userService.getUser(((User)session.getAttribute("user")).getUserId()).getCoupon()!=null) {
			map.put("message", "ok");
		}else {
			map.put("message", "no good");
		}
		
		return map;
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
		System.out.println("::"+user);
		User dbUser=null;
		dbUser=userService.getUser(user.getUserId());
		System.out.println("dbUser : "+dbUser);
		if(dbUser!=null && user.getPassword().equals(dbUser.getPassword())){
			System.out.println("비밀번호 일치");
			session.setAttribute("user", dbUser);
			System.out.println("~~~~~~~~~~~~~");
			return dbUser;
		}else if(dbUser!=null && !(user.getPassword().equals(dbUser.getPassword()))) {
			System.out.println("비밀번호를 잘못 입력한 경우");
			return user;
		}else{
			System.out.println("아이디부터 잘못 입력한 경우");
			return new User();
		}
	}
	
	//내정보 수정 화면 보여주기(내정보 필요)
	@RequestMapping( value="json/updateUser/{userId}", method=RequestMethod.GET )
	public User updateUser( @PathVariable String userId) throws Exception{

		System.out.println("/user/json/updateUser/{userId} : GET");
		//Business Logic
		User user = userService.getUser(userId);

		return user;
	}
	
	//내정보 수정 한 후 수정 정보 보여주기(내정보 필요)
	@RequestMapping( value="json/updateUser/{userId}", method=RequestMethod.POST )
	public User updateUser( @RequestBody User user, @PathVariable String userId) throws Exception{

		System.out.println("/user/json/updateUser/{userId} : POST");
		//Business Logic
		System.out.println("@RequestBody User user : "+user);
		
		user.setUserId(userId);
		System.out.println("0~~~~~~~~~~~~~~~"+user+"~~~~~~~~~~~~~~~~~~");
		userService.updateUser(user);

		return userService.getUser(userId);
	}
	
	//회원가임) DB에 user정보 더하기
	@RequestMapping( value="json/addUser", method=RequestMethod.POST )
	public User addUser( @RequestBody User user, HttpServletResponse response ) throws Exception {

		System.out.println("/user/json/addUser : POST");
		//Business Logic
		userService.addUser(user);
		return userService.getUser(user.getUserId());
	}
	
	@RequestMapping( value="json/logout", method=RequestMethod.GET )
	public Map logout(HttpSession session ) throws Exception{
		
		System.out.println("/user/json/logout : POST");
		
		session.invalidate();
		
		Map<String, String> map = new HashMap<>();
		map.put("message", "로그아웃 되었습니다.");
		return map;
	}
	
	//ID 중복확인
	@RequestMapping( value="json/checkDuplication/{userId}", method=RequestMethod.POST )
	public Map checkDuplication( @PathVariable String userId) throws Exception{
		
		System.out.println("/user/json/checkDuplication : POST");
		//Business Logic
		boolean result=userService.checkDuplication(userId);
		// Model 과 View 연결
		Map<String, String> map = new HashMap<>();
		if(result) {
			map.put("message", "ok");
		}else {
			map.put("message", "no good");
		}

		return map;
	}
	
	@RequestMapping( value="json/listUser", method=RequestMethod.POST)
	public Map listUser(@RequestBody Search search) throws Exception{
		
		System.out.println("/user/json/listUser");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", map.get("list"));
		returnMap.put("resultPage", resultPage);
		returnMap.put("search", search);
		
		return returnMap;
	}
	
	@ExceptionHandler(value=Exception.class)
	public String error(Exception e) throws Exception{
		e.printStackTrace();
		return e.getMessage();
	}
}
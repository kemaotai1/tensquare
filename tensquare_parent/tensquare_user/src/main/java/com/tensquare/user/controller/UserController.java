package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		/*//1.从请求头获取是否存在Authorization
		String auth = request.getHeader("Authorization");
		if(auth==null){
			return new Result(false,StatusCode.ACCESS_ERROR,"权限不足");
		}

		//2.判断是否以Bearer开头
		if(!auth.startsWith("Bearer")){
			return new Result(false,StatusCode.ACCESS_ERROR,"权限不足");
		}

		//3.截取token字符串
		String token = auth.substring(7);

		//4.使用jjwt解析token字符串是否合法
        Claims claims = jwtUtil.parseJWT(token);

        if(claims==null){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足");
        }

        //5.判断当前用户是否为管理员
        if(!claims.get("roles").equals("admin")){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足");
        }
*/

        //判断当前用户是否为管理员
        Claims claims = (Claims)request.getAttribute("admin_claims");
        if(claims==null){
            //不是管理员
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足");
        }

        userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"手机验证码发送成功");
	}

	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@PathVariable String code,@RequestBody User user){
		Boolean flag = userService.register(code,user);

		if(flag){
			return new Result(true,StatusCode.OK,"用户注册成功");
		}else{
			return new Result(false,StatusCode.ERROR,"验证码输入有误");
		}
	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		User loginUser = userService.login(user);

		if(loginUser==null){
			//登录失败
			return new Result(false,StatusCode.USER_PASS_ERROR,"用户或密码错误");
		}else{
			//登录成功

			//生成token字符串（包含用户ID），返回给前端
			String token = jwtUtil.createJWT(loginUser.getId(),loginUser.getMobile(),"user");
			Map data = new HashMap();
			data.put("name",loginUser.getMobile());
			data.put("token",token);


			return new Result(true,StatusCode.OK,"登录成功",data);
		}
	}

	/**
	 * 更新关注数
	 */
	@RequestMapping(value = "/updateFollowcount/{userid}/{x}",method = RequestMethod.PUT)
	public Result updateFollowcount(@PathVariable String userid,@PathVariable int x){
		 userService.updateFollowcount(userid,x);
		return new Result(true,StatusCode.OK,"更新关注数成功");
	}

	/**
	 * 更新粉丝数数
	 */
	@RequestMapping(value = "/updateFanscount/{userid}/{x}",method = RequestMethod.PUT)
	public Result updateFanscount(@PathVariable String userid,@PathVariable int x){
		userService.updateFanscount(userid,x);
		return new Result(true,StatusCode.OK,"更新粉丝数成功");
	}
}

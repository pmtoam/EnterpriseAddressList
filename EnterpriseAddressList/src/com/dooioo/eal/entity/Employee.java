package com.dooioo.eal.entity;

public class Employee
{
//	public String empNum;
//	public String empName;
//	public String sex;
//	public String department;
//	public String phoneNum;
//	public String photo;
	
//	  userCode: 工号
//    userNameCn: 姓名
//    userTitle: 头衔
//    orgClass: 目前在职员工只有两种: 前台/后台
//    sex: 性别
//    mobilePhone: 常用号码
//    alternatePhone: 其他号码
//    showedPhone: 显示的号码
//    orgName: 组织名
//    orgId: 组织Id
//    positionName: 岗位名称
//    joinDate: 首次入止日期(不会随着离职, 回聘的再次入职日期而改变)
//    latestJoinDate: 最新入职日期(如果该员工未曾离职过, 则与joinDate值一样, 否则显示的是最近一次的入职时间)
//    orgAdress: 组织地址
	
	public String userCode;
	public String userNameCn;
	public String userTitle;
	public String orgClass;
	public String sex;
	public String mobilePhone;
	public String alternatePhone;
	public String showedPhone;
	public String orgName;
	public String orgId;
	public String positionName;
	public String joinDate;
	public String latestJoinDate;
	public String orgAdress;
	
	
	public Employee()
	{
		super();
	}


	public Employee(String userNameCn, String userTitle, String mobilePhone,
			String orgName)
	{
		super();
		this.userNameCn = userNameCn;
		this.userTitle = userTitle;
		this.mobilePhone = mobilePhone;
		this.orgName = orgName;
	}
	
	
}

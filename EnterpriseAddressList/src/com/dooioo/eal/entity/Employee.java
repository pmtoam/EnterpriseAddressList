package com.dooioo.eal.entity;

public class Employee
{
//	public String empNum;
//	public String empName;
//	public String sex;
//	public String department;
//	public String phoneNum;
//	public String photo;
	
//	  userCode: ����
//    userNameCn: ����
//    userTitle: ͷ��
//    orgClass: Ŀǰ��ְԱ��ֻ������: ǰ̨/��̨
//    sex: �Ա�
//    mobilePhone: ���ú���
//    alternatePhone: ��������
//    showedPhone: ��ʾ�ĺ���
//    orgName: ��֯��
//    orgId: ��֯Id
//    positionName: ��λ����
//    joinDate: �״���ֹ����(����������ְ, ��Ƹ���ٴ���ְ���ڶ��ı�)
//    latestJoinDate: ������ְ����(�����Ա��δ����ְ��, ����joinDateֵһ��, ������ʾ�������һ�ε���ְʱ��)
//    orgAdress: ��֯��ַ
	
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

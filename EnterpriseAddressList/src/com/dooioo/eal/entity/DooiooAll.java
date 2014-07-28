package com.dooioo.eal.entity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DooiooAll implements Serializable
{
	// 'text': '浦城店',
	// 'id': '20179',
	// 'type': '门店',
	// 'status': '1',
	// 'orgClass': '前台',
	// 'children': [

	public String text;
	public String id;
	public String type;
	public String status;
	public String orgClass;
	public List<DooiooAll> children;

}

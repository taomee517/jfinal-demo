package com.jfc.controller;

import com.jfinal.core.Controller;

public class IndexCtrl extends Controller {
	public void index() {
		System.out.println("进入controller");
		renderJson("Hello~ Jfinal!!");
	}
}

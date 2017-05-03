package com.seck.hzy.lorameterapp.LoRaApp.p_activity;

import com.seck.hzy.lorameterapp.LoRaApp.utils.NetException;

/**
 *
 * 山科通讯错误类型提示。
 *
 * @author l412
 *
 */
public class P_NetExp_Builder {
	public static NetException getExp(int c) {
		return new NetException(c, getExpMessage(c));
	}

	public static String getExpMessage(int c) {
		switch (c) {
			case 0:
				return "正常";
			case 1:
				return "长度异常";
			case 2:
				return "前导符异常";
			case 3:
				return "应答码异常";
			case 4:
				return "校验失败";
			case 5:
				return "数据超时";
			case 0x21:
				return "图像长度异常";
			default:
				return "未知错误";
		}
	}
}

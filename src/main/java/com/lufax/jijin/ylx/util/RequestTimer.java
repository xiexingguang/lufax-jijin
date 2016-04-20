package com.lufax.jijin.ylx.util;

import java.util.Arrays;
import java.util.List;

public class RequestTimer {
	
	private int hour;
	private int minute;
	
	public RequestTimer(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}

    public static RequestTimer parse(String timeout){
        List<String> holder = Arrays.asList(timeout.split(":"));
        int hour = Integer.valueOf(holder.get(0));
        int minute = Integer.valueOf(holder.get(1));
        RequestTimer timer = new RequestTimer(hour,minute);
        return timer;
    }
}

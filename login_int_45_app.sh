#!/usr/bin/expect -f
set timeout 30
spawn -noecho ssh -l wls81opr 172.17.40.45
expect {
	"yes/no" {
		send "yes\r";
		exp_continue
	}
	"password" {
		send "wlsD8881\r"
	}
}

expect "]*"
send "cd /wls/applications\r"

interact
